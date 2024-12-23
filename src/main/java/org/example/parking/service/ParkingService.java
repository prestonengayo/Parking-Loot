package org.example.parking.service;

import org.example.parking.domain.Parking;
import org.example.parking.model.ParkingSpot;
import org.example.parking.model.Van;
import org.example.parking.model.Vehicle;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class contains the business logic for parking/unparking vehicles.
 */
public class ParkingService {

    /**
     * Attempt to park the given vehicle in the provided parking.
     * Returns true if successful, false otherwise.
     */
    public boolean parkVehicle(Parking parking, Vehicle vehicle) {

        // 1) If the vehicle can park on a BigSpot, try that first
        if (vehicle.canParkOnBigSpot()) {
            if (tryParkOnBigSpot(parking.getBigSpots(), vehicle)) {
                return true;
            }
        }

        // 2) If the vehicle is a Moto, try moto spots next
        if (vehicle.canParkOnMotoSpot()) {
            if (tryParkOnSpots(parking.getMotoSpots(), vehicle)) {
                return true;
            }
        }

        // 3) If it's a Van, try occupying 3 car spots
        if (vehicle instanceof Van) {
            return tryParkVanOnCarSpots(parking.getCarSpots(), (Van) vehicle);
        } else {
            // For a Car or a Moto fallback, try car spots
            return tryParkOnSpots(parking.getCarSpots(), vehicle);
        }
    }

    // Helper method to try parking on a big spot
    private boolean tryParkOnBigSpot(List<ParkingSpot> bigSpots, Vehicle vehicle) {
        for (ParkingSpot spot : bigSpots) {
            if (spot.canFitVehicle(vehicle)) {
                spot.occupy();
                return true;
            }
        }
        return false;
    }

    // Generic helper method to try parking on a list of spots (moto or car)
    private boolean tryParkOnSpots(List<ParkingSpot> spots, Vehicle vehicle) {
        for (ParkingSpot spot : spots) {
            if (spot.canFitVehicle(vehicle)) {
                spot.occupy();
                return true;
            }
        }
        return false;
    }

    /**
     * For a Van that requires multiple car spots (default: 3)
     */
    private boolean tryParkVanOnCarSpots(List<ParkingSpot> carSpots, Van van) {
        int required = van.getRequiredCarSpots(); // typically 3 for a Van

        // Collect free car spots that can fit a Van
        List<ParkingSpot> freeCarSpots = carSpots.stream()
                .filter(s -> !s.isOccupied() && s.canFitVehicle(van))
                .collect(Collectors.toList());

        // If there are enough free spots (>= 3), occupy them
        if (freeCarSpots.size() >= required) {
            for (int i = 0; i < required; i++) {
                freeCarSpots.get(i).occupy();
            }
            return true;
        }
        return false;
    }

    /**
     * Method to unpark a vehicle if we had a mapping from spots to vehicles.
     * For simplicity, this example doesn't track which vehicle is on which spot.
     * In a real system, you'd store that info in a Map<Vehicle, List<ParkingSpot>>
     * or store the Vehicle in each ParkingSpot.
     */
    public void unparkVehicle(Vehicle vehicle) {
        // Implementation depends on how you track parked vehicles...
    }
}
