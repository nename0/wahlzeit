package org.wahlzeit.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BuildingPhotoFactory extends PhotoFactory {
    @Override
    public Photo createPhoto() {
        return new BuildingPhoto();
    }

    @Override
    public Photo createPhoto(PhotoId id) {
        return new BuildingPhoto(id);
    }

    @Override
    public Photo createPhoto(ResultSet rs) throws SQLException {
        return new BuildingPhoto(rs);
    }
}
