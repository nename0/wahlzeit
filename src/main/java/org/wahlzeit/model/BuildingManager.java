package org.wahlzeit.model;

import org.wahlzeit.services.ObjectManager;
import org.wahlzeit.utils.PatternInstance;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@PatternInstance(
        patternName = "Singleton",
        participants = {}
)
public class BuildingManager extends ObjectManager {

    private static final BuildingManager instance = new BuildingManager();

    private final Map<String, BuildingType> buildingTypeMapTypeMap = new ConcurrentHashMap<>();

    protected BuildingManager() {
    }

    public static BuildingManager getInstance() {
        return instance;
    }

    @Override
    protected Building createObject(ResultSet rset) throws SQLException {
        return new Building(this, rset);
    }

    public Building getBuilding(String buildingName, String typeName) {
        return getBuilding(buildingName, getBuildingType(typeName));
    }

    public Building getBuilding(String buildingName, BuildingType type) {
        return  new Building(type, buildingName);
    }

    protected BuildingType getBuildingType(String type) {
        return buildingTypeMapTypeMap.computeIfAbsent(type, BuildingType::new);
    }
}
