package org.example.parking.model;

/**
 * Represents a large spot intended for larger vehicles (e.g., vans, or cars, motos).
 */
public class BigSpot extends ParkingSpot {

    public BigSpot(String id) {
        super(id);
    }

    @Override
    public boolean canFitVehicle(Vehicle vehicle) {
        // Check if the spot is free and if the vehicle
        // can park on a big spot
        return !isOccupied() && vehicle.canParkOnBigSpot();
    }
}
