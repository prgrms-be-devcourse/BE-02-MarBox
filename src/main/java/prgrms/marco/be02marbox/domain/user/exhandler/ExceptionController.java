package prgrms.marco.be02marbox.domain.user.exhandler;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import prgrms.marco.be02marbox.domain.user.exception.UserException;

@RestControllerAdvice
public class ExceptionController {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, List<String>>> handleClientException(MethodArgumentNotValidException exception) {
		log.error("ClientException : {0}", exception);

		BindingResult bindingResult = exception.getBindingResult();
		List<String> errors = bindingResult.getAllErrors()
			.stream()
			.map(DefaultMessageSourceResolvable::getDefaultMessage)
			.toList();

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(Map.of("message", errors));
	}

	@ExceptionHandler(UserException.class)
	public ResponseEntity<Map<String, String>> handleServiceException(UserException exception) {
		log.error("ServiceException : {0}", exception);

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(Map.of("message", exception.getMessage()));
	}
}
