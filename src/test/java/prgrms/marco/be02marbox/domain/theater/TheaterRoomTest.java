package prgrms.marco.be02marbox.domain.theater;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TheaterRoomTest {
	private static Validator validator;

	@BeforeAll
	static void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	@DisplayName("좌석정보(seats)는 1개 이상 등록되어야 한다.")
	void testValidate_seats() {
		Theater theater = new Theater();
		TheaterRoom theaterRoom = new TheaterRoom(theater, "name", new ArrayList<>());

		Set<ConstraintViolation<TheaterRoom>> validate = validator.validate(theaterRoom);
		assertThat(validate).hasSize(1);
	}

	@Test
	@DisplayName("TheaterRoom 생성 성공")
	void testValidate_success() {
		Theater theater = new Theater();
		List<Seat> seats = new ArrayList<>();
		seats.add(new Seat());
		seats.add(new Seat());
		TheaterRoom theaterRoom = new TheaterRoom(theater, "A관", seats);

		Set<ConstraintViolation<TheaterRoom>> validate = validator.validate(theaterRoom);
		assertThat(validate).isEmpty();
	}
}
