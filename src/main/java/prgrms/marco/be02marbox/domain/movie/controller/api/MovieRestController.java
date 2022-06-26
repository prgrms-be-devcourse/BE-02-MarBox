package prgrms.marco.be02marbox.domain.movie.controller.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import prgrms.marco.be02marbox.domain.movie.dto.ResponseGetMovies;
import prgrms.marco.be02marbox.domain.movie.service.MovieService;

@RequestMapping("/api/movies")
@RestController
public class MovieRestController {

	private final MovieService movieService;

	public MovieRestController(MovieService movieService) {
		this.movieService = movieService;
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseGetMovies> getMovies(@RequestParam(name = "page", defaultValue = "0") int page,
		@RequestParam(name = "size", defaultValue = "10") int size) {
		return ResponseEntity.ok().body(new ResponseGetMovies(movieService.getMovies(page, size)));
	}
}
