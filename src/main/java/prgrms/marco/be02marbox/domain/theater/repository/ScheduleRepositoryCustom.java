package prgrms.marco.be02marbox.domain.theater.repository;

import java.time.LocalDate;
import java.util.List;

import prgrms.marco.be02marbox.domain.theater.Schedule;

public interface ScheduleRepositoryCustom {

	List<Schedule> findSchedulesBetweenStartDateAndEndDate(LocalDate startDay, LocalDate endDay);

	List<Schedule> findScheduleByDate(LocalDate date);

	List<Schedule> findSchedulesByMovieIdAndDate(Long movieId, LocalDate date);
}
