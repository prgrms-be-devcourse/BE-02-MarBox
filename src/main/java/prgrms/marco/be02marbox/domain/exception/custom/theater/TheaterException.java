package prgrms.marco.be02marbox.domain.exception.custom.theater;

public abstract class TheaterException extends RuntimeException {
	public TheaterException(String message) {
		super(message);
	}

	public abstract int getStatusCode();
}
