package org.wahlzeit.model;

import org.wahlzeit.services.DataObject;
import org.wahlzeit.services.SysLog;
import org.wahlzeit.utils.Preconditions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

/*
 * Client-Service collaboration:
 * The role "Client" binds to BuildingPhoto class
 * The role "Service" binds to Building class
 * 
 * Type Object collaboration:
 * The role "Base Object" binds to Building class
 * The role "Type Object" binds to BuildingType class
 * 
 * Manager collaboration:
 * The role "Manager" binds to BuildingManager class
 * The role "Element" binds to Building class
 */

/*
// collaborations using pseudo syntax:

public collaboration ClientService {
    public role Client { ... }
    public role Service { ... }
}
public collaboration TypeObject {
    public role TypeObject {
        public TypeObject getSuperType();
        public boolean isTypeOfInstance(BaseObject instance);
    }
    public role BaseObject {
       public TypeObject getType();
    }
}
public collaboration Manager {
    public role Manager {
        public Element createObject(ResultSet r);
        public void updateObject(Element object, PreparedStatement stmt);
        public void deleteObject(Element object, PreparedStatement stmt);
    }
    public role Element {
        public void readFrom(ResultSet r);
        public void writeOn(ResultSet r);
   }
}

public class Building extends DataObject binds ClientService.Service,
                                               TypeObject.BaseObject,
                                               Manager.Element { 
    ... 
}
public class BuildingPhoto extends Photo binds ClientService.Client { ... }
public class BuildingType binds TypeObject.TypeObject { ... }
public class BuildingManager extends ObjectManager binds Manager.Manager { ... }
 */


/*
 * method calls that lead to new object:
 * BuildingManager.createObject()
 * Building.<init>()
 * BuildingManager.getBuildingType()
 * Building.readFrom()
 */
public class Building extends DataObject {
    public static final Calendar DATE_FUTURE = Calendar.getInstance();

    static {
        DATE_FUTURE.set(2030, Calendar.JANUARY, 1);
    }
    
    
    public final BuildingType buildingType;

    protected String name;
    
    protected long dateBuilt = 0;

    Building(BuildingManager buildingManager, ResultSet rset) throws SQLException {
        String typeName = rset.getString("building_type");
        if (typeName == null) 
            typeName = "Default";
        this.buildingType = buildingManager.getBuildingType(typeName);
        readFrom(rset);
    }

    Building(BuildingType buildingType, String name) {
        this.buildingType = buildingType;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        assertIsValidBuildingName(name);
        incWriteCount();
        this.name = name;
    }

    public long getDateBuilt() {
        return dateBuilt;
    }

    public void setDateBuilt(long dateBuilt) {
        assertBuiltDate(dateBuilt);
        incWriteCount();
        this.dateBuilt = dateBuilt;
    }

    @Override
    public void readFrom(ResultSet rset) throws SQLException {
        try {
            setName(rset.getString("building_name"));
        } catch (IllegalArgumentException e) {
            SysLog.logSysError("Invalid building_name in database: " + e.getMessage() + ". Setting name to default value");
            setName("Default");
        }
        try {
            setDateBuilt(rset.getLong("date_built"));
        } catch (IllegalArgumentException e) {
            SysLog.logSysError("Invalid dateBuilt in database: " + e.getMessage() + ". Setting dateBuilt to default value");
            setDateBuilt(0); // 1970
        }
    }

    @Override
    public void writeOn(ResultSet rset) throws SQLException {
        rset.updateString("building_type", buildingType.name);
        rset.updateString("building_name", name);
        rset.updateLong("date_built", dateBuilt);
    }

    private static void assertIsValidBuildingName(String name) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("Building name can not be empty");
        }
    }
    
    private static void assertBuiltDate(long dateBuilt) {
        if (dateBuilt > DATE_FUTURE.getTimeInMillis()) {
            Preconditions.fail("dateBuilt in Future");
        }
    }

    @Override
    public String getIdAsString() {
        throw new UnsupportedOperationException("Building is not stored in a separate table. So no ids");
    }

    @Override
    public void writeId(PreparedStatement stmt, int pos) throws SQLException {
        throw new UnsupportedOperationException("Building is not stored in a separate table. So no ids");
    }
}
