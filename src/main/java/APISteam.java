import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.lukaspradel.steamapi.data.json.appnews.GetNewsForApp;
import com.lukaspradel.steamapi.data.json.friendslist.Friendslist;
import com.lukaspradel.steamapi.data.json.friendslist.GetFriendList;
import com.lukaspradel.steamapi.data.json.playersummaries.GetPlayerSummaries;
import com.lukaspradel.steamapi.data.json.playersummaries.Player;
import com.lukaspradel.steamapi.webapi.client.SteamWebApiClient;
import com.lukaspradel.steamapi.webapi.request.GetFriendListRequest;
import com.lukaspradel.steamapi.webapi.request.GetNewsForAppRequest;
import com.lukaspradel.steamapi.webapi.request.builders.SteamWebApiRequestFactory;

    public class APISteam {
        public static void main(String[] args) throws SteamApiException {

            SteamWebApiClient client = new SteamWebApiClient.SteamWebApiClientBuilder("4537CB87C29E45B6A1C4B4C1A76A1076").build();
            GetFriendListRequest friendList = SteamWebApiRequestFactory.createGetFriendListRequest("76561198962160523");

            System.out.println(friendList.toString());
            GetFriendList request = client.processRequest(friendList);
            for(int i =0; request.getFriendslist().getFriends().size() > i;i++) {
                System.out.println(request.getFriendslist().getFriends().get(i).getSteamid());
            }

            System.out.println(request);

        }
    }
