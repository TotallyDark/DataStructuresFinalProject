import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.lukaspradel.steamapi.data.json.friendslist.GetFriendList;
import com.lukaspradel.steamapi.data.json.playersummaries.GetPlayerSummaries;
import com.lukaspradel.steamapi.webapi.client.SteamWebApiClient;
import com.lukaspradel.steamapi.webapi.request.GetFriendListRequest;
import com.lukaspradel.steamapi.webapi.request.SteamWebApiRequest;
import com.lukaspradel.steamapi.webapi.request.builders.SteamWebApiRequestFactory;

import java.util.LinkedList;
import java.util.List;

public class Node {
    // private String name;
    String id;
    LinkedList<Node> neighbours;

    public Node(String id) {
        this.neighbours = new LinkedList<>();
        this.id = id;
//        SteamWebApiClient client = new SteamWebApiClient.SteamWebApiClientBuilder("4537CB87C29E45B6A1C4B4C1A76A1076").build();
//        GetFriendListRequest friendList = SteamWebApiRequestFactory.createGetFriendListRequest(id);
//        try {
//            GetFriendList request = client.processRequest(friendList);
//            for (int i = 0; request.getFriendslist().getFriends().size() > i; i++) {
//                neighbours.add(new Node(request.getFriendslist().getFriends().get(i).getSteamid()));
//            }
//        } catch (SteamApiException friendlist) {
//            try {
//                SteamWebApiRequest playerSummaries = SteamWebApiRequestFactory.createGetPlayerSummariesRequest(List.of(id)); //using player summaries to get Username
//                GetPlayerSummaries run = client.processRequest(playerSummaries);
//                System.out.println(run.getResponse().getPlayers().get(0).getPersonaname() + "'s Friendlist private!");
//                if (run.getResponse().getPlayers().get(0).getPersonaname().equals("eGoodchild")) {
//                    System.out.println("found it");
//                }
//            } catch (SteamApiException profile) {
//                System.out.println("Profile is private!");
//            }
//        }
    }
}
