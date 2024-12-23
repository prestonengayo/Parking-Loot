package org.example.parking.domain;

import org.example.parking.model.BigSpot;
import org.example.parking.model.CarSpot;
import org.example.parking.model.MotoSpot;
import org.example.parking.model.ParkingSpot;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the overall parking structure, holding
 * lists of different types of spots (moto, car, big).
 */
public class Parking {

    private final List<ParkingSpot> motoSpots;
    private final List<ParkingSpot> carSpots;
    private final List<ParkingSpot> bigSpots;

    /**
     * Constructor that creates the specified number
     * of moto, car, and big spots.
     */
    public Parking(int nbMotoSpots, int nbCarSpots, int nbBigSpots) {
        this.motoSpots = new ArrayList<>();
        this.carSpots = new ArrayList<>();
        this.bigSpots = new ArrayList<>();

        // Populate the lists with the corresponding spot objects
        for (int i = 0; i < nbMotoSpots; i++) {
            motoSpots.add(new MotoSpot("M-" + i));
        }
        for (int i = 0; i < nbCarSpots; i++) {
            carSpots.add(new CarSpot("C-" + i));
        }
        for (int i = 0; i < nbBigSpots; i++) {
            bigSpots.add(new BigSpot("B-" + i));
        }
    }

    // Getters for the lists, in case we need them
    public List<ParkingSpot> getMotoSpots() {
        return motoSpots;
    }

    public List<ParkingSpot> getCarSpots() {
        return carSpots;
    }

    public List<ParkingSpot> getBigSpots() {
        return bigSpots;
    }

    public boolean areMotoSpotsFull() {
        return motoSpots.stream().allMatch(spot -> spot.isOccupied());
    }

    public boolean areCarSpotsFull() {
        return carSpots.stream().allMatch(spot -> spot.isOccupied());
    }

    public boolean areBigSpotsFull() {
        return bigSpots.stream().allMatch(spot -> spot.isOccupied());
    }


    /**
     * Returns the total number of spots in the parking.
     */
    public int getTotalSpots() {
        return motoSpots.size() + carSpots.size() + bigSpots.size();
    }

    /**
     * Returns how many spots (of all kinds) are currently free.
     */
    public int getFreeSpotsCount() {
        long freeMoto = motoSpots.stream().filter(s -> !s.isOccupied()).count();
        long freeCar = carSpots.stream().filter(s -> !s.isOccupied()).count();
        long freeBig = bigSpots.stream().filter(s -> !s.isOccupied()).count();
        return (int) (freeMoto + freeCar + freeBig);
    }

    /**
     * Checks if the parking is fully occupied (no free spots).
     */
    public boolean isFull() {
        return getFreeSpotsCount() == 0;
    }

    /**
     * Checks if the parking is completely empty (all spots are free).
     */
    public boolean isEmpty() {
        return getFreeSpotsCount() == getTotalSpots();
    }
}
