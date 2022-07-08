package prgrms.marco.be02marbox.domain.movie.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import prgrms.marco.be02marbox.domain.movie.Movie;
import prgrms.marco.be02marbox.domain.movie.dto.RequestCreateMovie;
import prgrms.marco.be02marbox.domain.movie.repository.MovieRepository;
import prgrms.marco.be02marbox.domain.movie.service.utils.MovieConverter;

@Service
public class MovieService {

	private final MovieRepository movieRepository;

	private final MovieConverter movieConverter;

	private static final String posterDirectory = "posters";

	public MovieService(MovieRepository movieRepository,
		MovieConverter movieConverter) {
		this.movieRepository = movieRepository;
		this.movieConverter = movieConverter;
	}

	/**
	 * movie 테이블에 영화 데이터를 추가한다
	 *
	 * @param
	 *    requestCreateMovie: 사용자의 Movie 생성 요청
	 * @return Movie가 생성되고 나서의 Id
	 */
	@Transactional
	public Long createMovie(RequestCreateMovie requestCreateMovie) throws IOException {
		Movie movie = movieConverter.convertFromRequestCreateMovieToMovie(requestCreateMovie);
		Long movieId = movieRepository.save(movie).getId();
		movie.updatePosterImgLocation(savePoster(requestCreateMovie.poster(), movieId));
		return movieId;
	}

	private String savePoster(MultipartFile poster, Long movieId) throws IOException {
		makeDirectory();

		String posterPath = generateNewFileName(poster, movieId);
		File posterFile = new File(posterPath);

		InputStream initialStream = poster.getInputStream();
		byte[] buffer = new byte[initialStream.available()];
		initialStream.read(buffer);

		try (OutputStream outputStream = new FileOutputStream(posterFile)) {
			outputStream.write(buffer);
		}

		return posterPath;
	}

	private String generateNewFileName(MultipartFile poster, Long movieId) {
		return new StringBuilder(posterDirectory)
			.append("/")
			.append(movieId)
			.append(getFileExtension(poster.getOriginalFilename()))
			.toString();
	}

	private String getFileExtension(String originalFileName) {
		return originalFileName.substring(originalFileName.lastIndexOf('.'));
	}

	private void makeDirectory() {
		File dir = new File(posterDirectory);
		if (!dir.exists()) {
			dir.mkdir();
		}
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
