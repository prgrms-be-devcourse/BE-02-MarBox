package prgrms.marco.be02marbox.domain.movie.service.utils;

import org.springframework.stereotype.Component;

import prgrms.marco.be02marbox.domain.movie.Movie;
import prgrms.marco.be02marbox.domain.movie.dto.RequestCreateMovie;
import prgrms.marco.be02marbox.domain.movie.dto.ResponseFindMovie;

@Component
public class MovieConverter {

	/**
	 * RequestCreateMovie Dto를 Movie Entity로 변환
	 *
	 * @param requestCreateMovie
	 * @return Movie
	 */
	public Movie convertFromRequestCreateMovieToMovie(RequestCreateMovie requestCreateMovie) {
		return new Movie(requestCreateMovie.name(),
			requestCreateMovie.limitAge(),
			requestCreateMovie.genre(),
			requestCreateMovie.runningTime(),
			requestCreateMovie.originalFileName());
	}

	/**
	 * Movie 객체를 ResponseFindMovie 객체로 변환
	 *
	 * @param movie
	 * @return ResponseFindMovie
	 */
	public ResponseFindMovie convertFromMovieToResponseFindMovie(Movie movie) {
		return new ResponseFindMovie(movie.getName(),
			movie.getLimitAge(),
			movie.getGenre(),
			movie.getRunningTime(),
			movie.getPosterImgLocation());
	}
}
