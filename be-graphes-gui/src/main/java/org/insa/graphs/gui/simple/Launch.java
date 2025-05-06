package org.insa.graphs.gui.simple;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.insa.graphs.algorithm.ArcInspector;
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

    public static boolean testShortestPathAlgo(int idOrigine, int idDest, String mapName, ArcInspector arcInspector){
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
        dijkstraAlgorithm = new DijkstraAlgorithm(data);
        
        BellmanFordAlgorithm bellmanFordAlgorithm;
        bellmanFordAlgorithm = new BellmanFordAlgorithm(data);

        ShortestPathSolution solutionDijkstra, solutionBellman;
        solutionDijkstra = dijkstraAlgorithm.run();
        solutionBellman = bellmanFordAlgorithm.run();

        return true;
    }

    public static void main(String[] args) throws Exception {

        // visit these directory to see the list of available files on commetud.
        final String mapName =
                "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/insa.mapgr";
        final String pathName =
                "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Paths/path_fr31insa_rangueil_r2.path";

        final Graph graph;
        final Path path;

        // create a graph reader
        try (final GraphReader reader = new BinaryGraphReader(new DataInputStream(
                new BufferedInputStream(new FileInputStream(mapName))))) {
            graph = reader.read();
        }

        // create the drawing
        final Drawing drawing = createDrawing();

        drawing.drawGraph(graph);

        //create a path reader

        try (final PathReader pathReader = new BinaryPathReader(new DataInputStream(
            new BufferedInputStream(new FileInputStream(pathName))))) {
            
            //read the path
            path = pathReader.readPath(graph);
        }

        //draw the path on the drawing
        drawing.drawPath(path);
    }

}
