package com.cfc.makingitbettergame.GameClasses.BoardClasses;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.cfc.makingitbettergame.GameClasses.ObjectiveRelatedClasses.Task;

public class TaskSquare extends Square {
    private Task task;
    private Image squareImg;

    public TaskSquare(SquareIconData squareIconData, Image squareImg) {
        super(SquareType.TaskSquare, squareIconData);
        this.squareImg = squareImg;
    }

    public Task getTask() {
        return task;
    }
    public void setTask(Task task) {
        this.task = task;
    }
    public Image getSquareImg() {
        return squareImg;
    }
}
