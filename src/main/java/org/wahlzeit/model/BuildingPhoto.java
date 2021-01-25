package org.wahlzeit.model;

import org.wahlzeit.utils.PatternInstance;

import java.sql.ResultSet;
import java.sql.SQLException;

@PatternInstance(
        patternName = "Abstract Factory",
        participants = "ConcreteProduct")
/*
 * method calls that lead to new object:
 * ObjectManager.readObject()
 * BuildingPhotoManager.createObject()
 * BuildingPhotoFactory.createPhoto()
 * BuildingPhoto.<init>()
 * Photo.<init>()
 * BuildingPhoto.readFrom()
 * Photo.readFrom()
 * Location.readFrom()
 * CartesianCoordinate/SphericCoordinate.getFromSQL()
 */
public class BuildingPhoto extends Photo {
    public BuildingPhoto() {
        super();
    }

    public BuildingPhoto(PhotoId myId) {
        super(myId);
    }

    public BuildingPhoto(ResultSet rset) throws SQLException {
        super(rset);
    }

    protected Building building;

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        incWriteCount();
        this.building = building;
    }

    @Override
    public boolean isDirty() {
        boolean selfDirty = super.isDirty();
        boolean buildingDirty = building != null && building.isDirty();

        return selfDirty || buildingDirty;
    }

    @Override
    public void readFrom(ResultSet rset) throws SQLException {
        super.readFrom(rset);
        building = BuildingManager.getInstance().createObject(rset);
    }

    @Override
    public void writeOn(ResultSet rset) throws SQLException {
        super.writeOn(rset);
        if (building != null)
            building.writeOn(rset);
    }
}
