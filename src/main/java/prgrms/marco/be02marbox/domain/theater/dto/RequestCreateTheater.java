package prgrms.marco.be02marbox.domain.theater.dto;

import javax.validation.constraints.NotBlank;

public record RequestCreateTheater(
	@NotBlank(message = "지역명을 입력해주세요.") String region,
	@NotBlank(message = "영화관 이름을 입력해주세요.") String name) {

	public RequestCreateTheater(String region, String name) {
		this.region = region;
		this.name = name;
	}

	@Override
	public String region() {
		return region;
	}

	@Override
	public String name() {
		return name;
	}
}
