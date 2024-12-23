package parking.service;

import org.example.parking.domain.Parking;
import org.example.parking.model.Car;
import org.example.parking.model.Moto;
import org.example.parking.model.Van;
import org.example.parking.model.Vehicle;
import org.example.parking.service.ParkingService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 * Extended Unit tests for ParkingService.
 */
public class ParkingServiceTest {

    /**
     * testParkMotoOnMotoSpot:
     * Ensures that a Moto can park on a dedicated moto spot if one is available.
     */
    @Test
    public void testParkMotoOnMotoSpot() {
        // Given: a parking with 1 moto spot, 1 car spot, 1 big spot
        Parking parking = new Parking(1, 1, 1);
        ParkingService service = new ParkingService();
        Vehicle moto = new Moto("MOTO-001");

        // When: we park the moto
        boolean parked = service.parkVehicle(parking, moto);

        // Then: it should succeed, and occupy the moto spot
        Assertions.assertTrue(parked, "Moto should successfully park on a moto spot");
        // We expect 2 free spots left (the car spot and the big spot)
        Assertions.assertEquals(2, parking.getFreeSpotsCount(),
                "After parking the moto, there should be 2 free spots left");
    }

    /**
     * testParkCarOnCarSpot:
     * Checks that a Car correctly parks on a car spot if available.
     */
    @Test
    public void testParkCarOnCarSpot() {
        // Given
        Parking parking = new Parking(1, 1, 1);
        ParkingService service = new ParkingService();
        Vehicle car = new Car("CAR-ABC");

        // When
        boolean parked = service.parkVehicle(parking, car);

        // Then
        Assertions.assertTrue(parked, "Car should successfully park on a car spot");
        Assertions.assertEquals(2, parking.getFreeSpotsCount(),
                "There should be 2 free spots left (moto + big) after parking the car");
    }

    /**
     * testParkVanOnBigSpot:
     * Verifies that a Van parks on a big spot if one is available.
     */
    @Test
    public void testParkVanOnBigSpot() {
        // Given
        Parking parking = new Parking(1, 1, 1); // 1 moto, 1 car, 1 big
        ParkingService service = new ParkingService();
        Vehicle van = new Van("VAN-001");

        // When
        boolean parked = service.parkVehicle(parking, van);

        // Then
        Assertions.assertTrue(parked, "Van should park on the big spot if available");
        // After parking the van on the big spot, we expect 2 free spots (moto + car)
        Assertions.assertEquals(2, parking.getFreeSpotsCount(),
                "Big spot occupied by the van, the other 2 remain free");
    }

    /**
     * testParkVanUsingCarSpotsWhenBigSpotNotAvailable:
     * Ensures that a Van will occupy 3 car spots if no big spot is available.
     */
    @Test
    public void testParkVanUsingCarSpotsWhenBigSpotNotAvailable() {
        // Given: no big spots, but 4 car spots
        Parking parking = new Parking(1, 4, 0); // 1 moto, 4 car, 0 big
        ParkingService service = new ParkingService();
        Vehicle van = new Van("VAN-002");

        // When
        boolean parked = service.parkVehicle(parking, van);

        // Then
        Assertions.assertTrue(parked, "Van should occupy 3 car spots if no big spot is available");
        // We had 4 car spots, the van takes 3 => 1 car spot left, plus the 1 moto spot
        Assertions.assertEquals(2, parking.getFreeSpotsCount(),
                "Van took 3 car spots, leaving 1 car spot + 1 moto spot free");
    }

    /**
     * testParkVanFailsIfNotEnoughCarSpots:
     * Confirms that a Van cannot park if there aren't enough car spots (3) and no big spots.
     */
    @Test
    public void testParkVanFailsIfNotEnoughCarSpots() {
        // Given: 1 moto spot, 2 car spots, 0 big spots
        // A van needs 3 car spots => this should fail
        Parking parking = new Parking(1, 2, 0);
        ParkingService service = new ParkingService();
        Vehicle van = new Van("VAN-003");

        // When
        boolean parked = service.parkVehicle(parking, van);

        // Then
        Assertions.assertFalse(parked, "Van should fail to park if there are not enough car spots");
        Assertions.assertEquals(3, parking.getFreeSpotsCount(),
                "No spots should be occupied since the van could not park");
    }

    /**
     * testIsFull:
     * Checks the scenario where all spots become occupied (1 moto, 1 car, 1 big).
     */
    @Test
    public void testIsFull() {
        // Given: a small parking (1 moto, 1 car, 1 big)
        Parking parking = new Parking(1, 1, 1);
        ParkingService service = new ParkingService();

        // We park 3 vehicles that fit perfectly:
        // 1 moto => uses the moto spot
        Assertions.assertTrue(service.parkVehicle(parking, new Moto("MOTO-111")));
        // 1 car => uses the car spot
        Assertions.assertTrue(service.parkVehicle(parking, new Car("CAR-XYZ")));
        // 1 van => uses the big spot
        Assertions.assertTrue(service.parkVehicle(parking, new Van("VAN-999")));

        // When: we check if the parking is full
        boolean isFull = parking.isFull();

        // Then: all spots are taken
        Assertions.assertTrue(isFull, "Parking should be full");
        Assertions.assertEquals(0, parking.getFreeSpotsCount(),
                "No free spots left");
    }

    /**
     * testIsEmpty:
     * Validates that a new parking with no vehicles is indeed empty.
     */
    @Test
    public void testIsEmpty() {
        // Given: a new parking with no vehicles parked
        Parking parking = new Parking(2, 5, 2);

        // Then: it should be empty initially
        Assertions.assertTrue(parking.isEmpty(), "Brand new parking should be empty");
        Assertions.assertEquals(9, parking.getFreeSpotsCount(),
                "2 + 5 + 2 = 9 free spots at the start");
    }

    // -------------------------------------------------------------------------
    //                        EXTRA TESTS (SCENARIOS)
    // -------------------------------------------------------------------------

    /**
     * testParkMotoFallbackOnCarSpot:
     * Ensures that a Moto falls back to a car spot if no moto spot is available.
     */
    @Test
    public void testParkMotoFallbackOnCarSpot() {
        // Given: 0 moto spots, 2 car spots, 1 big spot
        Parking parking = new Parking(0, 2, 1);
        ParkingService service = new ParkingService();
        Vehicle moto = new Moto("MOTO-888");

        // When: we park the moto
        boolean parked = service.parkVehicle(parking, moto);

        // Then: it should succeed using a car spot
        Assertions.assertTrue(parked, "Moto should park on a car spot if no moto spot is available");
        // We used 1 car spot => 1 car spot left + 1 big spot => total 2
        Assertions.assertEquals(2, parking.getFreeSpotsCount(),
                "1 car spot left + 1 big spot => 2 free spots remain");
    }

    /**
     * testParkMotoFallbackOnBigSpot:
     * Ensures that a Moto uses a big spot if neither moto nor car spots exist or are free.
     */
    @Test
    public void testParkMotoFallbackOnBigSpot() {
        // Given: 0 moto spots, 0 car spots, 1 big spot
        Parking parking = new Parking(0, 0, 1);
        ParkingService service = new ParkingService();
        Vehicle moto = new Moto("MOTO-999");

        // When: we park the moto
        boolean parked = service.parkVehicle(parking, moto);

        // Then: it should succeed on the big spot
        Assertions.assertTrue(parked,
                "Moto should occupy the big spot if there are no moto or car spots left");
        Assertions.assertEquals(0, parking.getFreeSpotsCount(),
                "The single big spot is now occupied => 0 free spots");
    }

    /**
     * testParkCarOnBigSpotIfCarSpotsFull:
     * Checks that a Car can occupy a big spot if there are no car spots available.
     */
    @Test
    public void testParkCarOnBigSpotIfCarSpotsFull() {
        // Given: 1 moto spot, 0 car spots, 1 big spot
        Parking parking = new Parking(1, 0, 1);
        ParkingService service = new ParkingService();
        Vehicle car = new Car("CAR-NOCAR");

        // When
        boolean parked = service.parkVehicle(parking, car);

        // Then
        Assertions.assertTrue(parked,
                "Car should park on a big spot if no car spots are available");
        // 1 moto spot remains free => total 1
        Assertions.assertEquals(1, parking.getFreeSpotsCount(),
                "The big spot is used; only the moto spot remains => 1 free spot");
    }

    /**
     * testParkMultipleMotosPartiallyFillingSpots:
     * Verifies that multiple Motos distribute correctly over moto, car, and big spots.
     */
    @Test
    public void testParkMultipleMotosPartiallyFillingSpots() {
        // Given: 2 moto spots, 1 car spot, 1 big spot
        // We'll park 4 motos total => first 2 use the moto spots,
        // third goes to car spot, fourth goes to big spot (if logic allows).
        Parking parking = new Parking(2, 1, 1);
        ParkingService service = new ParkingService();

        // Motos
        Assertions.assertTrue(service.parkVehicle(parking, new Moto("MOTO-1")));
        Assertions.assertTrue(service.parkVehicle(parking, new Moto("MOTO-2")));
        Assertions.assertTrue(
                service.parkVehicle(parking, new Moto("MOTO-3")),
                "Third moto should fallback to the car spot"
        );
        Assertions.assertTrue(
                service.parkVehicle(parking, new Moto("MOTO-4")),
                "Fourth moto should fallback to the big spot"
        );

        // Then
        Assertions.assertTrue(parking.isFull(), "All spots should now be occupied");
        Assertions.assertEquals(0, parking.getFreeSpotsCount());
    }

    /**
     * testBoundaryParkingNoSpots:
     * Tests a boundary case where the parking has 0 spots of any kind.
     */
    @Test
    public void testBoundaryParkingNoSpots() {
        // Given: an empty parking configuration
        Parking parking = new Parking(0, 0, 0);
        ParkingService service = new ParkingService();

        // When: we try to park anything
        boolean motoParked = service.parkVehicle(parking, new Moto("MOTO-BND"));
        boolean carParked = service.parkVehicle(parking, new Car("CAR-BND"));
        boolean vanParked = service.parkVehicle(parking, new Van("VAN-BND"));

        // Then: all should fail
        Assertions.assertFalse(motoParked,
                "Should fail to park a moto in a parking with 0 spots");
        Assertions.assertFalse(carParked,
                "Should fail to park a car in a parking with 0 spots");
        Assertions.assertFalse(vanParked,
                "Should fail to park a van in a parking with 0 spots");
        Assertions.assertTrue(parking.isEmpty(),
                "Parking remains empty since no vehicle could be parked");
    }
}
