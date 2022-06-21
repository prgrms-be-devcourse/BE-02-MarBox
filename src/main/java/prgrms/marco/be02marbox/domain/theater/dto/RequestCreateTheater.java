package prgrms.marco.be02marbox.domain.theater.dto;

import javax.validation.constraints.NotBlank;

public class RequestCreateTheater {

	@NotBlank(message = "지역명을 입력해주세요.")
	private final String region;
	@NotBlank(message = "영화관 이름을 입력해주세요.")
	private final String name;

	public RequestCreateTheater(String region, String name) {
		this.region = region;
		this.name = name;
	}

	public String getRegion() {
		return region;
	}

	public String getName() {
		return name;
	}

}