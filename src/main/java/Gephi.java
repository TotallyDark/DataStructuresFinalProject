import org.gephi.appearance.api.AppearanceController;
import org.gephi.appearance.api.AppearanceModel;
import org.gephi.filters.api.FilterController;
import org.gephi.filters.api.Query;
import org.gephi.filters.api.Range;
import org.gephi.filters.plugin.graph.DegreeRangeBuilder;
import org.gephi.graph.api.*;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.EdgeDirectionDefault;
import org.gephi.io.importer.api.EdgeMergeStrategy;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.types.EdgeColor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.gephi.statistics.plugin.GraphDistance;
import org.openide.util.Lookup;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Gephi {

    public static void main(String[] args) {
        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        Workspace workspace = pc.getCurrentWorkspace();

        //Get models and controllers for this new workspace - will be useful later
        GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel();
        PreviewModel model = Lookup.getDefault().lookup(PreviewController.class).getModel();
        ImportController importController = Lookup.getDefault().lookup(ImportController.class);
        FilterController filterController = Lookup.getDefault().lookup(FilterController.class);
        AppearanceController appearanceController = Lookup.getDefault().lookup(AppearanceController.class);
        AppearanceModel appearanceModel = appearanceController.getModel();

        Container container;
        try {
            File file = new File("FriendData_Nodes.csv");
            container = importController.importFile(file);
            container.getLoader().setEdgeDefault(EdgeDirectionDefault.UNDIRECTED);
            container.getLoader().setAllowAutoNode(true);  //create missing nodes
            container.getLoader().setEdgesMergeStrategy(EdgeMergeStrategy.SUM);
            container.getLoader().setAutoScale(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        //Append imported data to GraphAPI
        importController.process(container, new DefaultProcessor(), workspace);

        //See if graph is well imported
        UndirectedGraph graph = graphModel.getUndirectedGraph();
        System.out.println("Nodes: " + graph.getNodeCount());
        System.out.println("Edges: " + graph.getEdgeCount());

        //Filter
        DegreeRangeBuilder.DegreeRangeFilter degreeFilter = new DegreeRangeBuilder.DegreeRangeFilter();
        degreeFilter.init(graph);
        degreeFilter.setRange(new Range(0, Integer.MAX_VALUE));     //Remove nodes with degree < 30
        Query query = filterController.createQuery(degreeFilter);
        GraphView view = filterController.filter(query);
        graphModel.setVisibleView(view);    //Set the filter result as the visible view

        //See visible graph stats
        UndirectedGraph graphVisible = graphModel.getUndirectedGraphVisible();
        System.out.println("Nodes: " + graphVisible.getNodeCount());
        System.out.println("Edges: " + graphVisible.getEdgeCount());

        //Run YifanHuLayout for 100 passes - The layout always takes the current visible view
        YifanHuLayout layout = new YifanHuLayout(null, new StepDisplacement(1f));
        layout.setGraphModel(graphModel);
        layout.resetPropertiesValues();
        layout.setOptimalDistance(200f);

        layout.initAlgo();
        for (int i = 0; i < 100 && layout.canAlgo(); i++) {
            layout.goAlgo();
        }
        layout.endAlgo();
/*
        //Get Centrality
        GraphDistance distance = new GraphDistance();
        distance.setDirected(true);
        distance.execute(graphModel);

        //Rank color by Degree
        Function degreeRanking = appearanceModel.getNodeFunction(graphModel.defaultColumns()
                .degree(), RankingElementColorTransformer.class);
        RankingElementColorTransformer degreeTransformer = degreeRanking.getTransformer();
        degreeTransformer.setColors(new Color[]{new Color(0xFEF0D9), new Color(0xB30000)});
        degreeTransformer.setColorPositions(new float[]{0f, 1f});
        appearanceController.transform(degreeRanking);

        //Rank size by centrality
        Column centralityColumn = graphModel.getNodeTable().getColumn(GraphDistance.BETWEENNESS);
        Function centralityRanking = appearanceModel.getNodeFunction(centralityColumn, RankingNodeSizeTransformer.class);
        RankingNodeSizeTransformer centralityTransformer = (RankingNodeSizeTransformer) centralityRanking.getTransformer();
        centralityTransformer.setMinSize(3);
        centralityTransformer.setMaxSize(10);
        appearanceController.transform(centralityRanking);
*/
        //Preview
        model.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
        model.getProperties().putValue(PreviewProperty.EDGE_COLOR, new EdgeColor(Color.GRAY));
        model.getProperties().putValue(PreviewProperty.EDGE_THICKNESS, 0.1);
        model.getProperties().putValue(PreviewProperty.NODE_LABEL_FONT, model.getProperties().getFontValue(PreviewProperty.NODE_LABEL_FONT).deriveFont(8));

        //Export
        ExportController ec = Lookup.getDefault().lookup(ExportController.class);
        try {
            ec.exportFile(new File("headless_simple.pdf"));
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
    }
}
