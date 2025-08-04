package com.cfc.makingitbettergame.GameClasses.BoardClasses;

public class Square {
    public enum SquareType {
        StartSquare,
        TaskSquare,
        PositiveEventSquare,
        NegativeEventSquare
    }

    private String description;
    private SquareType squareType;
    private SquareIconData squareIconData;

    protected Square(SquareType squareType, String description, SquareIconData squareIconData) {
        this.squareType = squareType;
        this.description = description;
        this.squareIconData = squareIconData;
    }
    protected Square(SquareType squareType, SquareIconData squareIconData) {
        this.squareType = squareType;
        this.squareIconData = squareIconData;
    }

    public SquareType getSquareType() {
        return squareType;
    }
    public String getSquareDescription() {
        return description;
    }
    public SquareIconData getSquareCoordinates() {
        return squareIconData;
    }
    public void setDescription(String description) { this.description = description; }
}
