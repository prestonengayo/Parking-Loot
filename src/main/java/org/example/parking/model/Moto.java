package org.example.parking.model;

/**
 * Represents a Motorcycle (Moto).
 */
public class Moto extends Vehicle {

    public Moto(String plateNumber) {
        super(plateNumber);
    }

    @Override
    public int getRequiredCarSpots() {
        // A motorcycle would only use 1 car spot if needed
        return 1;
    }

    @Override
    public boolean canParkOnMotoSpot() {
        // Motos can park on a moto spot
        return true;
    }

    @Override
    public boolean canParkOnBigSpot() {
        // A moto can park on a big spot
        return true;
    }

    @Override
    public boolean canParkOnCarSpot() {
        // A moto can also park on a car spot
        return true;
    }
}
