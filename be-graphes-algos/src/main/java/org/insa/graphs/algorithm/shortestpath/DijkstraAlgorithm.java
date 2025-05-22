package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Collections;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.algorithm.utils.ElementNotFoundException;
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

        if (data.getDestination() == data.getOrigin()){
            return new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, data.getOrigin()));
        }

        // Initialize array of labels
        Label[] labelArray = new Label[nbNodes];
        
        for (Node node : graph.getNodes()){
            if (this instanceof AStarAlgorithm){
                labelArray[node.getId()] = new LabelStar(node, Double.POSITIVE_INFINITY, null, data.getDestination(), data);
            } else {
                labelArray[node.getId()] = new Label(node, Double.POSITIVE_INFINITY, null);
            }
        }
        
        Label labelDestination = labelArray[data.getDestination().getId()];
        Label labelOrigin = labelArray[data.getOrigin().getId()];


        labelOrigin.setCost(0);

        // Initialize heap of labels.
        BinaryHeap<Label> labelHeap = new BinaryHeap<Label>();

        labelHeap.insert(labelOrigin);

        while (!labelDestination.getMark() && !labelHeap.isEmpty()){
            Label labelMin = labelHeap.deleteMin();

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
                    
                    try {
                        labelHeap.remove(destinationOfArc);
                    } catch (ElementNotFoundException e) {
                        /* We just catch the exception so it doesn't stop the execution and do nothing with it. 
                        It's not a problem, that means the element isn't in the heap yet, the next instruction will insert it*/
                    }
                    labelHeap.insert(destinationOfArc);
                }
            }

            labelMin.setMark(true);
        }
        
        double costPath = 0;
        
        // The destination has been found, notify the observers.
        notifyDestinationReached(data.getDestination());

         // Create the path from the array of predecessors...
         ArrayList<Arc> arcs = new ArrayList<>();
         Arc arc = labelArray[data.getDestination().getId()].getDaddy();

         // Destination has no predecessor, the solution is infeasible...
         if (arc== null){
            solution =  new ShortestPathSolution(data, Status.INFEASIBLE);
         } else {
             while (arc != null) {
                 arcs.add(arc);
                 costPath += data.getCost(arc);
                 arc = labelArray[arc.getOrigin().getId()].getDaddy();
             }
    
             // Reverse the path...
             Collections.reverse(arcs);
    
             assert ((labelDestination.getRealisedCost() - costPath)<= 1/1000);
    
             // Create the final solution.
             solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
         }


        // when the algorithm terminates, return the solution that has been found
        return solution;
    }

}
