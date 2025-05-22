package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Point;

public class LabelStar extends Label{
    private Double estimatedCost;

    public LabelStar(Node currentNode, double realisedCost, Arc daddy, Node destNode, ShortestPathData data){
        super(currentNode, realisedCost, daddy);
        Point currentPoint = currentNode.getPoint();
        Point destPoint = destNode.getPoint();
        Double distanceToDest = currentPoint.distanceTo(destPoint);
        Mode mode = data.getMode();
        switch (mode) {
            case TIME:
                this.estimatedCost = distanceToDest * 3600.0 / (data.getMaximumSpeed() * 1000.0);
                break;
            case LENGTH:
                this.estimatedCost = distanceToDest;
                break;
            default:
                break;
        }
    }

    public Double getTotalCost(){
        return this.estimatedCost + super.getRealisedCost();
    }

    public Double getEstimatedCost(){
        return estimatedCost;
    }

}
