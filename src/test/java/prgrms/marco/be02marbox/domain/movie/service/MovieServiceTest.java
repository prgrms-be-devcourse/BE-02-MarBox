package prgrms.marco.be02marbox.domain.movie.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;

import prgrms.marco.be02marbox.domain.movie.Genre;
import prgrms.marco.be02marbox.domain.movie.LimitAge;
import prgrms.marco.be02marbox.domain.movie.Movie;
import prgrms.marco.be02marbox.domain.movie.dto.RequestCreateMovie;
import prgrms.marco.be02marbox.domain.movie.repository.MovieRepository;
import prgrms.marco.be02marbox.domain.movie.service.utils.MovieConverter;

@DataJpaTest
@Import({MovieService.class, MovieConverter.class})
class MovieServiceTest {

	@Autowired
	MovieService movieService;

	@Autowired
	MovieRepository movieRepository;

	static RequestCreateMovie frozen;
	static RequestCreateMovie notebook;
	static RequestCreateMovie matrix;
	static RequestCreateMovie tangled;
	static RequestCreateMovie tazza;
	static RequestCreateMovie aboutTime;


	@BeforeAll
	static void beforeAll() {
		frozen = new RequestCreateMovie("Frozen", LimitAge.CHILD, Genre.ANIMATION, 102,
			new MockMultipartFile("fronzen.tmp", "fronzen.tmp", "tmp", "fronzen poster".getBytes()));
		notebook = new RequestCreateMovie("NoteBook", LimitAge.CHILD, Genre.ROMANCE, 124,
			new MockMultipartFile("notebook.tmp", "notebook.tmp", "tmp", "notebook poster".getBytes()));
		matrix = new RequestCreateMovie("Matrix", LimitAge.CHILD, Genre.ACTION, 136,
			new MockMultipartFile("matrix.tmp", "matrix.tmp", "tmp", "matrix poster".getBytes()));
		tangled = new RequestCreateMovie("tangled", LimitAge.CHILD, Genre.ANIMATION, 148,
			new MockMultipartFile("tangled.tmp", "tangled.tmp", "tmp", "tangled poster".getBytes()));
		tazza = new RequestCreateMovie("tazza", LimitAge.ADULT, Genre.ACTION, 186,
			new MockMultipartFile("tazza.tmp", "tazza.tmp", "tmp", "tazza poster".getBytes()));
		aboutTime = new RequestCreateMovie("About Time", LimitAge.CHILD, Genre.ROMANCE, 124,
			new MockMultipartFile("about_time.tmp", "about_time.tmp", "tmp", "about time poster".getBytes()));
	}

	@Test
	@DisplayName("movie를 추가할 수 있다")
	void testCreateMovie() throws IOException {
		Long movieId = movieService.createMovie(frozen);
		Optional<Movie> found = movieRepository.findById(movieId);

		assertAll(
			() -> assertThat(found.isPresent()).isTrue(),
			() -> assertThat(found.get().getName()).isEqualTo(frozen.name()),
			() -> assertThat(found.get().getLimitAge()).isEqualTo(frozen.limitAge()),
			() -> assertThat(found.get().getGenre()).isEqualTo(frozen.genre()),
			() -> assertThat(found.get().getRunningTime()).isEqualTo(frozen.runningTime())
		);
	}

	@Test
	@DisplayName("movie 리스트를 조회할 수 있다")
	void testGetMovies() throws IOException {
		movieService.createMovie(frozen);
		movieService.createMovie(notebook);
		movieService.createMovie(matrix);
		movieService.createMovie(tangled);
		movieService.createMovie(tazza);
		movieService.createMovie(aboutTime);

		List<Movie> movies1 = movieService.getMovies(0, 5);
		List<Movie> movies2 = movieService.getMovies(1, 5);
		List<Movie> movies3 = movieService.getMovies(2, 5);

		assertAll(
			() -> assertThat(movies1).hasSize(5),
			() -> assertThat(movies2).hasSize(1),
			() -> assertThat(movies3).hasSize(0)
		);
	}
}
