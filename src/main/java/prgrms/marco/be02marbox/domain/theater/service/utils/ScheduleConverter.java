package prgrms.marco.be02marbox.domain.theater.service.utils;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import prgrms.marco.be02marbox.domain.movie.Movie;
import prgrms.marco.be02marbox.domain.theater.Schedule;
import prgrms.marco.be02marbox.domain.theater.TheaterRoom;
import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateSchedule;
import prgrms.marco.be02marbox.domain.theater.dto.ResponseFindMovieListAndDateList;

@Component
public class ScheduleConverter {

	public Schedule convertFromRequestCreateScheduleToSchedule(RequestCreateSchedule request, TheaterRoom theaterRoom,
		Movie movie) {
		return Schedule.builder()
			.theaterRoom(theaterRoom)
			.movie(movie)
			.startTime(request.startTime())
			.endTime(request.endTime())
			.build();
	}

	public ResponseFindMovieListAndDateList convertFromScheduleListToResponseFindMovieListAndDateList(
		List<Schedule> scheduleList) {
		List<LocalDate> dateList = scheduleList.stream()
			.map(schedule -> schedule.getStartTime().toLocalDate())
			.distinct().toList();

		List<String> movieList = scheduleList.stream()
			.map(schedule -> schedule.getMovie().getName())
			.distinct().toList();

		return new ResponseFindMovieListAndDateList(movieList, dateList);
	}
}
