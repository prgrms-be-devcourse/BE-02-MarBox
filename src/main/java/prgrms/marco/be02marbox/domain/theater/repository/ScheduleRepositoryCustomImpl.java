package prgrms.marco.be02marbox.domain.theater.repository;

import static prgrms.marco.be02marbox.domain.theater.QSchedule.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import prgrms.marco.be02marbox.domain.theater.Schedule;

@Repository
public class ScheduleRepositoryCustomImpl implements ScheduleRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	public ScheduleRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
		this.jpaQueryFactory = jpaQueryFactory;
	}

	@Override
	public List<Schedule> findSchedulesBetweenStartDateAndEndDate(LocalDate startDay, LocalDate endDay) {
		return jpaQueryFactory.selectFrom(schedule)
			.leftJoin(schedule.theaterRoom).fetchJoin()
			.leftJoin(schedule.movie).fetchJoin()
			.where(schedule.startTime.between(startDay.atStartOfDay(),
				LocalDateTime.of(endDay, LocalTime.MAX).withNano(0)))
			.fetch();
	}

	@Override
	public List<Schedule> findScheduleByDate(LocalDate date) {
		return jpaQueryFactory.selectFrom(schedule)
			.leftJoin(schedule.theaterRoom).fetchJoin()
			.leftJoin(schedule.movie).fetchJoin()
			.where(schedule.startTime.year()
				.eq(date.getYear())
				.and(schedule.startTime.month().eq(date.getMonth().getValue()))
				.and(schedule.startTime.dayOfMonth().eq(date.getDayOfMonth())))
			.fetch();
	}

	@Override
	public List<Schedule> findSchedulesByMovieIdAndDate(Long movieId, LocalDate date) {
		return jpaQueryFactory.selectFrom(schedule)
			.leftJoin(schedule.theaterRoom).fetchJoin()
			.where(schedule.movie.id.eq(movieId)
				.and(schedule.startTime.year().eq(date.getYear()))
				.and(schedule.startTime.month().eq(date.getMonth().getValue()))
				.and(schedule.startTime.dayOfMonth().eq(date.getDayOfMonth())))
			.fetch();
	}
}
