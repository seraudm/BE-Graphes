package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Point;

public class LabelStar extends Label{
    private Double estimatedCost;

    public LabelStar(Node currentNode, double realisedCost, Arc daddy, Node destNode){
        super(currentNode, realisedCost, daddy);
        Point currentPoint = currentNode.getPoint();
        Point destPoint = destNode.getPoint();
        this.estimatedCost = currentPoint.distanceTo(destPoint);
    }

    public Double getTotalCost(){
        return this.estimatedCost + super.getRealisedCost();
    }

    public Double getEstimatedCost(){
        return estimatedCost;
    }

}
