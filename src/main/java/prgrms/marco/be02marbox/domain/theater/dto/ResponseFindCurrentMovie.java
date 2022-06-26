package prgrms.marco.be02marbox.domain.theater.dto;

import prgrms.marco.be02marbox.domain.movie.Genre;
import prgrms.marco.be02marbox.domain.movie.LimitAge;

public record ResponseFindCurrentMovie(
	String name,
	LimitAge limitAge,
	Genre genre,
	int runningTime,
	String posterImgLocation
) {
}
