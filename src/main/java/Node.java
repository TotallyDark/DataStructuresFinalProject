import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.lukaspradel.steamapi.data.json.friendslist.GetFriendList;
import com.lukaspradel.steamapi.webapi.client.SteamWebApiClient;
import com.lukaspradel.steamapi.webapi.request.GetFriendListRequest;
import com.lukaspradel.steamapi.webapi.request.builders.SteamWebApiRequestFactory;

import java.util.LinkedList;

public class Node {
    // private String name;
    String id;
    LinkedList<Node> neighbours;

    public Node(String id) throws SteamApiException {
        this.neighbours = new LinkedList<Node>();
        this.id = id;
        SteamWebApiClient client = new SteamWebApiClient.SteamWebApiClientBuilder("4537CB87C29E45B6A1C4B4C1A76A1076").build();
        GetFriendListRequest friendList = SteamWebApiRequestFactory.createGetFriendListRequest(id);
        GetFriendList request = client.processRequest(friendList);
        for(int i =0; request.getFriendslist().getFriends().size() > i;i++) {
            neighbours.add(new Node(request.getFriendslist().getFriends().get(i).getSteamid()));
        }
    }
}
