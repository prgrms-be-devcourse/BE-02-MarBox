package prgrms.marco.be02marbox.domain.reservation.repository;

import java.util.List;

import org.springframework.data.repository.query.Param;

import prgrms.marco.be02marbox.domain.reservation.ReservedSeat;

public interface ReservedSeatRepositoryCustom {
	List<ReservedSeat> searchByScheduleIdStartsWith(@Param("scheduleId") Long scheduleId);
}
