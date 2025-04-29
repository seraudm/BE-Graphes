package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;


public class Label implements Comparable<Label> {
    private Node currentNode;
    private boolean mark;
    private double realisedCost;
    private Arc daddy;


    public Label(Node currentNode, double realisedCost, Arc daddy) {
        currentNode = this.currentNode;
        mark = false;
        realisedCost = this.realisedCost;
        daddy = this.daddy;
    }

    public Node getCurrentNode(){
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

    public void setMark(boolean mark){
        this.mark = mark;
    }

    public void setCost(double cost){
        this.realisedCost = cost;
    }

    public void setDaddy(Arc daddy){
        this.daddy = daddy;
    }

    public int compareTo(Label label) throws NullPointerException{
        if (label == null){
            throw new NullPointerException("Erreur dans le compare to de label, argument null\n");
        }
        
        return Double.compare(realisedCost, label.getRealisedCost());
    }
}
