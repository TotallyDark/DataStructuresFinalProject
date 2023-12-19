import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.lukaspradel.steamapi.data.json.friendslist.Friend;
import com.lukaspradel.steamapi.data.json.friendslist.GetFriendList;
import com.lukaspradel.steamapi.data.json.ownedgames.Game;
import com.lukaspradel.steamapi.data.json.ownedgames.GetOwnedGames;
import com.lukaspradel.steamapi.data.json.playerstats.GetUserStatsForGame;
import com.lukaspradel.steamapi.data.json.playersummaries.GetPlayerSummaries;
import com.lukaspradel.steamapi.webapi.client.SteamWebApiClient;
import com.lukaspradel.steamapi.webapi.request.GetFriendListRequest;
import com.lukaspradel.steamapi.webapi.request.GetOwnedGamesRequest;
import com.lukaspradel.steamapi.webapi.request.SteamWebApiRequest;
import com.lukaspradel.steamapi.webapi.request.builders.SteamWebApiRequestFactory;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;
import java.util.*;

public class CreateFriendAtlas {
    private static HashMap<String,Node> discovered;
    private static HashMap<String,Integer> depths;
    private static Queue<String> queue;

    public CreateFriendAtlas() {
        queue = new ArrayDeque<>();
        discovered = new HashMap<>();
        depths = new HashMap<>();
    }

    public static void BFS() {
        depths = new HashMap<>();
        queue = new LinkedList<>();
        discovered = new HashMap<>();
        String steamID = "76561198962160523";
        depths.put(steamID,0);
        queue.add(steamID);
        SteamWebApiClient client = new SteamWebApiClient.SteamWebApiClientBuilder("4537CB87C29E45B6A1C4B4C1A76A1076").build();
        try {
            CSVPrinter csvWriter = new CSVPrinter(new FileWriter("FriendData_Friends.csv"), CSVFormat.DEFAULT);
            CSVPrinter csvWriterG = new CSVPrinter(new FileWriter("FriendData_Games.csv"), CSVFormat.DEFAULT);

            csvWriter.printRecord("Name","Friends");
            while (queue.size() > 0) {
                String s = queue.poll();
                Node n = new Node(s);
                if(depths.get(n.id) >=2) {
                    break;
                }



                GetFriendListRequest friendList = SteamWebApiRequestFactory.createGetFriendListRequest(n.id);
                SteamWebApiRequest playerSummaries = SteamWebApiRequestFactory.createGetPlayerSummariesRequest(List.of(n.id));
                SteamWebApiRequest playerGames = SteamWebApiRequestFactory.createGetOwnedGamesRequest(n.id);

                try {
                    GetFriendList request = client.processRequest(friendList);
                    GetPlayerSummaries mainPlayer = client.processRequest(playerSummaries);
                    GetOwnedGames games = client.processRequest(playerGames);
                    String mainPlayerName = mainPlayer.getResponse().getPlayers().get(0).getPersonaname();
                    System.out.println("--------------------------------------");
                    System.out.println(mainPlayerName + "'s Friendlist; Depth:" + depths.get(n.id) + "\n");
                    System.out.println(games.getResponse().getGames().get(0).getHasCommunityVisibleStats());

                    for(Game game:games.getResponse().getGames()) {
                        if (game.getPlaytimeForever().intValue() > 120) {
                            System.out.println(game.getName());
                            csvWriterG.printRecord(mainPlayerName, game.getName());
                            csvWriterG.flush();
                        }
                    }
                    for (Friend friend : request.getFriendslist().getFriends()) {
                        SteamWebApiRequest friendSummary = SteamWebApiRequestFactory.createGetPlayerSummariesRequest(List.of(friend.getSteamid()));
                        GetPlayerSummaries player = client.processRequest(friendSummary);
                        String name = player.getResponse().getPlayers().get(0).getPersonaname();
                        n.neighbours.add(friend.getSteamid());
                        csvWriter.printRecord(mainPlayerName, name);
                        csvWriter.flush();

                        if (!discovered.containsKey(friend.getSteamid())) {
                            System.out.println(name);
                            discovered.put(friend.getSteamid(), n);
                            if(!queue.contains(n.id)) {
                                queue.add(friend.getSteamid());
                            }
                            depths.put(friend.getSteamid(), depths.get(n.id)+1);
                        }
                    }

                    assert(new File("FriendData.csv").exists());
                    System.out.println("--------------------------------------\n");
                } catch (Exception friends) {
                    try {
                        GetPlayerSummaries player = client.processRequest(playerSummaries);
                        System.out.println(player.getResponse().getPlayers().get(0).getPersonaname() + "'s Friendlist is Private!");
                    } catch (Exception profile) {
                        System.out.println("Profile Private");
                    }
                }

            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        try {
            FileOutputStream fos = new FileOutputStream("Node.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(discovered);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }



    public static void main(String[] args) throws SteamApiException {
        BFS();
    }
}
