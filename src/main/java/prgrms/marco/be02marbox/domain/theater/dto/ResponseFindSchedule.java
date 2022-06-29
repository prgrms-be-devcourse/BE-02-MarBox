package prgrms.marco.be02marbox.domain.theater.dto;

import java.time.LocalDate;
import java.util.List;

import prgrms.marco.be02marbox.domain.movie.dto.ResponseFindMovie;

public record ResponseFindSchedule(
	List<ResponseFindMovie> movieList,
	List<ResponseFindTheater> theaterList,
	List<LocalDate> dateList,
	List<ResponseFindTime> timeList
) {
}
