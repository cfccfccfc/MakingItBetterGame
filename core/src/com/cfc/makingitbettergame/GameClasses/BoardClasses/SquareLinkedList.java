package com.cfc.makingitbettergame.GameClasses.BoardClasses;

import com.cfc.makingitbettergame.GameClasses.PlayerClasses.Player;

public class SquareLinkedList {
    private SquareNode startSquare;
    private int squareCounter;
    private Boolean lastAndFirstLinked = false;

    public SquareLinkedList() {
        this.startSquare = null;
    }

    public SquareNode getStartSquare() {
        return startSquare;
    }
    public int getSquareCounter() {
        return squareCounter;
    }
    public Boolean getLastAndFirstLinked() {
        return lastAndFirstLinked;
    }
    public void addSquare(Square square) {
        if (!lastAndFirstLinked) {
            SquareNode newSquareNode = new SquareNode(square);
            if (startSquare == null && square instanceof StartSquare) {
                startSquare = newSquareNode;
                squareCounter++;
            }
            if (startSquare != null && !(square instanceof StartSquare)) {
                SquareNode current = startSquare;
                while (current.getNextSquare() != null) {
                    current = current.getNextSquare();
                }
                current.setNextSquare(newSquareNode);
                newSquareNode.setPreviousSquare(current);
                squareCounter++;
            }
        }
    }
    public void linkLastAndFirst() {
        SquareNode lastSquare = startSquare;

        for (int i = 1; i < squareCounter; i++)
            lastSquare = lastSquare.getNextSquare();

        lastSquare.setNextSquare(startSquare);
        startSquare.setPreviousSquare(lastSquare);
        lastAndFirstLinked = true;
    }
    public SquareNode moveForward(Player player) {
        return player.getPlayerBoardPosition().getNextSquare();
    }
    public SquareNode moveBackwards(Player player)  {
        return player.getPlayerBoardPosition().getPreviousSquare();
    }
}
