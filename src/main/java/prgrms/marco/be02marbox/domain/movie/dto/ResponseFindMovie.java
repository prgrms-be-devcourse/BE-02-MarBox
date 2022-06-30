package prgrms.marco.be02marbox.domain.movie.dto;

import prgrms.marco.be02marbox.domain.movie.Genre;
import prgrms.marco.be02marbox.domain.movie.LimitAge;

public record ResponseFindMovie(
	String name,
	LimitAge limitAge,
	Genre genre,
	int runningTime,
	String posterImgLocation
) {
}
