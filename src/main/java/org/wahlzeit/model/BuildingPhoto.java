package org.wahlzeit.model;

import org.wahlzeit.services.SysLog;
import org.wahlzeit.utils.Preconditions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

public class BuildingPhoto extends Photo {
    public static final Calendar DATE_FUTURE = Calendar.getInstance();
    static {
        DATE_FUTURE.set(2030, Calendar.JANUARY, 1);
    }
    
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
        assertBuiltDate(dateBuilt);
        this.dateBuilt = dateBuilt;
        incWriteCount();
    }

    @Override
    public void readFrom(ResultSet rset) throws SQLException {
        super.readFrom(rset);
        try {
        setDateBuilt(rset.getLong("date_built"));}
        catch (IllegalArgumentException e) {
            SysLog.logSysError("Invalid dateBuilt in database: " + e.getMessage() + ". Setting dateBuilt to default value");
            setDateBuilt(0); // 1970
        }
    }

    @Override
    public void writeOn(ResultSet rset) throws SQLException {
        super.writeOn(rset);
        rset.updateLong("date_built", dateBuilt);
    }
    
    private static void assertBuiltDate(long dateBuilt) {
        if (dateBuilt > DATE_FUTURE.getTimeInMillis()) {
            Preconditions.fail("dateBuilt in Future");
        }
    }
}
