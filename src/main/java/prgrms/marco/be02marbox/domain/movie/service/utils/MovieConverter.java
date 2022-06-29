package prgrms.marco.be02marbox.domain.movie.service.utils;

import org.springframework.stereotype.Component;

import prgrms.marco.be02marbox.domain.movie.Movie;
import prgrms.marco.be02marbox.domain.movie.dto.RequestCreateMovie;
import prgrms.marco.be02marbox.domain.movie.dto.ResponseFindCurrentMovie;

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
			requestCreateMovie.runningTime());
	}

	/**
	 * Movie 객체를 ResponseFindCurrentMovie 객체로 변환
	 *
	 * @param movie
	 * @return ResponseFindCurrentMovie
	 */
	public ResponseFindCurrentMovie convertFromMovieToResponseFindCurrentMovie(Movie movie) {
		return new ResponseFindCurrentMovie(movie.getName(),
			movie.getLimitAge(),
			movie.getGenre(),
			movie.getRunningTime(),
			movie.getPosterImgLocation());
	}
}
