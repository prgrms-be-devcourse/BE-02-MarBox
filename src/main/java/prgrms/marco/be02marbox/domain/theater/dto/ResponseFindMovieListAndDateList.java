package prgrms.marco.be02marbox.domain.theater.dto;

import java.time.LocalDate;
import java.util.List;

public record ResponseFindMovieListAndDateList(
	List<String> movieList,
	List<LocalDate> dateList
) {
}
