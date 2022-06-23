package prgrms.marco.be02marbox.domain.movie.service.utils;

import org.springframework.stereotype.Component;

import prgrms.marco.be02marbox.domain.movie.Movie;
import prgrms.marco.be02marbox.domain.movie.dto.RequestCreateMovie;

@Component
public class MovieConverter {

	public Movie convertFromRequestCreateMovieToMovie(RequestCreateMovie requestCreateMovie) {
		return new Movie(requestCreateMovie.name(),
			requestCreateMovie.limitAge(),
			requestCreateMovie.genre(),
			requestCreateMovie.runningTime(),
			requestCreateMovie.originalFileName());
	}
}
