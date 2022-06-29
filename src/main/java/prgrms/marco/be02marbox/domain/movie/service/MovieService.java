package prgrms.marco.be02marbox.domain.movie.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prgrms.marco.be02marbox.domain.movie.Movie;
import prgrms.marco.be02marbox.domain.movie.dto.RequestCreateMovie;
import prgrms.marco.be02marbox.domain.movie.repository.MovieRepository;
import prgrms.marco.be02marbox.domain.movie.service.utils.MovieConverter;

@Service
public class MovieService {

	private final MovieRepository movieRepository;

	private final MovieConverter movieConverter;

	public MovieService(MovieRepository movieRepository,
		MovieConverter movieConverter) {
		this.movieRepository = movieRepository;
		this.movieConverter = movieConverter;
	}

	/**
	 * movie 테이블에 영화 데이터를 추가한다
	 *
	 * @param
	 * 	requestCreateMovie: 사용자의 Movie 생성 요청
	 * @return Movie가 생성되고 나서의 Id
	 */
	@Transactional
	public Long createMovie(RequestCreateMovie requestCreateMovie) {
		Movie newMovie = movieConverter.convertFromRequestCreateMovieToMovie(requestCreateMovie);
		return movieRepository
			.save(newMovie)
			.getId();
	}

	/**
	 * 관리자에게 요청한 Movie들이 데이터를 보여준다
	 *
	 * @param page: Movie 데이터를 조회 페이지
	 * @param size: 읽을 Movie 데이터 사이즈
	 * @return Movie List
	 */
	@Transactional(readOnly = true)
	public List<Movie> getMovies(int page, int size) {
		return movieRepository
			.findAll(PageRequest.of(page, size))
			.getContent();
	}
}
