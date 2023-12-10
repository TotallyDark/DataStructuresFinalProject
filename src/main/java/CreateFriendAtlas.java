import com.lukaspradel.steamapi.core.exception.SteamApiException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class CreateFriendAtlas {
    private static HashSet<Node> discovered;
    private static Queue<Node> queue;
    public CreateFriendAtlas() {
        queue = new LinkedList<>();
        discovered = new HashSet<>();
    }

    public static void Main() {
        String steamID = "76561198962160523";
        Node root = new Node(steamID);
        discovered.add(root);
        queue.add(root);
        int count =0;
        while (queue.size() >0 && count <=3) {
            Node v = queue.poll();
            for (Node n:v.neighbours) {
                if(!discovered.contains(n)){
                    discovered.add(n);
                    queue.add(n);
                }
                count++;
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

    public static void main(String[] args) throws SteamApiException {
        Main();
    }
}
