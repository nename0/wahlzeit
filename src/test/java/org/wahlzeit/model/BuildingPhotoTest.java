package org.wahlzeit.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class BuildingPhotoTest {
    @Test
    public void testLocationGetterSetter() {
        BuildingPhoto p = new BuildingPhoto();

        assertEquals(0, p.getDateBuilt());

        p.setDateBuilt(1234);

        assertEquals(1234, p.getDateBuilt());

        assertTrue(p.isDirty());
    }
}
