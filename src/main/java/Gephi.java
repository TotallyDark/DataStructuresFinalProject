import org.gephi.appearance.api.AppearanceController;
import org.gephi.appearance.api.AppearanceModel;
import org.gephi.filters.api.FilterController;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.graph.api.UndirectedGraph;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.importer.impl.ImportContainerFactoryImpl;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.types.EdgeColor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

import java.awt.*;
import java.io.File;
import java.io.IOException;


public class Gephi {
    private Workspace workspace;
    public Gephi() {
        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        this.workspace = pc.getCurrentWorkspace();
    }
    public void script() {
        //Initializing Workspace
        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        Workspace workspace = pc.getCurrentWorkspace();

        //Creating a node
        Container container;
        GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel();
        PreviewModel model = Lookup.getDefault().lookup(PreviewController.class).getModel();
        ImportController importController = Lookup.getDefault().lookup(ImportController.class);
        FilterController filterController = Lookup.getDefault().lookup(FilterController.class);
        AppearanceController appearanceController = Lookup.getDefault().lookup(AppearanceController.class);
        AppearanceModel appearanceModel = appearanceController.getModel();

        Node n1 = graphModel.factory().newNode("Name");
        UndirectedGraph undirectedGraph = graphModel.getUndirectedGraph();
        undirectedGraph.addNode(n1);


        model.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
        model.getProperties().putValue(PreviewProperty.EDGE_COLOR, new EdgeColor(Color.GRAY));
        model.getProperties().putValue(PreviewProperty.EDGE_THICKNESS, 0.1);
        model.getProperties().putValue(PreviewProperty.NODE_LABEL_FONT, model.getProperties().getFontValue(PreviewProperty.NODE_LABEL_FONT).deriveFont(8));
        ExportController ec = Lookup.getDefault().lookup(ExportController.class);

        try {
            ec.exportFile(new File("headless_simple.pdf"));
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
    }
    public static void main(String[] args) {
        Gephi gephi = new Gephi();
        gephi.script();
    }
}
