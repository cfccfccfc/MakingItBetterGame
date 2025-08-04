package com.cfc.makingitbettergame.GameClasses.ObjectiveRelatedClasses;

import com.cfc.makingitbettergame.GameClasses.Miscellaneous.Resources;

import java.util.ArrayList;
import java.util.List;

public class Event {
    public enum EventType {
        CommunityVolunteerDay,
        GovernmentGrant,
        DonationOfMaterials,
        LocalBusinessSponsorships,
        VillageFair,
        MediaCoverage,
        TechnicalTrainingWorkshop,
        RecognitionByHealthOrganizations,
        InnovativeWaterSavingTechniquesWorkshop,

        Flood,
        Drought,
        WorkerInjury,
        SupplyShortage,
        EquipmentBroken,
        SupplyDelay,
        WorkersHoliday,
        LowAwarenessOpposition,
        PermitIssues;
    }

    private EventType eventType;
    private List<Resources> resourcesChangeValues;
    private Boolean assignedToSquare = false;

    public Event(EventType eventType, List<Resources> resourcesChangeValues) {
        this.eventType = eventType;

        this.resourcesChangeValues = new ArrayList<>();
        if (!resourcesChangeValues.isEmpty())
            this.resourcesChangeValues.addAll(resourcesChangeValues);
    }

    public Boolean getAssignedToSquare() { return assignedToSquare; }
    public List<Resources> getResourcesChangeValues() { return resourcesChangeValues; }
    public EventType getEventType() { return eventType; }
    public void isAssignedToSquare() { this.assignedToSquare = true; }
}
