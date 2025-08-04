package com.cfc.makingitbettergame.GameClasses.BoardClasses;

public class SquareNode {
    private Square square;
    private SquareNode nextSquare;
    private SquareNode previousSquare;
    public SquareNode(Square square) {
        this.square = square;
        this.nextSquare = null;
        this.previousSquare = null;
    }
    public Square getSquare() {
        return square;
    }
    public SquareNode getNextSquare() {
        return nextSquare;
    }
    public SquareNode getPreviousSquare() {
        return previousSquare;
    }
    public void setNextSquare(SquareNode nextSquare) {
        this.nextSquare = nextSquare;
    }
    public void setPreviousSquare(SquareNode previousSquare) {
        this.previousSquare = previousSquare;
    }
    public boolean hasNextSquare() {
        if (nextSquare == null)
            return false;
        else
            return true;
    }
    public boolean hasPreviousSquare() {
        if (previousSquare == null)
            return false;
        else
            return true;
    }
    public TaskSquare getTaskSquare() {
        if (square instanceof TaskSquare) {
            return (TaskSquare) square;
        }
        else {
            return null;
        }
    }
    public EventSquare getEventSquare() {
        if (square instanceof EventSquare) {
            return (EventSquare) square;
        }
        else {
            return null;
        }
    }
}
