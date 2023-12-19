import com.lukaspradel.steamapi.data.json.playersummaries.GetPlayerSummaries;
import com.lukaspradel.steamapi.webapi.client.SteamWebApiClient;
import com.lukaspradel.steamapi.webapi.request.SteamWebApiRequest;
import com.lukaspradel.steamapi.webapi.request.builders.SteamWebApiRequestFactory;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Main {
    private final HashMap<String, Node> nodes;

    public Main(HashMap<String, Node> discovered) {
        this.nodes = discovered;
    }

    //Creates a node and adds it to the graph
    public void addVertex(String label) {
        nodes.put(label, new Node(label));
    }

    //Adds a connecting edge between 2 nodes

    public boolean hasEdge(Node n1, Node n2) {
        for (int i = 0; i < n1.neighbours.size(); i++) {
            if (n1.neighbours.get(i).equals(n2.id)) {
                return true;
            }
            if (n2.neighbours.get(i).equals(n1.id)) {
                return true;
            }
        }
        return false;
    }

    public void addEdge(Node n1, Node n2) {
        n1.neighbours.add(n2.id);
        n2.neighbours.add(n1.id);
    }

    public void removeVertex(Node n1) {
        for (int i = 0; i < n1.neighbours.size(); i++) {
            removeEdge(n1, nodes.get(n1.neighbours.get(i)));
        }
        nodes.remove(n1.id);
    }

    // Removes the edge between the given vertices
    public void removeEdge(Node n1, Node n2) {
        for (int i = 0; i < n1.neighbours.size(); i++) {
            if (n1.neighbours.get(i).equals(n2.id)) {
                n1.neighbours.remove(i);
                break;
            }
        }
        for (int i = 0; i < n2.neighbours.size(); i++) {
            if (n2.neighbours.get(i).equals(n1.id)) {
                n2.neighbours.remove(i);
                break;
            }
        }
    }

    public static void main(String[] args) {
        SteamWebApiClient client = new SteamWebApiClient.SteamWebApiClientBuilder("4537CB87C29E45B6A1C4B4C1A76A1076").build();
        try {
            FileInputStream fis = new FileInputStream("Node.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Node n = (Node)(ois.readObject());
            for(String s:n.neighbours) {
                SteamWebApiRequest friendSummary = SteamWebApiRequestFactory.createGetPlayerSummariesRequest(List.of(s));
                GetPlayerSummaries player = client.processRequest(friendSummary);
                String name = player.getResponse().getPlayers().get(0).getPersonaname();
                System.out.println(name);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }


    }
}
