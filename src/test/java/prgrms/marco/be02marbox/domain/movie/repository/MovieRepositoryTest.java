package prgrms.marco.be02marbox.domain.movie.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import prgrms.marco.be02marbox.domain.movie.Genre;
import prgrms.marco.be02marbox.domain.movie.LimitAge;
import prgrms.marco.be02marbox.domain.movie.Movie;

@DataJpaTest
class MovieRepositoryTest {

	@Autowired
	MovieRepository movieRepository;

	@Test
	@DisplayName("Movie를 DB에 저장할 수 있다")
	void testSaveMovie() {
		Movie movie = new Movie("Frozen", LimitAge.CHILD, Genre.ANIMATION, 102L, "resources/posters/frozen.png");
		Movie savedMovie = movieRepository.save(movie);
		Optional<Movie> found = movieRepository.findById(savedMovie.getId());

		assertAll(
			() -> assertThat(found.isPresent()).isTrue(),
			() -> assertThat(found.get().getName()).isEqualTo(movie.getName()),
			() -> assertThat(found.get().getLimitAge()).isEqualTo(movie.getLimitAge()),
			() -> assertThat(found.get().getGenre()).isEqualTo(movie.getGenre()),
			() -> assertThat(found.get().getRunningTime()).isEqualTo(movie.getRunningTime()),
			() -> assertThat(found.get().getPosterImgLocation()).isEqualTo(movie.getPosterImgLocation())
		);
	}
}