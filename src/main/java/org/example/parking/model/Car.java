package org.example.parking.model;

/**
 * Represents a standard Car.
 */
public class Car extends Vehicle {

    public Car(String plateNumber) {
        super(plateNumber);
    }

    @Override
    public int getRequiredCarSpots() {
        // A car only needs 1 car spot
        return 1;
    }

    @Override
    public boolean canParkOnMotoSpot() {
        // Cars cannot park on a moto spot
        return false;
    }

    @Override
    public boolean canParkOnBigSpot() {
        // A car can park on a big spot
        return true;
    }

    @Override
    public boolean canParkOnCarSpot() {
        // A car can park on a car spot
        return true;
    }
}
