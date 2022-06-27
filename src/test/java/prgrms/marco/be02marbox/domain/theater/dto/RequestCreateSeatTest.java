package prgrms.marco.be02marbox.domain.theater.dto;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class RequestCreateSeatTest {

	private static Validator validator;

	@BeforeAll
	static void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	@DisplayName("RequestSeat 생성 성공")
	void testValidate_success() {
		RequestCreateSeat requestCreateSeat = new RequestCreateSeat(0, 0);
		Set<ConstraintViolation<RequestCreateSeat>> validate = validator.validate(requestCreateSeat);
		assertThat(validate).isEmpty();
	}

	@ParameterizedTest
	@DisplayName("RequestSeat 음수는 허용하지 않는다.")
	@CsvSource({"-1, 0", "0, -1"})
	void testValidate_fail(int row, int col) {
		RequestCreateSeat requestCreateSeat = new RequestCreateSeat(row, col);
		Set<ConstraintViolation<RequestCreateSeat>> validate = validator.validate(requestCreateSeat);
		assertThat(validate).hasSize(1);
	}

}
