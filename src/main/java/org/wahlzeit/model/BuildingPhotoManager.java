package org.wahlzeit.model;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BuildingPhotoManager extends PhotoManager {

    public static BuildingPhotoManager getInstance() {
        return (BuildingPhotoManager) PhotoManager.getInstance();
    }
    
    /**
     * Override to make clear that a BuildingPhotoFactory is called, not a normal PhotoFactory
     */
    @Override
    protected BuildingPhoto createObject(ResultSet rset) throws SQLException {
        return BuildingPhotoFactory.getInstance().createPhoto(rset);
    }

    @Override
    public BuildingPhoto createPhoto(File file) throws Exception {
        return (BuildingPhoto) super.createPhoto(file);
    }
    
}
