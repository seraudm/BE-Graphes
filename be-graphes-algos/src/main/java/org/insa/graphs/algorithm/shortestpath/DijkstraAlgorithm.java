package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Collections;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {

        // retrieve data from the input problem (getInputData() is inherited from the
        // parent class ShortestPathAlgorithm)
        final ShortestPathData data = getInputData();

        // variable that will contain the solution of the shortest path problem
        ShortestPathSolution solution = null;


        Graph graph = data.getGraph();
        final int nbNodes = graph.size();
        
        // Notify observers about the first event (origin processed).
        notifyOriginProcessed(data.getOrigin());

        // Initialize array of labels
        Label[] labelArray = new Label[nbNodes];
        
        for (Node node : graph.getNodes()){
            labelArray[node.getId()] = new Label(node, Double.POSITIVE_INFINITY, null);
        }
        
        Label labelDestination = labelArray[data.getDestination().getId()];

        labelArray[data.getOrigin().getId()].setCost(0);

        // Initialize heap of labels.
        BinaryHeap<Label> labelHeap = new BinaryHeap<Label>();

        for (Label label : labelArray){
            labelHeap.insert(label);
        }

        while (!labelDestination.getMark() && !labelHeap.isEmpty()){
            Label labelMin = labelHeap.deleteMin();

            //If the next node to be marked has an inifinite cost, there is no solution.
            if (Double.isInfinite(labelMin.getRealisedCost())){     
                return new ShortestPathSolution(data, Status.INFEASIBLE);
            }

            for (Arc arc: labelMin.getCurrentNode().getSuccessors()){
                
                // Small test to check allowed roads...
                if (!data.isAllowed(arc)) {
                    continue;
                }

                Label destinationOfArc = labelArray[arc.getDestination().getId()];

                // Retrieve weight of the arc.
                double w = data.getCost(arc);
                double oldDistance = destinationOfArc.getRealisedCost();
                double newDistance = labelMin.getRealisedCost() + w;

                if (Double.isInfinite(oldDistance)
                            && Double.isFinite(newDistance)) {
                        notifyNodeReached(arc.getDestination());
                    }

                // Check if new distances would be better, if so update...
                if (newDistance < oldDistance) {
                    destinationOfArc.setCost(newDistance);
                    destinationOfArc.setDaddy(arc);
                    
                    labelHeap.remove(destinationOfArc);
                    labelHeap.insert(destinationOfArc);
                }
            }

            labelMin.setMark(true);
        }
                
        
        // The destination has been found, notify the observers.
        notifyDestinationReached(data.getDestination());

         // Create the path from the array of predecessors...
         ArrayList<Arc> arcs = new ArrayList<>();
         Arc arc = labelArray[data.getDestination().getId()].getDaddy();
         while (arc != null) {
             arcs.add(arc);
             arc = labelArray[arc.getOrigin().getId()].getDaddy();
         }

         // Reverse the path...
         Collections.reverse(arcs);

         // Create the final solution.
         solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));


        // when the algorithm terminates, return the solution that has been found
        return solution;
    }

}
