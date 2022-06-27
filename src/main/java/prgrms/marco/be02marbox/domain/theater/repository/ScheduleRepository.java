package prgrms.marco.be02marbox.domain.theater.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import prgrms.marco.be02marbox.domain.theater.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

	@Query("SELECT s FROM Schedule s WHERE CAST(s.startTime AS LocalDate) BETWEEN :startDay AND :endDay")
	List<Schedule> getSchedulesBetweenStartDateAndEndDate(LocalDate startDay, LocalDate endDay);
}
