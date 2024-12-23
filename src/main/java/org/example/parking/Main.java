package org.example.parking;

import org.example.parking.domain.Parking;
import org.example.parking.model.Car;
import org.example.parking.model.Moto;
import org.example.parking.model.Van;
import org.example.parking.service.ParkingService;

/**
 * Demo class to show usage of the Parking and ParkingService.
 */
public class Main {
    public static void main(String[] args) {
        // Create a Parking with 2 moto spots, 5 car spots, and 2 big spots
        Parking parking = new Parking(2, 5, 2);
        ParkingService parkingService = new ParkingService();

        System.out.println("== Initial Parking Info ==");
        System.out.println("Total spots: " + parking.getTotalSpots());
        System.out.println("Free spots : " + parking.getFreeSpotsCount());
        System.out.println("Is empty?   " + parking.isEmpty());

        // Park different vehicles
        parkingService.parkVehicle(parking, new Moto("MOTO-123"));
        parkingService.parkVehicle(parking, new Moto("MOTO-456"));
        parkingService.parkVehicle(parking, new Car("CAR-ABC"));
        parkingService.parkVehicle(parking, new Van("VAN-001"));
        parkingService.parkVehicle(parking, new Van("VAN-002"));

        System.out.println("\n== After Parking Some Vehicles ==");
        System.out.println("Free spots : " + parking.getFreeSpotsCount());
        System.out.println("Is full?    " + parking.isFull());
    }
}
