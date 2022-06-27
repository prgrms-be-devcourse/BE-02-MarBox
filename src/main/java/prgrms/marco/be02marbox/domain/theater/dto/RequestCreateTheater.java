package prgrms.marco.be02marbox.domain.theater.dto;

import javax.validation.constraints.NotBlank;

public record RequestCreateTheater(
	@NotBlank(message = "지역명을 입력해주세요.") String region,
	@NotBlank(message = "영화관 이름을 입력해주세요.") String name) {
}
