package org.example.parking.model;

/**
 * Represents a Van, which may occupy multiple spots if necessary.
 */
public class Van extends Vehicle {

    public Van(String plateNumber) {
        super(plateNumber);
    }

    @Override
    public int getRequiredCarSpots() {
        // A van occupies 3 car spots if no big spot is available
        return 3;
    }

    @Override
    public boolean canParkOnMotoSpot() {
        // Vans cannot park on moto spots
        return false;
    }

    @Override
    public boolean canParkOnBigSpot() {
        // Vans can park on a big spot
        return true;
    }

    @Override
    public boolean canParkOnCarSpot() {
        // Vans can use car spots (they need 3 in total)
        return true;
    }
}
