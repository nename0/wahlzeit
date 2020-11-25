package org.wahlzeit.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BuildingPhotoManager extends PhotoManager {

    /**
     * Override to make clear that a BuildingPhotoFactory is called, not a normal PhotoFactory
     */
    @Override
    protected Photo createObject(ResultSet rset) throws SQLException {
        return BuildingPhotoFactory.getInstance().createPhoto(rset);
    }
    
}
