package bharat_yatra_setu_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import bharat_yatra_setu_backend.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByBookingId(String bookingId);
}