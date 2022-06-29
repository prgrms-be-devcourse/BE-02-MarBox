package prgrms.marco.be02marbox.domain.reservation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import prgrms.marco.be02marbox.domain.reservation.ReservedSeat;

public interface ReservedSeatRepository extends JpaRepository<ReservedSeat, String> {
	List<ReservedSeat> searchByIdStartsWith(String scheduleId);
}
