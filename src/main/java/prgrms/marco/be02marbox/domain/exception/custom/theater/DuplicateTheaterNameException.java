package prgrms.marco.be02marbox.domain.exception.custom.theater;

public class DuplicateTheaterNameException extends TheaterException {

	private static final String MESSAGE = "이미 존재하는 영화관입니다.";

	public DuplicateTheaterNameException() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return 400;
	}
}
