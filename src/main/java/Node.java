
import java.io.Serializable;
import java.util.LinkedList;

public class Node implements Serializable{
    // private String name;
    String id;
    LinkedList<String> neighbours;
    int depth;

    public Node(String id) {
        this.neighbours = new LinkedList<>();
        this.id = id;
        this.depth = 0;
    }


}
