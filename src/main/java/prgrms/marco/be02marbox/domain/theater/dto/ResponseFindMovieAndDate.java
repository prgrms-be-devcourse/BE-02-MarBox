package prgrms.marco.be02marbox.domain.theater.dto;

import java.time.LocalDate;

public record ResponseFindMovieAndDate(
	String movieName,
	LocalDate date
) {
}
