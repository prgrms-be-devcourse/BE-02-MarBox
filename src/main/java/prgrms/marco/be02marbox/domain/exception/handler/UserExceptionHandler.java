package prgrms.marco.be02marbox.domain.exception.handler;

import static org.springframework.http.HttpStatus.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import prgrms.marco.be02marbox.domain.exception.custom.user.UserException;
import prgrms.marco.be02marbox.domain.exception.dto.ResponseApiError;
import prgrms.marco.be02marbox.domain.user.controller.UserController;

@RestControllerAdvice(assignableTypes = UserController.class)
public class UserExceptionHandler {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ResponseApiError> handleClientException(MethodArgumentNotValidException exception) {
		log.error("ClientException : {0}", exception);

		BindingResult bindingResult = exception.getBindingResult();
		List<String> errors = bindingResult.getAllErrors()
			.stream()
			.map(DefaultMessageSourceResolvable::getDefaultMessage)
			.toList();

		return ResponseEntity
			.status(BAD_REQUEST)
			.body(new ResponseApiError(errors, BAD_REQUEST.value()));
	}

	@ExceptionHandler({UserException.class, BadCredentialsException.class})
	public ResponseEntity<ResponseApiError> handleUserException(Exception exception) {
		log.error("UserException : {0}", exception);

		return ResponseEntity
			.status(BAD_REQUEST)
			.body(new ResponseApiError(List.of(exception.getMessage()), BAD_REQUEST.value()));
	}

	@ExceptionHandler
	public ResponseEntity<ResponseApiError> handleException(Exception exception) {
		log.error("Exception : {0}", exception);

		return ResponseEntity
			.status(INTERNAL_SERVER_ERROR)
			.body(new ResponseApiError(List.of(exception.getMessage()), INTERNAL_SERVER_ERROR.value()));
	}
}
