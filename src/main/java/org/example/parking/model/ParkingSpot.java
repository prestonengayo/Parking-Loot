package org.example.parking.model;

/**
 * Abstract class representing a generic parking spot.
 * Subclasses (MotoSpot, CarSpot, BigSpot) will specify
 * which vehicles can park here.
 */
public abstract class ParkingSpot {
    private final String id;
    private boolean isOccupied = false;

    protected ParkingSpot(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void occupy() {
        this.isOccupied = true;
    }

    public void free() {
        this.isOccupied = false;
    }

    /**
     * Checks if the given vehicle can fit into this spot,
     * considering the spot type and whether it's occupied.
     */
    public abstract boolean canFitVehicle(Vehicle vehicle);
}
