package prgrms.marco.be02marbox.domain.reservation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import prgrms.marco.be02marbox.domain.reservation.ReservedSeat;

public interface ReservedSeatRepository extends JpaRepository<ReservedSeat, String>, ReservedSeatRepositoryCustom {
}
