package org.wahlzeit.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class BuildingType {

    // the root of the inheritance tree
    public static final BuildingType ROOT = new BuildingType("<ROOT>");

    protected BuildingType superType = ROOT;

    protected Set<BuildingType> subTypes = new HashSet<>();

    public final String name;

    BuildingType(String name) {
        assertIsValidBuildingType(name);
        this.name = name;
    }

    public BuildingType getSuperType() {
        return superType;
    }

    public Set<BuildingType> getSubTypes() {
        return Collections.unmodifiableSet(subTypes);
    }

    public void addSubType(BuildingType buildingType) {
        if (buildingType == null) {
            throw new IllegalArgumentException("buildingType == null");
        }
        buildingType.superType = this;
        subTypes.add(buildingType);
    }

    public boolean isTypeOfInstance(Building instance) {
        if (instance == null) {
            return false; // like "null instanceof Object"
        }
        if (instance.buildingType.equals(this)) {
            return true;
        }
        return isSubType(instance.buildingType);
    }

    public boolean isSubType(BuildingType buildingType) {
        if (buildingType == null) {
            throw new IllegalArgumentException("buildingType == null");
        }
        if (subTypes.contains(buildingType)) {
            return true;
        }
        return subTypes.stream().anyMatch(subType -> subType.isSubType(buildingType));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BuildingType that = (BuildingType) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    private static void assertIsValidBuildingType(String name) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("BuildingType name can not be empty");
        }
    }
}
