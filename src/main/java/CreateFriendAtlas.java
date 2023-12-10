import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.lukaspradel.steamapi.data.json.friendslist.GetFriendList;
import com.lukaspradel.steamapi.webapi.client.SteamWebApiClient;
import com.lukaspradel.steamapi.webapi.request.GetFriendListRequest;
import com.lukaspradel.steamapi.webapi.request.builders.SteamWebApiRequestFactory;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class CreateFriendAtlas {
    private final HashSet<Node> discovered;
    private final Queue<Node> queue;
    public CreateFriendAtlas() {
        queue = new LinkedList<>();
        discovered = new HashSet<Node>();
    }

    public void Main() throws SteamApiException {
        String steamID = "76561198962160523";
        Node root = new Node(steamID);
        discovered.add(root);
        queue.add(root);
        while (queue.size() >0) {
            Node v = queue.poll();
            for (Node n:v.neighbours) {
                if(!discovered.contains(n)){
                    discovered.add(n);
                    queue.add(n);
                }
            }
        }
//        SteamWebApiClient client = new SteamWebApiClient.SteamWebApiClientBuilder("4537CB87C29E45B6A1C4B4C1A76A1076").build();
//        GetFriendListRequest friendList = SteamWebApiRequestFactory.createGetFriendListRequest(steamID);
//        System.out.println(friendList.toString());
//        GetFriendList request = client.processRequest(friendList);
//        for(int i =0; request.getFriendslist().getFriends().size() > i;i++) {
//            friendAtlas.add(new Node(request.getFriendslist().getFriends().get(i).getSteamid()));
//        }
    }
}
