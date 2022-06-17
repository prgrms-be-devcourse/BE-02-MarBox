package prgrms.marco.be02marbox.domain.theater.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import prgrms.marco.be02marbox.domain.theater.Seat;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}
