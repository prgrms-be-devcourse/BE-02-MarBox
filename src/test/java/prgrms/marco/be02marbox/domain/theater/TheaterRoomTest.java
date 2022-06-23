package prgrms.marco.be02marbox.domain.theater;

import static org.assertj.core.api.Assertions.*;

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
	@DisplayName("TheaterRoom 생성 성공")
	void testValidate_success() {
		Theater theater = new Theater();
		TheaterRoom theaterRoom = TheaterRoom.builder()
			.theater(theater)
			.name("A관")
			.build();
		Set<ConstraintViolation<TheaterRoom>> validate = validator.validate(theaterRoom);
		assertThat(validate).isEmpty();
	}
}
