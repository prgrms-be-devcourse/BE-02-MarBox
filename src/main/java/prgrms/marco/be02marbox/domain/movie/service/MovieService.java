package prgrms.marco.be02marbox.domain.movie.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prgrms.marco.be02marbox.aws.S3Upload;
import prgrms.marco.be02marbox.domain.movie.Movie;
import prgrms.marco.be02marbox.domain.movie.dto.RequestCreateMovie;
import prgrms.marco.be02marbox.domain.movie.repository.MovieRepository;
import prgrms.marco.be02marbox.domain.movie.service.utils.MovieConverter;

@Service
public class MovieService {

	private final MovieRepository movieRepository;
	private final MovieConverter movieConverter;
	private final S3Upload s3Upload;

	public MovieService(MovieRepository movieRepository,
		MovieConverter movieConverter, S3Upload s3Upload) {
		this.movieRepository = movieRepository;
		this.movieConverter = movieConverter;
		this.s3Upload = s3Upload;
	}

	/**
	 * movie 테이블에 영화 데이터를 추가한다
	 * 포스터는 S3 저장소에 저장된다.
	 *
	 * @param
	 *    requestCreateMovie: 사용자의 Movie 생성 요청
	 * @return Movie가 생성되고 나서의 Id
	 */
	@Transactional
	public Long createMovie(RequestCreateMovie requestCreateMovie) throws IOException {
		Movie movie = movieConverter.convertFromRequestCreateMovieToMovie(requestCreateMovie);
		Long movieId = movieRepository.save(movie).getId();
		movie.updatePosterImgLocation(s3Upload.upload(requestCreateMovie.poster(), movieId));
		return movieId;
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
