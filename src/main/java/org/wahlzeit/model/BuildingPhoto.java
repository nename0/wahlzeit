package org.wahlzeit.model;

import java.sql.ResultSet;
import java.sql.SQLException;

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

    protected long dateBuilt = 0;

    public long getDateBuilt() {
        return dateBuilt;
    }

    public void setDateBuilt(long dateBuilt) {
        this.dateBuilt = dateBuilt;
        incWriteCount();
    }

    @Override
    public void readFrom(ResultSet rset) throws SQLException {
        super.readFrom(rset);
        dateBuilt = rset.getLong("date_built");
    }

    @Override
    public void writeOn(ResultSet rset) throws SQLException {
        super.writeOn(rset);
        rset.updateLong("date_built", dateBuilt);
    }
}
