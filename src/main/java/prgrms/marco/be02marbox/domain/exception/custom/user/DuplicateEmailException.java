package prgrms.marco.be02marbox.domain.exception.custom.user;

import prgrms.marco.be02marbox.domain.exception.custom.Message;

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
