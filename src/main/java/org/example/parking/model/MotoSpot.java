package org.example.parking.model;

/**
 * Represents a spot intended for motorcycles.
 */
public class MotoSpot extends ParkingSpot {

    public MotoSpot(String id) {
        super(id);
    }

    @Override
    public boolean canFitVehicle(Vehicle vehicle) {
        // Check if the spot is free and if the vehicle is allowed
        // to park on a moto spot (e.g., a moto can, a car cannot).
        return !isOccupied() && vehicle.canParkOnMotoSpot();
    }
}
