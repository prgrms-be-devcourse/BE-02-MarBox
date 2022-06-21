package prgrms.marco.be02marbox.domain.exception;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestApiControllerAdvice {
	@ExceptionHandler({
		EntityNotFoundException.class
	})
	public ResponseEntity<String> notFoundHandler(Exception exception) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
	}

	@ExceptionHandler({
		Exception.class
	})
	public ResponseEntity<String> internalServerErrorHandler(Exception exception) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
	}
}
