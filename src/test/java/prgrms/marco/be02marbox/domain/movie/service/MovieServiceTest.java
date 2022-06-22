package prgrms.marco.be02marbox.domain.movie.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import prgrms.marco.be02marbox.domain.movie.Genre;
import prgrms.marco.be02marbox.domain.movie.LimitAge;
import prgrms.marco.be02marbox.domain.movie.Movie;
import prgrms.marco.be02marbox.domain.movie.dto.CreateMovie;

@DataJpaTest
@Import({MovieService.class})
class MovieServiceTest {

	@Autowired
	MovieService movieService;

	@Test
	@DisplayName("movie를 추가할 수 있다")
	void testCreateMovie() {
		CreateMovie frozen = new CreateMovie("Frozen", LimitAge.CHILD, Genre.ANIMATION, 102, "frozen.png");
		Movie movie = movieService.createMovie(frozen);
		assertAll(
			() -> assertThat(movie.getId()).isNotNull(),
			() -> assertThat(movie.getName()).isEqualTo(frozen.name()),
			() -> assertThat(movie.getLimitAge()).isEqualTo(frozen.limitAge()),
			() -> assertThat(movie.getGenre()).isEqualTo(frozen.genre()),
			() -> assertThat(movie.getRunningTime()).isEqualTo(frozen.runningTime())
		);
	}
}
