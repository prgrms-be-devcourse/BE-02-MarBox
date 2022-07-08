package prgrms.marco.be02marbox.domain.theater.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import prgrms.marco.be02marbox.domain.theater.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

	@Query("SELECT s FROM Schedule s "
		+ "JOIN FETCH s.theaterRoom "
		+ "JOIN FETCH s.movie "
		+ "WHERE CAST(s.startTime AS LocalDate) BETWEEN :startDay AND :endDay")
	List<Schedule> findSchedulesBetweenStartDateAndEndDate(LocalDate startDay, LocalDate endDay);

	@Query("SELECT s FROM Schedule s "
		+ "JOIN FETCH s.theaterRoom "
		+ "JOIN FETCH s.movie "
		+ "WHERE CAST(s.startTime AS LocalDate) = :date")
	List<Schedule> findScheduleByDate(LocalDate date);

	@Query("SELECT s FROM Schedule s "
		+ "JOIN FETCH s.theaterRoom "
		+ "WHERE s.movie.id = :movieId AND CAST(s.startTime AS LocalDate) = :date")
	List<Schedule> findSchedulesByMovieIdAndDate(Long movieId, LocalDate date);
}
