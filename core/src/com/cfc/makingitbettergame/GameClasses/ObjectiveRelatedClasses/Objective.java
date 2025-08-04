package com.cfc.makingitbettergame.GameClasses.ObjectiveRelatedClasses;

import com.cfc.makingitbettergame.GameClasses.Miscellaneous.Resources;

import java.util.ArrayList;
import java.util.List;

public class Objective {
    private String description;
    private List<Task> taskList;
    private Boolean isCompleted = false;

    public Objective(String description, List<Task> tasksComprisingObjectiveList) {
        this.description = description;
        this.taskList = new ArrayList<>();
        if (!tasksComprisingObjectiveList.isEmpty())
            this.taskList.addAll(tasksComprisingObjectiveList);
    }

    public String getDescription() {
        return description;
    }
    public Boolean getAllTasksCollected() {
        for (Task task : taskList)
            if (!task.getCollected())
                return false;

        return true;
    }
    public Boolean getIsCompleted() {
        return isCompleted;
    }
    public void isCompleted() {
        isCompleted = true;
    }
    public List<Task> getTaskList() {
        return taskList;
    }
    public Resources getResourceCost() {
        Resources resourcesTotalCost = new Resources();
        for (Task task : taskList) {
            resourcesTotalCost.modifyAllResources(task.getResourcesCost(), true);
        }
        return resourcesTotalCost;
    }
}
