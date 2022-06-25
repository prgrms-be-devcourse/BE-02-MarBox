package prgrms.marco.be02marbox.domain.user.exception;

public class InvalidEmailException extends UserException {

	private final Message message;

	public InvalidEmailException(Message message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message.getMessage();
	}
}
