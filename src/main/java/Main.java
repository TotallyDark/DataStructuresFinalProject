import java.util.HashMap;
import java.util.LinkedList;

public class Main {
    private HashMap<String, Node> nodes;

    public Main() {
        this.nodes = new HashMap<>();
    }

    //Creates a node and adds it to the graph
    public void addVertex(String label) {
        nodes.put(label, new Node(label));
    }

    //Adds a connecting edge between 2 nodes

    public boolean hasEdge(String label1, String label2) {
        LinkedList<Node> neighbours = nodes.get(label1).neighbours;
        for (int i = 0; i < neighbours.size(); i++) {
            if (nodes.get(label1).neighbours.get(i).id.equals(label2)) {
                return true;
            }
            if (nodes.get(label2).neighbours.get(i).id.equals(label1)) {
                return true;
            }
        }
        return false;
    }

    public void addEdge(String label1, String label2) {
        nodes.get(label1).neighbours.add(nodes.get(label2));
        nodes.get(label2).neighbours.add(nodes.get(label1));
    }

    public void removeVertex(String label) {
        for (int i = 0; i < nodes.get(label).neighbours.size(); i++) {
            removeEdge(label, nodes.get(label).neighbours.get(i).id);
        }
        nodes.remove(label);
    }

    // Removes the edge between the given vertices
    public void removeEdge(String label1, String label2) {
        for (int i = 0; i < nodes.get(label1).neighbours.size(); i++) {
            if (nodes.get(label1).neighbours.get(i).id.equals(label2)) {
                nodes.get(label1).neighbours.remove(i);
                break;
            }
        }
        for (int i = 0; i < nodes.get(label2).neighbours.size(); i++) {
            if (nodes.get(label2).neighbours.get(i).id.equals(label1)) {
                nodes.get(label2).neighbours.remove(i);
                break;
            }
        }

//        ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);
//        projectController.newProject();
//        Workspace workspace = projectController.getCurrentWorkspace();
    }
}
