package com.cfc.makingitbettergame.GameClasses.BoardClasses;

public class SquareIconData {
    private float x;
    private float y;
    private float width;
    private float height;

    public SquareIconData(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }
    public float getWidth() {
        return width;
    }
    public float getHeight() {
        return height;
    }
}
