package prgrms.marco.be02marbox.domain.exception.custom;

public class BadRequestTheater extends RuntimeException {
	public BadRequestTheater(String message) {
		super(message);
	}
}
