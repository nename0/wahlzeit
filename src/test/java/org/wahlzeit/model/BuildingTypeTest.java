package org.wahlzeit.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class BuildingTypeTest {
    @Test
    public void testAddSubType() {
        BuildingType t = BuildingManager.getInstance().getBuildingType("Schloss");
        BuildingType tSub = BuildingManager.getInstance().getBuildingType("Barockschloss");
        t.addSubType(tSub);
        
        assertEquals(t, tSub.getSuperType());
        assertTrue(t.getSubTypes().contains(tSub));
        
    }

    @Test
    public void testIsTypeOfInstance() {
        BuildingType t = BuildingManager.getInstance().getBuildingType("t");
        BuildingType tSub = BuildingManager.getInstance().getBuildingType("tSub");
        BuildingType tSubSub = BuildingManager.getInstance().getBuildingType("tSubSub");
        t.addSubType(tSub);
        tSub.addSubType(tSubSub);
        Building inst_t = BuildingManager.getInstance().getBuilding("inst_t", t);
        Building inst_tSub = BuildingManager.getInstance().getBuilding("inst_tSub", tSub);
        Building inst_tSubSub = BuildingManager.getInstance().getBuilding("inst_tSubSub", tSubSub);
        
        assertTrue(t.isTypeOfInstance(inst_t));
        assertTrue(t.isTypeOfInstance(inst_tSub));
        assertTrue(t.isTypeOfInstance(inst_tSubSub));

        assertFalse(tSub.isTypeOfInstance(inst_t));
        assertTrue(tSub.isTypeOfInstance(inst_tSub));
        assertTrue(tSub.isTypeOfInstance(inst_tSubSub));

        assertFalse(tSubSub.isTypeOfInstance(inst_t));
        assertFalse(tSubSub.isTypeOfInstance(inst_tSub));
        assertTrue(tSubSub.isTypeOfInstance(inst_tSubSub));
    }

    @Test
    public void testIsSubType() {
        BuildingType t = BuildingManager.getInstance().getBuildingType("t");
        BuildingType tSub = BuildingManager.getInstance().getBuildingType("tSub");
        BuildingType tSubSub = BuildingManager.getInstance().getBuildingType("tSubSub");
        t.addSubType(tSub);
        tSub.addSubType(tSubSub);

        assertFalse(t.isSubType(t));
        assertTrue(t.isSubType(tSub));
        assertTrue(t.isSubType(tSubSub));

        assertFalse(tSub.isSubType(t));
        assertFalse(tSub.isSubType(tSub));
        assertTrue(tSub.isSubType(tSubSub));

        assertFalse(tSubSub.isSubType(t));
        assertFalse(tSubSub.isSubType(tSub));
        assertFalse(tSubSub.isSubType(tSubSub));
    }
}
