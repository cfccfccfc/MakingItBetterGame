package com.cfc.makingitbettergame.GameClasses.Miscellaneous;

import com.cfc.makingitbettergame.GameClasses.BoardClasses.EventSquare;

import java.util.HashMap;

public class Resources {
    public enum ResourceTypes {
        Money,
        Labour,
        CommunitySupport,
        Reputation;
    }

    private HashMap<ResourceTypes, Integer> resourcesHashMap;

    public Resources() {
        resourcesHashMap = new HashMap<>();
        for (ResourceTypes resourceType : ResourceTypes.values()) {
            resourcesHashMap.put(resourceType, 0);
        }
    }
    public Resources(int moneyQuantity, int labourQuantity, int communitySupportQuantity, int reputationQuantity) {
        resourcesHashMap = new HashMap<>();

        resourcesHashMap.put(ResourceTypes.Money, moneyQuantity);
        resourcesHashMap.put(ResourceTypes.Labour, labourQuantity);
        resourcesHashMap.put(ResourceTypes.CommunitySupport, communitySupportQuantity);
        resourcesHashMap.put(ResourceTypes.Reputation, reputationQuantity);
    }

    public void modifyResource(ResourceTypes resourceType, int quantity, Boolean add) {
        if (add)
            resourcesHashMap.put(resourceType, resourcesHashMap.get(resourceType) + quantity);
        else
            resourcesHashMap.put(resourceType, resourcesHashMap.get(resourceType) - quantity);
    }
    public void modifyAllResources(Resources resourcesModifier, Boolean add) {
        if (add)
            for (ResourceTypes resourceType : ResourceTypes.values())
                resourcesHashMap.put(resourceType, resourcesHashMap.get(resourceType) + resourcesModifier.getAllResourceTypesQuantity().get(resourceType));
        else
            for (ResourceTypes resourceType : ResourceTypes.values())
                resourcesHashMap.put(resourceType, resourcesHashMap.get(resourceType) - resourcesModifier.getAllResourceTypesQuantity().get(resourceType));
    }
    public void applyDiscountToResource(ResourceTypes resourceType, int discountPercentage) {
        resourcesHashMap.put(resourceType, resourcesHashMap.get(resourceType) - resourcesHashMap.get(resourceType) * (100 - discountPercentage) / 100);
    }
    public Resources applyDiscountToAll (int discountPercentage) {
        Resources resourcesWithDiscount = new Resources();

        for (ResourceTypes resourceType : ResourceTypes.values())
            resourcesWithDiscount.getAllResourceTypesQuantity().put(resourceType, resourcesHashMap.get(resourceType) -
                    resourcesHashMap.get(resourceType) * discountPercentage / 100);

        return resourcesWithDiscount;
    }
    public HashMap<ResourceTypes, Integer> getAllResourceTypesQuantity() {
        return resourcesHashMap;
    }
    public Boolean has(Resources resources) {
        for (ResourceTypes resourceType : ResourceTypes.values())
            if (resourcesHashMap.get(resourceType) < resources.getAllResourceTypesQuantity().get(resourceType))
                return false;
        return true;
    }
}
