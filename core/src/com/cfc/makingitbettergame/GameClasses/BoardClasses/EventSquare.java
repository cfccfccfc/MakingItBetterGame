package com.cfc.makingitbettergame.GameClasses.BoardClasses;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.cfc.makingitbettergame.GameClasses.Miscellaneous.Resources;
import com.cfc.makingitbettergame.GameClasses.ObjectiveRelatedClasses.Event;

import java.util.ArrayList;
import java.util.List;

public class EventSquare extends Square {
    private Image squareImg;
    private Event event;

    public EventSquare(SquareType squareType, SquareIconData squareIconData, Image squareImg) {
        super(squareType, squareIconData);

        this.squareImg = squareImg;
    }

    public Image getSquareImg() {
        return squareImg;
    }
    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }
}
