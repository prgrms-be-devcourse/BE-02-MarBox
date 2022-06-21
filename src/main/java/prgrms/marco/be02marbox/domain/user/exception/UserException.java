package prgrms.marco.be02marbox.domain.user.exception;

public abstract class UserException extends RuntimeException {
	public UserException(String message) {
		super(message);
	}
}
