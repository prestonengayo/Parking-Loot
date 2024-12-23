package org.example.parking.model;

/**
 * Abstract class representing a generic vehicle.
 * Each specific vehicle type (Moto, Car, Van, etc.)
 * will extend this class.
 */
public abstract class Vehicle {
    private String plateNumber;

    protected Vehicle(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    // Indicates how many car-size spots are required if this vehicle
    // has to park in car spots (e.g., a Van might need 3).
    public abstract int getRequiredCarSpots();

    // Determines if the vehicle can park on a MotoSpot
    public abstract boolean canParkOnMotoSpot();

    // Determines if the vehicle can park on a BigSpot
    public abstract boolean canParkOnBigSpot();

    // Determines if the vehicle can park on a CarSpot
    public abstract boolean canParkOnCarSpot();
}
