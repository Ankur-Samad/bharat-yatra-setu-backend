package bharat_yatra_setu_backend.controller;

import bharat_yatra_setu_backend.entity.Booking;
import bharat_yatra_setu_backend.repository.BookingRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingRepository bookingRepository;

    public BookingController(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    // =========================
    // CREATE BOOKING
    // =========================

    @PostMapping
    public ResponseEntity<Map<String, Object>> createBooking(
            @RequestBody Map<String, Object> bookingData
    ) {

        String bookingId = "BYS-" + System.currentTimeMillis();

        Booking booking = new Booking();

        booking.setBookingId(bookingId);

        Object experienceId = bookingData.get("experienceId");

        if (experienceId != null) {
            booking.setExperienceId(String.valueOf(experienceId));
        }

        booking.setExperienceName(
                getString(bookingData, "experienceName")
        );

        booking.setBookingDate(
                getString(bookingData, "date")
        );

        booking.setBookingTime(
                getString(bookingData, "time")
        );

        booking.setGuests(
                getInteger(bookingData, "guests")
        );

        booking.setTotal(
                getDouble(bookingData, "total")
        );

        booking.setPaymentStatus("SUCCESS");
        booking.setStatus("PENDING");
        booking.setProviderStatus("PENDING");

        Booking savedBooking = bookingRepository.save(booking);

        return ResponseEntity.ok(
                buildBookingResponse(savedBooking)
        );
    }


    // =========================
    // GET ALL BOOKINGS
    // =========================

    @GetMapping
    public List<Map<String, Object>> getBookings() {

        return bookingRepository.findAll()
                .stream()
                .map(this::buildBookingResponse)
                .toList();
    }


    // =========================
    // GET SINGLE BOOKING
    // =========================

    @GetMapping("/{bookingId}")
    public ResponseEntity<Map<String, Object>> getBooking(
            @PathVariable String bookingId
    ) {

        return bookingRepository
                .findByBookingId(bookingId)
                .map(booking ->
                        ResponseEntity.ok(
                                buildBookingResponse(booking)
                        )
                )
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }


    // =========================
    // UPDATE BOOKING
    // =========================

    @PutMapping("/{bookingId}")
    public ResponseEntity<Map<String, Object>> updateBooking(
            @PathVariable String bookingId,
            @RequestBody Map<String, Object> updates
    ) {

        return bookingRepository
                .findByBookingId(bookingId)
                .map(booking -> {

                    if (updates.containsKey("status")) {
                        booking.setStatus(
                                String.valueOf(
                                        updates.get("status")
                                )
                        );
                    }

                    if (updates.containsKey("providerStatus")) {
                        booking.setProviderStatus(
                                String.valueOf(
                                        updates.get("providerStatus")
                                )
                        );
                    }

                    if (updates.containsKey("paymentStatus")) {
                        booking.setPaymentStatus(
                                String.valueOf(
                                        updates.get("paymentStatus")
                                )
                        );
                    }

                    Booking updatedBooking =
                            bookingRepository.save(booking);

                    return ResponseEntity.ok(
                            buildBookingResponse(updatedBooking)
                    );
                })
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }


    // =========================
    // ADMIN BOOKING STATS
    // =========================

    @GetMapping("/stats")
    public Map<String, Object> getBookingStats() {

        List<Booking> bookings =
                bookingRepository.findAll();

        long totalBookings =
                bookings.size();

        long successfulPayments =
                bookings.stream()
                        .filter(booking ->
                                "SUCCESS".equalsIgnoreCase(
                                        booking.getPaymentStatus()
                                )
                        )
                        .count();

        double totalRevenue =
                bookings.stream()
                        .filter(booking ->
                                "SUCCESS".equalsIgnoreCase(
                                        booking.getPaymentStatus()
                                )
                        )
                        .mapToDouble(booking ->
                                booking.getTotal() != null
                                        ? booking.getTotal()
                                        : 0.0
                        )
                        .sum();

        long confirmedBookings =
                bookings.stream()
                        .filter(booking ->
                                "CONFIRMED".equalsIgnoreCase(
                                        booking.getStatus()
                                )
                        )
                        .count();

        long pendingBookings =
                bookings.stream()
                        .filter(booking ->
                                "PENDING".equalsIgnoreCase(
                                        booking.getStatus()
                                )
                        )
                        .count();

        long acceptedBookings =
                bookings.stream()
                        .filter(booking ->
                                "ACCEPTED".equalsIgnoreCase(
                                        booking.getProviderStatus()
                                )
                        )
                        .count();

        Map<String, Object> stats =
                new HashMap<>();

        stats.put(
                "totalBookings",
                totalBookings
        );

        stats.put(
                "successfulPayments",
                successfulPayments
        );

        stats.put(
                "totalRevenue",
                totalRevenue
        );

        stats.put(
                "confirmedBookings",
                confirmedBookings
        );

        stats.put(
                "pendingBookings",
                pendingBookings
        );

        stats.put(
                "acceptedBookings",
                acceptedBookings
        );

        return stats;
    }


    // =========================
    // BUILD BOOKING RESPONSE
    // =========================

    private Map<String, Object> buildBookingResponse(
            Booking booking
    ) {

        Map<String, Object> bookingDetails =
                new HashMap<>();

        bookingDetails.put(
                "experienceId",
                booking.getExperienceId()
        );

        bookingDetails.put(
                "experienceName",
                booking.getExperienceName()
        );

        bookingDetails.put(
                "date",
                booking.getBookingDate()
        );

        bookingDetails.put(
                "time",
                booking.getBookingTime()
        );

        bookingDetails.put(
                "guests",
                booking.getGuests()
        );

        bookingDetails.put(
                "total",
                booking.getTotal()
        );


        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "bookingId",
                booking.getBookingId()
        );

        response.put(
                "status",
                booking.getStatus()
        );

        response.put(
                "paymentStatus",
                booking.getPaymentStatus()
        );

        response.put(
                "providerStatus",
                booking.getProviderStatus()
        );

        response.put(
                "booking",
                bookingDetails
        );

        return response;
    }


    // =========================
    // HELPER: STRING
    // =========================

    private String getString(
            Map<String, Object> data,
            String key
    ) {

        Object value = data.get(key);

        return value == null
                ? null
                : String.valueOf(value);
    }


    // =========================
    // HELPER: INTEGER
    // =========================

    private Integer getInteger(
            Map<String, Object> data,
            String key
    ) {

        Object value = data.get(key);

        if (value == null) {
            return null;
        }

        if (value instanceof Number number) {
            return number.intValue();
        }

        try {
            return Integer.parseInt(
                    String.valueOf(value)
            );
        } catch (NumberFormatException exception) {
            return null;
        }
    }


    // =========================
    // HELPER: DOUBLE
    // =========================

    private Double getDouble(
            Map<String, Object> data,
            String key
    ) {

        Object value = data.get(key);

        if (value == null) {
            return null;
        }

        if (value instanceof Number number) {
            return number.doubleValue();
        }

        try {
            return Double.parseDouble(
                    String.valueOf(value)
            );
        } catch (NumberFormatException exception) {
            return null;
        }
    }
}