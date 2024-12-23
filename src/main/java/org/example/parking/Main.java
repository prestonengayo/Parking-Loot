package org.example.parking;

import org.example.parking.domain.Parking;
import org.example.parking.model.Car;
import org.example.parking.model.Moto;
import org.example.parking.model.Van;
import org.example.parking.model.Vehicle;
import org.example.parking.service.ParkingService;


public class Main {
    public static void main(String[] args) {
        // 1) Create a Parking with 2 moto spots, 5 car spots, and 2 big spots
        Parking parking = new Parking(2, 5, 2);
        ParkingService parkingService = new ParkingService();

        System.out.println("== Initial Parking Info ==");
        System.out.println("Total spots: " + parking.getTotalSpots());
        System.out.println("Free spots : " + parking.getFreeSpotsCount());
        System.out.println("Is empty?   " + parking.isEmpty());

        // 2) Park different vehicles and print outcome messages
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

        // 3) Display parking info after parking several vehicles
        System.out.println("\n== After Parking Some Vehicles ==");
        System.out.println("Free spots : " + parking.getFreeSpotsCount());
        System.out.println("Is full?    " + parking.isFull());

        // 4) Call the new methods (if you have them in Parking and ParkingService)
        System.out.println("\n== Detailed Spot Info ==");
        // Suppose you added these methods to the Parking class:
        System.out.println("Are moto spots full? " + parking.areMotoSpotsFull());
        System.out.println("Are car spots full?  " + parking.areCarSpotsFull());
        System.out.println("Are big spots full?  " + parking.areBigSpotsFull());

        // Suppose you added this method to the ParkingService class:
        System.out.println("Spots occupied by vans: " + parkingService.getNumberOfSpotsOccupiedByVans());

        // 5) Example of unpark: let's unpark the second van (VAN-002)
        System.out.println("\n== Unparking VAN-002 ==");
        parkingService.unparkVehicle(van2);

        // 6) Display parking info after unparking VAN-002
        System.out.println("After unparking VAN-002:");
        System.out.println("Free spots : " + parking.getFreeSpotsCount());
        System.out.println("Is full?    " + parking.isFull());

        // Check spot states again
        System.out.println("Are moto spots full? " + parking.areMotoSpotsFull());
        System.out.println("Are car spots full?  " + parking.areCarSpotsFull());
        System.out.println("Are big spots full?  " + parking.areBigSpotsFull());
        System.out.println("Spots occupied by vans: " + parkingService.getNumberOfSpotsOccupiedByVans());
    }


    private static void parkVehicleAndPrint(ParkingService service, Parking parking, Vehicle vehicle) {
        boolean parked = service.parkVehicle(parking, vehicle);
        if (parked) {
            System.out.println("Parked vehicle [" + vehicle.getClass().getSimpleName()
                    + "] with plate \"" + vehicle.getPlateNumber() + "\" successfully.");
        } else {
            System.out.println("Could NOT park vehicle [" + vehicle.getClass().getSimpleName()
                    + "] with plate \"" + vehicle.getPlateNumber() + "\".");
        }
        // Print the updated free spots count to track the parking usage
        System.out.println(" -> Free spots now: " + parking.getFreeSpotsCount());
    }
}
