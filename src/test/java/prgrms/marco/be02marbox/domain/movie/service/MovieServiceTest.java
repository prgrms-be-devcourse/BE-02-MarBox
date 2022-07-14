package prgrms.marco.be02marbox.domain.movie.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.amazonaws.services.s3.AmazonS3Client;

import prgrms.marco.be02marbox.aws.S3Upload;
import prgrms.marco.be02marbox.config.QueryDslConfig;
import prgrms.marco.be02marbox.domain.movie.Genre;
import prgrms.marco.be02marbox.domain.movie.LimitAge;
import prgrms.marco.be02marbox.domain.movie.Movie;
import prgrms.marco.be02marbox.domain.movie.repository.MovieRepository;
import prgrms.marco.be02marbox.domain.movie.service.utils.MovieConverter;

@DataJpaTest
@Import({MovieService.class, MovieConverter.class, S3Upload.class, AmazonS3Client.class, QueryDslConfig.class})
class MovieServiceTest {

	@Autowired
	MovieService movieService;

	@Autowired
	MovieRepository movieRepository;

	@PersistenceContext
	EntityManager em;

	Movie frozen = new Movie("Frozen", LimitAge.CHILD, Genre.ANIMATION, 102);
	Movie notebook = new Movie("NoteBook", LimitAge.CHILD, Genre.ROMANCE, 124);
	Movie matrix = new Movie("Matrix", LimitAge.CHILD, Genre.ACTION, 136);
	Movie tangled = new Movie("tangled", LimitAge.CHILD, Genre.ANIMATION, 148);
	Movie tazza = new Movie("tazza", LimitAge.ADULT, Genre.ACTION, 186);
	Movie aboutTime = new Movie("About Time", LimitAge.CHILD, Genre.ROMANCE, 124);

	@Test
	@DisplayName("movie 리스트를 조회할 수 있다")
	void testGetMovies() {
		movieRepository.save(frozen);
		movieRepository.save(notebook);
		movieRepository.save(matrix);
		movieRepository.save(tangled);
		movieRepository.save(tazza);
		movieRepository.save(aboutTime);
		em.flush();
		em.clear();

		List<Movie> movies1 = movieService.getMovies(0, 5);
		List<Movie> movies2 = movieService.getMovies(1, 5);
		List<Movie> movies3 = movieService.getMovies(2, 5);

		assertAll(
			() -> assertThat(movies1).hasSize(5),
			() -> assertThat(movies2).hasSize(1),
			() -> assertThat(movies3).isEmpty()
		);
	}
}
