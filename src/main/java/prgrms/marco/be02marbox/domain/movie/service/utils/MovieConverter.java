package prgrms.marco.be02marbox.domain.movie.service.utils;

import org.springframework.stereotype.Component;

import prgrms.marco.be02marbox.domain.movie.Movie;
import prgrms.marco.be02marbox.domain.movie.dto.RequestCreateMovie;
import prgrms.marco.be02marbox.domain.movie.dto.ResponseFindCurrentMovie;

@Component
public class MovieConverter {

	public Movie convertFromRequestCreateMovieToMovie(RequestCreateMovie requestCreateMovie) {
		return new Movie(requestCreateMovie.name(),
			requestCreateMovie.limitAge(),
			requestCreateMovie.genre(),
			requestCreateMovie.runningTime(),
			requestCreateMovie.originalFileName());
	}

	public ResponseFindCurrentMovie convertFromMovieToResponseFindCurrentMovie(Movie movie) {
		return new ResponseFindCurrentMovie(movie.getName(), movie.getLimitAge(), movie.getGenre(),
			movie.getRunningTime(), movie.getPosterImgLocation());
	}
}
