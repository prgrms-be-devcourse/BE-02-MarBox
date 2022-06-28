package prgrms.marco.be02marbox.domain.exception.custom.theater;

public class NotRegisteredRegion extends TheaterException {

	private static final String MESSAGE = "사전에 등록되지 않은 지역입니다";

	public NotRegisteredRegion() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return 400;
	}
}
