package prgrms.marco.be02marbox.domain.theater.service.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import prgrms.marco.be02marbox.domain.movie.Movie;
import prgrms.marco.be02marbox.domain.theater.Schedule;
import prgrms.marco.be02marbox.domain.theater.TheaterRoom;
import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateSchedule;
import prgrms.marco.be02marbox.domain.theater.dto.ResponseFindTime;

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

	public List<LocalDate> convertFromScheduleListToDateList(List<Schedule> scheduleList) {
		return scheduleList.stream()
			.map(schedule -> schedule.getStartTime().toLocalDate())
			.distinct()
			.toList();
	}

	public List<ResponseFindTime> convertFromScheduleListToResponseFindTimeList(List<Schedule> schedules) {
		List<ResponseFindTime> responseFindTimeSchedules = new ArrayList<>();

		Set<TheaterRoom> theaterRooms = schedules.stream()
			.map(Schedule::getTheaterRoom)
			.collect(Collectors.toSet());

		for (TheaterRoom theaterRoom : theaterRooms) {
			List<LocalTime> startTimeListInTheaterRoom =
				getStartTimeListOfSchedulesInTheaterRoom(schedules, theaterRoom);

			responseFindTimeSchedules.add(new ResponseFindTime(theaterRoom.getName(),
				theaterRoom.getTotalAmount(), startTimeListInTheaterRoom));
		}

		return responseFindTimeSchedules;
	}

	private List<LocalTime> getStartTimeListOfSchedulesInTheaterRoom(List<Schedule> schedules,
		TheaterRoom theaterRoom) {
		return schedules.stream()
			.filter(schedule -> schedule.getTheaterRoom().equals(theaterRoom))
			.map(schedule -> schedule.getStartTime().toLocalTime())
			.toList();
	}

}
