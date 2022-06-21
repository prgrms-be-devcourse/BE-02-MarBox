package prgrms.marco.be02marbox.domain.user.exception;

public class DuplicateEmailException extends UserException {
	public DuplicateEmailException(String message) {
		super(message);
	}
}
