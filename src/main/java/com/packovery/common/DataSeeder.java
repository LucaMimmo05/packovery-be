package com.packovery.common;

import com.packovery.common.enums.OrderStatus;
import com.packovery.common.enums.PackageSize;
import com.packovery.common.enums.PackageWeight;
import com.packovery.common.enums.UserRole;
import com.packovery.common.enums.VehicleType;
import com.packovery.location.OrderLocation;
import com.packovery.location.RiderLocation;
import com.packovery.order.Order;
import com.packovery.user.User;
import com.packovery.vehicle.Vehicle;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@ApplicationScoped
public class DataSeeder {

    @ConfigProperty(name = "app.db.seed-enabled", defaultValue = "true")
    boolean seedEnabled;

    private static final Logger LOG = Logger.getLogger(DataSeeder.class);

    @Transactional
    void onStart(@Observes StartupEvent ev) {

        if (!seedEnabled) {
            LOG.info("DataSeeder disabilitato da configurazione (app.db.seed-enabled=false).");
            return;
        }

        LOG.info("Initing DataSeeder...");

        String commonPasswordHash = BcryptUtil.bcryptHash("password");

        // 1. Create Sender
        User sender = User.find("email", "sender@test.com").firstResult();
        if (sender == null) {
            sender = new User("sender@test.com", commonPasswordHash, UserRole.USER);
            sender.setFirstName("Luigi");
            sender.setLastName("Verdi");
            sender.persist();
        }

        // 2. Create Rider (Mario Rossi)
        User rider = User.find("email", "rider@test.com").firstResult();
        if (rider == null) {
            rider = new User("rider@test.com", commonPasswordHash, UserRole.RIDER);
            rider.setFirstName("Mario");
            rider.setLastName("Rossi");
            rider.persist();
        }

        // 3. Create Email Tester User (Customer Care)
        User emailTester = User.find("email", "testquarkus24@gmail.com").firstResult();
        if (emailTester == null) {
            emailTester = new User("testquarkus24@gmail.com", commonPasswordHash, UserRole.CUSTOMER_CARE);
            emailTester.setFirstName("Tester");
            emailTester.setLastName("Email");
            emailTester.persist();
            LOG.info("Creato utente per test email: testquarkus24@gmail.com");
        }

        // 4. Create Vehicle for Rider
        Vehicle vehicle = Vehicle.find("licensePlate", "TEST-PLATE-001").firstResult();
        if (vehicle == null) {
            vehicle = new Vehicle(rider, VehicleType.SCOOTER, "TEST-PLATE-001");
            vehicle.persist();
        }

        // 5. Create Order (TEST-MAP-001)
        Order order = Order.find("trackingCode", "TEST-MAP-001").firstResult();
        if (order == null) {
            order = new Order();
            order.setStatus(OrderStatus.IN_TRANSIT);
            order.setPackageSize(PackageSize.M);
            order.setPackageWeight(PackageWeight.M);
            order.setActualSize(new BigDecimal("25.50"));
            order.setActualWeight(new BigDecimal("2.500"));
            order.setTrackingCode("TEST-MAP-001");
            order.setCreationDate(LocalDateTime.now());
            order.setCreatedAt(LocalDateTime.now());
            order.setRider(rider);
            order.setVehicle(vehicle);
            order.setSenderId(sender.id);
            order.persist();

            OrderLocation location = new OrderLocation();
            location.setOrder(order);
            location.setPickupCity("Roma");
            location.setPickupProvince("RM");
            location.setPickupLatitude(new BigDecimal("41.9014"));
            location.setPickupLongitude(new BigDecimal("12.5000"));
            location.setDeliveryCity("Roma");
            location.setDeliveryProvince("RM");
            location.setDeliveryLatitude(new BigDecimal("41.9029"));
            location.setDeliveryLongitude(new BigDecimal("12.4534"));
            location.setStreetAddress("Via Cavour, 1");
            location.setEstimatedArrival(LocalDateTime.now().plusMinutes(45));
            location.persist();

            order.setLocation(location);
        }

        // 6. Create Rider Location (MongoDB)
        RiderLocation riderLocation = RiderLocation.find("riderId", rider.id).firstResult();
        if (riderLocation == null) {
            riderLocation = new RiderLocation();
            riderLocation.riderId = rider.id;
            riderLocation.latitude = 41.8992;
            riderLocation.longitude = 12.4733;
            riderLocation.positionTimestamp = Instant.now();
            riderLocation.distanceTraveled = 5.2;

            riderLocation.persist();
            LOG.info("Created new RiderLocation for rider " + rider.id);
        }

        LOG.info("DataSeeder base completed. Test Order: TEST-MAP-001");

        // --- MOCK DATA EXPANSION ---
        seedAdditionalData(commonPasswordHash);
    }

    private void seedAdditionalData(String commonPasswordHash) {

        User rider2 = createUser("giulia.bianchi@test.com", "Giulia", "Bianchi", UserRole.RIDER, commonPasswordHash);
        Vehicle vehicle2 = createVehicle(rider2, VehicleType.BIKE, "BIKE-MI-01");

        User rider3 = createUser("luca.neri@test.com", "Luca", "Neri", UserRole.RIDER, commonPasswordHash);
        Vehicle vehicle3 = createVehicle(rider3, VehicleType.VAN, "VAN-NA-99");

        User rider4 = createUser("marco.verdi@test.com", "Marco", "Verdi", UserRole.RIDER, commonPasswordHash);
        Vehicle vehicle4 = createVehicle(rider4, VehicleType.CAR, "CAR-TO-88");

        User sender = User.find("email", "sender@test.com").firstResult();

        // Order 2: Milano - DELIVERED
        createOrder("TEST-MI-002", OrderStatus.DELIVERED, sender, rider2, vehicle2,
                "Milano", "MI", new BigDecimal("45.4642"), new BigDecimal("9.1900"),
                "Monza", "MB", new BigDecimal("45.5845"), new BigDecimal("9.2744"),
                LocalDateTime.now().minusHours(24),
                PackageSize.S, PackageWeight.S
        );
        setupRiderLocation(rider2.id, 45.5845, 9.2744);

        // Order 3: Napoli - PENDING (No Rider)
        createOrder("TEST-NA-003", OrderStatus.PENDING, sender, null, null,
                "Napoli", "NA", new BigDecimal("40.8518"), new BigDecimal("14.2681"),
                "Salerno", "SA", new BigDecimal("40.6824"), new BigDecimal("14.7681"),
                LocalDateTime.now().minusHours(2),
                PackageSize.XL, PackageWeight.XL
        );
        setupRiderLocation(rider3.id, 40.8518, 14.2681);

        // Order 4: Roma - ASSIGNED (Mario Rossi again)
        User rider1 = User.find("email", "rider@test.com").firstResult();
        Vehicle vehicle1 = Vehicle.find("licensePlate", "TEST-PLATE-001").firstResult();
        createOrder("TEST-RM-004", OrderStatus.ASSIGNED, sender, rider1, vehicle1,
                "Fiumicino", "RM", new BigDecimal("41.7999"), new BigDecimal("12.2462"),
                "Roma Centro", "RM", new BigDecimal("41.9028"), new BigDecimal("12.4964"),
                LocalDateTime.now().minusMinutes(30),
                PackageSize.L, PackageWeight.L
        );

        // Order 5: Torino -> Asti - IN_TRANSIT (Marco Verdi)
        createOrder("TEST-TO-005", OrderStatus.IN_TRANSIT, sender, rider4, vehicle4,
                "Torino", "TO", new BigDecimal("45.0703"), new BigDecimal("7.6869"),
                "Asti", "AT", new BigDecimal("44.8998"), new BigDecimal("8.2045"),
                LocalDateTime.now().minusMinutes(45),
                PackageSize.M, PackageWeight.M
        );
        setupRiderLocation(rider4.id, 44.9500, 7.8500);

        // Order 6: Bari -> Lecce - CANCELLED
        createOrder("TEST-BA-006", OrderStatus.CANCELLED, sender, null, null,
                "Bari", "BA", new BigDecimal("41.1171"), new BigDecimal("16.8719"),
                "Lecce", "LE", new BigDecimal("40.3515"), new BigDecimal("18.1750"),
                LocalDateTime.now().minusDays(1),
                PackageSize.S, PackageWeight.S
        );

        // Order 7: Palermo -> Catania - FAILED
        User rider5 = createUser("rider5@test.com", "Giovanni", "Neri", UserRole.RIDER, commonPasswordHash);
        Vehicle vehicle5 = createVehicle(rider5, VehicleType.VAN, "TEST-PLATE-005");
        createOrder("TEST-PA-007", OrderStatus.FAILED, sender, rider5, vehicle5,
                "Palermo", "PA", new BigDecimal("38.1157"), new BigDecimal("13.3615"),
                "Catania", "CT", new BigDecimal("37.5079"), new BigDecimal("15.0830"),
                LocalDateTime.now().minusHours(5),
                PackageSize.L, PackageWeight.L
        );
    }

    private User createUser(String email, String firstName, String lastName, UserRole role, String passwordHash) {
        User user = User.find("email", email).firstResult();
        if (user == null) {
            user = new User(email, passwordHash, role);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.persist();
        }
        return user;
    }

    private Vehicle createVehicle(User rider, VehicleType type, String plate) {
        Vehicle vehicle = Vehicle.find("licensePlate", plate).firstResult();
        if (vehicle == null) {
            vehicle = new Vehicle(rider, type, plate);
            vehicle.persist();
        }
        return vehicle;
    }

    private void createOrder(String trackingCode, OrderStatus status, User sender, User rider, Vehicle vehicle,
                             String pickupCity, String pickupProv, BigDecimal pickupLat, BigDecimal pickupLon,
                             String deliveryCity, String deliveryProv, BigDecimal delLat, BigDecimal delLon,
                             LocalDateTime creationDate, PackageSize size, PackageWeight weight) {

        Order existingOrder = Order.find("trackingCode", trackingCode).firstResult();
        if (existingOrder != null) {
            return;
        }

        Order order = new Order();
        order.setStatus(status);
        order.setPackageSize(size);
        order.setPackageWeight(weight);
        order.setActualSize(size == PackageSize.S ? new BigDecimal("10.0") : new BigDecimal("50.0"));
        order.setActualWeight(weight == PackageWeight.S ? new BigDecimal("0.5") : new BigDecimal("5.0"));
        order.setTrackingCode(trackingCode);
        order.setCreationDate(creationDate);
        order.setCreatedAt(creationDate);
        order.setSenderId(sender.id);

        if (rider != null) {
            order.setRider(rider);
            order.setVehicle(vehicle);
        }

        order.persist();

        OrderLocation location = new OrderLocation();
        location.setOrder(order);
        location.setPickupCity(pickupCity);
        location.setPickupProvince(pickupProv);
        location.setPickupLatitude(pickupLat);
        location.setPickupLongitude(pickupLon);

        location.setDeliveryCity(deliveryCity);
        location.setDeliveryProvince(deliveryProv);
        location.setDeliveryLatitude(delLat);
        location.setDeliveryLongitude(delLon);

        location.setStreetAddress("Via Exemplum, 10");

        if (status == OrderStatus.IN_TRANSIT || status == OrderStatus.ASSIGNED) {
            location.setEstimatedArrival(LocalDateTime.now().plusHours(1));
        } else if (status == OrderStatus.DELIVERED) {
            location.setDeliveryTime(LocalDateTime.now().minusMinutes(10));
        }

        location.persist();
        order.setLocation(location);
    }

    private void setupRiderLocation(Long riderId, double lat, double lon) {
        RiderLocation loc = RiderLocation.find("riderId", riderId).firstResult();

        if (loc == null) {
            loc = new RiderLocation();
            loc.riderId = riderId;
            loc.latitude = lat;
            loc.longitude = lon;
            loc.positionTimestamp = Instant.now();
            loc.distanceTraveled = 12.5;
            loc.persist();
        }
    }
}