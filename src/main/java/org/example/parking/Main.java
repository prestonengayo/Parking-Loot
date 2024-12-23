package org.example.parking;

import org.example.parking.domain.Parking;
import org.example.parking.model.Car;
import org.example.parking.model.Moto;
import org.example.parking.model.Van;
import org.example.parking.model.Vehicle;
import org.example.parking.service.ParkingService;

/**
 * Demo class to show usage of the Parking and ParkingService.
 */
public class Main {
    public static void main(String[] args) {
        // 1) Create a Parking with 2 moto spots, 5 car spots, and 2 big spots
        Parking parking = new Parking(2, 5, 2);
        ParkingService parkingService = new ParkingService();

        System.out.println("== Initial Parking Info ==");
        System.out.println("Total spots: " + parking.getTotalSpots());
        System.out.println("Free spots : " + parking.getFreeSpotsCount());
        System.out.println("Is empty?   " + parking.isEmpty());

        // 2) Park different vehicles and print messages
        Vehicle moto1 = new Moto("MOTO-123");
        parkVehicleAndPrint(parkingService, parking, moto1);

        Vehicle moto2 = new Moto("MOTO-456");
        parkVehicleAndPrint(parkingService, parking, moto2);

        Vehicle car1 = new Car("CAR-ABC");
        parkVehicleAndPrint(parkingService, parking, car1);

        Vehicle van1 = new Van("VAN-001");
        parkVehicleAndPrint(parkingService, parking, van1);

        Vehicle van2 = new Van("VAN-002");
        parkVehicleAndPrint(parkingService, parking, van2);

        System.out.println("\n== After Parking Some Vehicles ==");
        System.out.println("Free spots : " + parking.getFreeSpotsCount());
        System.out.println("Is full?    " + parking.isFull());

        // 3) Example of unpark: let's unpark the second van (VAN-002)
        System.out.println("\n== Unparking VAN-002 ==");
        parkingService.unparkVehicle(van2);

        // Check the parking state again
        System.out.println("After unparking VAN-002:");
        System.out.println("Free spots : " + parking.getFreeSpotsCount());
        System.out.println("Is full?    " + parking.isFull());
    }

    /**
     * Helper method to attempt parking a vehicle and print a success/failure message.
     */
    private static void parkVehicleAndPrint(ParkingService service, Parking parking, Vehicle vehicle) {
        boolean parked = service.parkVehicle(parking, vehicle);
        if (parked) {
            System.out.println("Parked vehicle [" + vehicle.getClass().getSimpleName()
                    + "] with plate \"" + vehicle.getPlateNumber() + "\" successfully.");
        } else {
            System.out.println("Could NOT park vehicle [" + vehicle.getClass().getSimpleName()
                    + "] with plate \"" + vehicle.getPlateNumber() + "\".");
        }
        // Print the updated free spots count to follow the progress
        System.out.println(" -> Free spots now: " + parking.getFreeSpotsCount());
    }
}
