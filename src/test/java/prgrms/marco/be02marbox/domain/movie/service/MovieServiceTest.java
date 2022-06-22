package prgrms.marco.be02marbox.domain.movie.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import prgrms.marco.be02marbox.domain.movie.Genre;
import prgrms.marco.be02marbox.domain.movie.LimitAge;
import prgrms.marco.be02marbox.domain.movie.Movie;
import prgrms.marco.be02marbox.domain.movie.dto.CreateMovie;
import prgrms.marco.be02marbox.domain.movie.repository.MovieRepository;

@DataJpaTest
@Import({MovieService.class})
class MovieServiceTest {

	@Autowired
	MovieService movieService;

	@Autowired
	MovieRepository movieRepository;

	@Test
	@DisplayName("movie를 추가할 수 있다")
	void testCreateMovie() {
		CreateMovie frozen = new CreateMovie("Frozen", LimitAge.CHILD, Genre.ANIMATION, 102, "frozen.png");
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
}
