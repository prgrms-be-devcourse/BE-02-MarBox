package prgrms.marco.be02marbox.domain.user.exception;

public class DuplicateEmailException extends UserException {

	private final Message message;

	public DuplicateEmailException(Message message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message.getMessage();
	}
}
