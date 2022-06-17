package prgrms.marco.be02marbox.domain.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import prgrms.marco.be02marbox.domain.movie.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
