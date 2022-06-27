package prgrms.marco.be02marbox.domain.exception.custom.user;

import prgrms.marco.be02marbox.domain.exception.custom.Message;

public class DuplicateNameException extends UserException {

	private final Message message;

	public DuplicateNameException(Message message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message.getMessage();
	}
}
