package org.insa.graphs.gui.simple;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.gui.drawing.Drawing;
import org.insa.graphs.gui.drawing.components.BasicDrawing;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.BinaryPathReader;
import org.insa.graphs.model.io.GraphReader;
import org.insa.graphs.model.io.PathReader;

public class Launch {

    /**
     * Create a new Drawing inside a JFrame an return it.
     *
     * @return The created drawing.
     * @throws Exception if something wrong happens when creating the graph.
     */
    public static Drawing createDrawing() throws Exception {
        BasicDrawing basicDrawing = new BasicDrawing();
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("BE Graphes - Launch");
                frame.setLayout(new BorderLayout());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                frame.setSize(new Dimension(800, 600));
                frame.setContentPane(basicDrawing);
                frame.validate();
            }
        });
        return basicDrawing;
    }

    public static boolean testShortestPathAlgo(int idOrigine, int idDest, String mapName, ArcInspector arcInspector, boolean isAStar) throws Exception {
        Graph graph;
        Node nodeOrigine, nodeDest;

        // create a graph reader
        try (final GraphReader reader = new BinaryGraphReader(new DataInputStream(
                new BufferedInputStream(new FileInputStream(mapName))))) {
            graph = reader.read();
        }
        nodeOrigine = graph.get(idOrigine);
        nodeDest = graph.get(idDest);

        ShortestPathData data = new ShortestPathData(graph, nodeOrigine, nodeDest, arcInspector);

        DijkstraAlgorithm dijkstraAlgorithm;
        if (isAStar){
            dijkstraAlgorithm = new AStarAlgorithm(data);
        } else {
            dijkstraAlgorithm = new DijkstraAlgorithm(data);
        }
        
        BellmanFordAlgorithm bellmanFordAlgorithm;
        bellmanFordAlgorithm = new BellmanFordAlgorithm(data);
        
        ShortestPathSolution solutionDijkstra, solutionBellman;
        solutionDijkstra = dijkstraAlgorithm.run();
        solutionBellman = bellmanFordAlgorithm.run();

        if(!solutionBellman.isFeasible() && !solutionDijkstra.isFeasible()){
            return true;
        }

        if(solutionBellman.isFeasible() != solutionDijkstra.isFeasible()){
            System.err.println("Bellman"+solutionBellman.isFeasible()+" Dijkstra: "+solutionDijkstra.isFeasible());
            return false;
        }

        Path pathDijkstra = solutionDijkstra.getPath();
        Path pathBellman = solutionBellman.getPath();



        if(pathDijkstra.getOrigin() != nodeOrigine){
            return false;
        }

        if(!pathDijkstra.getArcs().isEmpty() && pathDijkstra.getDestination() != nodeDest){
            return false;
        }
        
        if(!pathDijkstra.isValid()){
            return false;
        }

        if ((data.getCost(pathDijkstra) - data.getCost(pathBellman)) > 1/1000){
            return false;
        }
        

        return true;
    }

    public static void myAssert(boolean b) throws AssertionError{
        if (!b){
            throw new AssertionError();
        }
        System.out.println("OK");
    }

    public static void main(String[] args) throws Exception {

        // final String mapNameMP = "/home/verdeil/Bureau/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/midi-pyrenees.mapgr";
        final String mapNameMP = "C:\\Users\\maels\\OneDrive\\Documents\\INSA\\BE_Graphes\\BE-Graphes-Git-Folder\\Maps\\toulouse\\europe\\france\\midi-pyrenees.mapgr";
        List<ArcInspector> listArcInspector = ArcInspectorFactory.getAllFilters();
        ArcInspector shortestNoFilter = listArcInspector.get(0);
        ArcInspector shortestOnlyCars = listArcInspector.get(1);
        ArcInspector fastestNoFilter = listArcInspector.get(2);
        ArcInspector fastestPedestrian = listArcInspector.get(3);
        ArcInspector fastestOnlyCars = listArcInspector.get(4);

        boolean isAStar = false;

        System.out.println("Début des tests");
        myAssert(testShortestPathAlgo(404734, 281104, mapNameMP, shortestNoFilter, isAStar));
        myAssert(testShortestPathAlgo(13120, 120842, mapNameMP, fastestOnlyCars, isAStar));
        myAssert(testShortestPathAlgo(13120, 13120, mapNameMP, fastestNoFilter, isAStar));
        myAssert(testShortestPathAlgo(13120, 120842, mapNameMP, fastestPedestrian, isAStar));
        myAssert(testShortestPathAlgo(13120, 67032, mapNameMP, shortestNoFilter, isAStar));
        myAssert(testShortestPathAlgo(13120, 67032, mapNameMP, shortestOnlyCars, isAStar));
        System.out.println("Fin des tests: OK");

        // // visit these directory to see the list of available files on commetud.
        // final String mapName =
        //         "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/insa.mapgr";
        // final String pathName =
        //         "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Paths/path_fr31insa_rangueil_r2.path";

        // final Graph graph;
        // final Path path;

        // // create a graph reader
        // try (final GraphReader reader = new BinaryGraphReader(new DataInputStream(
        //         new BufferedInputStream(new FileInputStream(mapName))))) {
        //     graph = reader.read();
        // }

        // // create the drawing
        // final Drawing drawing = createDrawing();

        // drawing.drawGraph(graph);

        // //create a path reader

        // try (final PathReader pathReader = new BinaryPathReader(new DataInputStream(
        //     new BufferedInputStream(new FileInputStream(pathName))))) {
            
        //     //read the path
        //     path = pathReader.readPath(graph);
        // }

        // //draw the path on the drawing
        // drawing.drawPath(path);
    }

}
