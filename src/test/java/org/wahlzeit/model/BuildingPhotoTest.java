package org.wahlzeit.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class BuildingPhotoTest {
    @Test
    public void testBuildingGetterSetter() {
        BuildingPhoto p = new BuildingPhoto();
        Building b = BuildingManager.getInstance().getBuilding("Schloss Neuschwanstein", "Schloss");

        assertNull(p.getBuilding());

        p.setBuilding(b);

        assertEquals(b, p.getBuilding());

        assertTrue(p.isDirty());
    }
}
