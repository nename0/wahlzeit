package org.wahlzeit.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BuildingPhotoFactory extends PhotoFactory {
    public static BuildingPhotoFactory getInstance() {
        return (BuildingPhotoFactory) PhotoFactory.getInstance();
    }
    
    @Override
    public BuildingPhoto createPhoto() {
        return new BuildingPhoto();
    }

    @Override
    public BuildingPhoto createPhoto(PhotoId id) {
        return new BuildingPhoto(id);
    }

    @Override
    public BuildingPhoto createPhoto(ResultSet rs) throws SQLException {
        return new BuildingPhoto(rs);
    }
}
