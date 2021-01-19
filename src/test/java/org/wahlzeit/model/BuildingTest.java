package org.wahlzeit.model;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

public class BuildingTest {
    @Test
    public void testDateBuiltGetterSetter() {
        Building b = BuildingManager.getInstance().getBuilding("Schloss Neuschwanstein", "Schloss");
        Calendar calendar = Calendar.getInstance();
        calendar.set(1869, Calendar.FEBRUARY, 1);
        long date = calendar.getTimeInMillis();

        assertEquals(0, b.getDateBuilt());

        b.setDateBuilt(date);

        assertEquals(date, b.getDateBuilt());

        assertTrue(b.isDirty());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDate() {
        Building b = BuildingManager.getInstance().getBuilding("Schloss Neuschwanstein", "Schloss");
        b.setDateBuilt(Long.MAX_VALUE);
    } 

    @Test
    public void testNameGetterSetter() {
        Building b = BuildingManager.getInstance().getBuilding("Schloss Neuschwanstein", "Schloss");

        assertEquals("Schloss Neuschwanstein", b.getName());

        b.setName("Schloss Neuschwanstein (Märchenschloss)");

        assertEquals("Schloss Neuschwanstein (Märchenschloss)", b.getName());

        assertTrue(b.isDirty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidName() {
        Building b = BuildingManager.getInstance().getBuilding("Schloss Neuschwanstein", "Schloss");
        b.setName("");
    }
}
