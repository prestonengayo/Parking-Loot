package org.example.parking.model;

/**
 * Represents a spot intended for cars.
 */
public class CarSpot extends ParkingSpot {

    public CarSpot(String id) {
        super(id);
    }

    @Override
    public boolean canFitVehicle(Vehicle vehicle) {
        // Check if the spot is free and if the vehicle
        // can park on a car spot
        return !isOccupied() && vehicle.canParkOnCarSpot();
    }
}
