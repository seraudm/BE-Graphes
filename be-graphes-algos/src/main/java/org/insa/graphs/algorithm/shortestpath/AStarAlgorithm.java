package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }

    protected Label getLabelUsed(Node currentNode, double realisedCost, Arc daddy, ShortestPathData data){
        return new LabelStar(currentNode, realisedCost, daddy, data);
    }

}
