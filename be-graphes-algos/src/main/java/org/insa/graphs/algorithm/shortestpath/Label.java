package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;


public class Label {
    private Node currentNode;
    private boolean mark;
    private double realisedCost;
    private Arc daddy;


    public Label(Node currentNode, boolean mark, double realisedCost, Arc daddy) {
        currentNode = this.currentNode;
        mark = false;
        realisedCost = this.realisedCost;
        daddy = this.daddy;
    }

    public Node getNode(){
        return currentNode;
    }

    public boolean getMark(){
        return mark;
    }

    public double getRealisedCost(){
        return realisedCost;
    }

    public Arc getDaddy(){
        return daddy;
    }
}
