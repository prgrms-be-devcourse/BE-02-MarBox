package prgrms.marco.be02marbox.domain.user.exhandler;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import prgrms.marco.be02marbox.domain.user.controller.UserController;
import prgrms.marco.be02marbox.domain.user.exception.UserException;

@RestControllerAdvice(assignableTypes = UserController.class)
public class ExceptionController {

	public static final String MESSAGE = "message";

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
			.body(Map.of(MESSAGE, errors));
	}

	@ExceptionHandler({UserException.class, BadCredentialsException.class})
	public ResponseEntity<Map<String, String>> handleUserException(Exception exception) {
		log.error("UserException : {0}", exception);

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(Map.of(MESSAGE, exception.getMessage()));
	}

	@ExceptionHandler
	public ResponseEntity<Map<String, String>> handleException(Exception exception) {
		log.error("Exception : {0}", exception);

		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(Map.of(MESSAGE, exception.getMessage()));
	}
}
