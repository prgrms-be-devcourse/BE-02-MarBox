package prgrms.marco.be02marbox.domain.movie.service.utils;

import prgrms.marco.be02marbox.domain.movie.Movie;
import prgrms.marco.be02marbox.domain.movie.dto.CreateMovie;

public class MovieDtoConverter {

	public static Movie convertToMovie(CreateMovie createMovie) {
		return new Movie(createMovie.name(),
			createMovie.limitAge(),
			createMovie.genre(),
			createMovie.runningTime(),
			createMovie.originalFileName());
	}
}
