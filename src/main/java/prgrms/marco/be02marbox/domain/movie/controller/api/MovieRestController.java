package prgrms.marco.be02marbox.domain.movie.controller.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import prgrms.marco.be02marbox.domain.movie.service.MovieService;

@RequestMapping("/api/movies")
@RestController
public class MovieRestController {

	private final MovieService movieService;

	public MovieRestController(MovieService movieService) {
		this.movieService = movieService;
	}

	@GetMapping
	public void getMovies(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {
		movieService.getMovies(page, size);
	}
}
