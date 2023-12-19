import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.lukaspradel.steamapi.data.json.friendslist.GetFriendList;
import com.lukaspradel.steamapi.data.json.playersummaries.GetPlayerSummaries;
import com.lukaspradel.steamapi.webapi.client.SteamWebApiClient;
import com.lukaspradel.steamapi.webapi.request.GetFriendListRequest;
import com.lukaspradel.steamapi.webapi.request.SteamWebApiRequest;
import com.lukaspradel.steamapi.webapi.request.builders.SteamWebApiRequestFactory;
import java.io.*;
import java.util.List;

public class APISteam {
        public static void main(String[] args) throws SteamApiException {
            SteamWebApiClient client = new SteamWebApiClient.SteamWebApiClientBuilder("4537CB87C29E45B6A1C4B4C1A76A1076").build();
            GetFriendListRequest friendList = SteamWebApiRequestFactory.createGetFriendListRequest("76561198962160523");
            System.out.println(friendList.toString());
            GetFriendList request = client.processRequest(friendList);
            SteamWebApiRequest playerSummaries = SteamWebApiRequestFactory.createGetPlayerSummariesRequest(List.of("76561198962160523")); //using player summaries to get Username
            GetPlayerSummaries run = client.processRequest(playerSummaries);
            System.out.println(run.getResponse().getPlayers().get(0).getPersonaname());


            for(int i =0; request.getFriendslist().getFriends().size() > i;i++) {
                playerSummaries = SteamWebApiRequestFactory.createGetPlayerSummariesRequest(List.of(request.getFriendslist().getFriends().get(i).getSteamid()));
                run = client.processRequest(playerSummaries);
                System.out.println(request.getFriendslist(). getFriends().get(i).getSteamid() + ", " + run.getResponse().getPlayers().get(0).getPersonaname());
            }


            File User_Ids = new File("src/main/User_Info/User_Ids");
            try { //Sends the IDS to the User_IDs document
                FileWriter fileWriter = new FileWriter(User_Ids);
                for(int i =0; request.getFriendslist().getFriends().size() > i;i++) {
                    fileWriter.write("\n" + request.getFriendslist().getFriends().get(i).getSteamid());
                }
                fileWriter.close();
                System.out.println(request);
            } catch (Exception e) { //Catch needed for filewriter
                e.printStackTrace();
        }
    }
}
