package org.wahlzeit.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class BuildingManagerTest {
    @Test
    public void testGetBuilding() {
        BuildingManager instance = BuildingManager.getInstance();
        Building b = instance.getBuilding("Schloss Neuschwanstein", "Schloss");
        
        assertEquals("Schloss Neuschwanstein", b.getName());
        assertEquals("Schloss", b.buildingType.name);
        
        assertSame(b.buildingType, instance.getBuildingType("Schloss"));
        assertSame(b.buildingType, instance.getBuilding("Schloss Bellevue", "Schloss").buildingType);
    }
}
