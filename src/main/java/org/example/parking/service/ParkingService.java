package org.example.parking.service;

import org.example.parking.domain.Parking;
import org.example.parking.model.Car;
import org.example.parking.model.Moto;
import org.example.parking.model.ParkingSpot;
import org.example.parking.model.Van;
import org.example.parking.model.Vehicle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class contains the business logic for parking/unparking vehicles,
 * using an internal Map<Vehicle, List<ParkingSpot>> to track which spots
 * each vehicle occupies.
 */
public class ParkingService {

    // Map linking each parked Vehicle to the list of ParkingSpots it occupies
    private Map<Vehicle, List<ParkingSpot>> vehicleToSpots = new HashMap<>();

    /**
     * Attempts to park the given vehicle in the provided parking.
     * Returns true if successful, false otherwise.
     */
    public boolean parkVehicle(Parking parking, Vehicle vehicle) {

        // If it's a Moto
        if (vehicle instanceof Moto) {
            // 1) Try moto spots
            List<ParkingSpot> used = tryParkOnSpots(parking.getMotoSpots(), vehicle);
            if (used != null) {
                vehicleToSpots.put(vehicle, used);
                return true;
            }
            // 2) Then fallback on car spots
            used = tryParkOnSpots(parking.getCarSpots(), vehicle);
            if (used != null) {
                vehicleToSpots.put(vehicle, used);
                return true;
            }
            // 3) Finally try big spots
            used = tryParkOnBigSpot(parking.getBigSpots(), vehicle);
            if (used != null) {
                vehicleToSpots.put(vehicle, used);
                return true;
            }
            return false;
        }

        // If it's a Car
        if (vehicle instanceof Car) {
            // 1) Try car spots
            List<ParkingSpot> used = tryParkOnSpots(parking.getCarSpots(), vehicle);
            if (used != null) {
                vehicleToSpots.put(vehicle, used);
                return true;
            }
            // 2) Then fallback on big spots
            used = tryParkOnBigSpot(parking.getBigSpots(), vehicle);
            if (used != null) {
                vehicleToSpots.put(vehicle, used);
                return true;
            }
            return false;
        }

        // If it's a Van
        if (vehicle instanceof Van) {
            // 1) Prefer a big spot first
            List<ParkingSpot> used = tryParkOnBigSpot(parking.getBigSpots(), vehicle);
            if (used != null) {
                vehicleToSpots.put(vehicle, used);
                return true;
            }
            // 2) Otherwise occupy multiple car spots (e.g., 3)
            used = tryParkVanOnCarSpots(parking.getCarSpots(), (Van) vehicle);
            if (used != null) {
                vehicleToSpots.put(vehicle, used);
                return true;
            }
            return false;
        }

        // Otherwise (if we add a new vehicle type?), fallback
        return false;
    }

    /**
     * Unparks a vehicle by freeing all the spots it occupies, if any.
     */
    public void unparkVehicle(Vehicle vehicle) {
        List<ParkingSpot> spots = vehicleToSpots.get(vehicle);
        if (spots != null && !spots.isEmpty()) {
            // Free each occupied spot
            for (ParkingSpot s : spots) {
                s.free();
            }
            // Remove the vehicle from the map
            vehicleToSpots.remove(vehicle);
        }
    }

    /**
     * Tries to park a vehicle on a list of single-capacity spots (moto or car).
     * Returns a list containing the used spot if successful, or null otherwise.
     */
    private List<ParkingSpot> tryParkOnSpots(List<ParkingSpot> spots, Vehicle vehicle) {
        for (ParkingSpot spot : spots) {
            if (!spot.isOccupied() && spot.canFitVehicle(vehicle)) {
                spot.occupy();
                return List.of(spot); // single spot
            }
        }
        return null;
    }

    /**
     * Tries to park a vehicle on a single big spot, returns a list
     * containing the used spot if successful, or null otherwise.
     */
    private List<ParkingSpot> tryParkOnBigSpot(List<ParkingSpot> bigSpots, Vehicle vehicle) {
        for (ParkingSpot spot : bigSpots) {
            if (!spot.isOccupied() && spot.canFitVehicle(vehicle)) {
                spot.occupy();
                return List.of(spot);
            }
        }
        return null;
    }

    /**
     * Tries to park a Van on multiple car spots (e.g., 3).
     * Returns the list of spots if successful, or null otherwise.
     */
    private List<ParkingSpot> tryParkVanOnCarSpots(List<ParkingSpot> carSpots, Van van) {
        int required = van.getRequiredCarSpots(); // typically 3
        List<ParkingSpot> freeCarSpots = carSpots.stream()
                .filter(s -> !s.isOccupied() && s.canFitVehicle(van))
                .toList();

        if (freeCarSpots.size() >= required) {
            // Occupy the first 'required' spots
            List<ParkingSpot> spotsToUse = freeCarSpots.subList(0, required);
            for (ParkingSpot s : spotsToUse) {
                s.occupy();
            }
            return spotsToUse;
        }
        return null;
    }
}
