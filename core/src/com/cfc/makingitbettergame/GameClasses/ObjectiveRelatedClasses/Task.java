package com.cfc.makingitbettergame.GameClasses.ObjectiveRelatedClasses;

import com.cfc.makingitbettergame.GameClasses.Miscellaneous.Resources;
import com.cfc.makingitbettergame.GameClasses.PlayerClasses.Player;

import java.util.HashMap;

public class Task {
    private String description;
    private String name;
    private Boolean Collected = false;
    private Boolean assignedToSquare = false;
    private int moraleCost;
    private HashMap<Player.PlayerExpertise, Integer> playerExpertiseBenefactorsHashMap;
    private Resources resourcesCost;
    private Boolean canBeTransferred = true;
    private int taskNumber;

    public Task (int taskNumber, String name, String description, int moraleCost, HashMap<Player.PlayerExpertise, Integer> playerExpertiseBenefactorsHashMap, Resources resourcesCost) {
        this.description = description;
        this.moraleCost = moraleCost;
        this.resourcesCost = resourcesCost;
        this.name = name;
        this.taskNumber = taskNumber;

        this.playerExpertiseBenefactorsHashMap = new HashMap<>();

        if (!playerExpertiseBenefactorsHashMap.isEmpty())
            this.playerExpertiseBenefactorsHashMap.putAll(playerExpertiseBenefactorsHashMap);
    }

    public String getDescription() {
        return description;
    }
    public int getTaskNumber() { return taskNumber; }
    public int getMoraleCost() {
        return moraleCost;
    }
    public Boolean getCollected() {
        return Collected;
    }
    public Boolean getAssignedToSquare() {
        return assignedToSquare;
    }
    public String getName() { return name; }
    public Boolean getCanBeTransferred() { return canBeTransferred; }
    public void hasBeenTransferred() { canBeTransferred = false; }
    public HashMap<Player.PlayerExpertise, Integer> getPlayerExpertiseBenefactorsHashMap() {
        return playerExpertiseBenefactorsHashMap;
    }
    public void isCollected() {
        Collected = true;
    }
    public void isAssignedToSquare() {
        assignedToSquare = true;
    }
    public Resources getResourcesCost() {
        return resourcesCost;
    }
}
