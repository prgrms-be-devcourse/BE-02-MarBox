package prgrms.marco.be02marbox.domain.theater;

import java.util.Arrays;

public enum Region {
	SEOUL("서울"),
	INCHEON("인천"),
	BUSAN("부산"),
	ULSAN("울산"),
	DAEGU("대구"),
	DAEJEON("대전"),
	GWANGJU("광주"),
	GYEONGGI("경기"),
	CHUNGCHEONG("충청"),
	GANGWON("강원"),
	GYEONGSANG("경상"),
	JEOLLA("전라"),
	JEJU("제주");

	private static final String INVALID_REGION = "해당 지역은 존재하지 않습니다.";
	private final String regionKor;

	Region(String regionKor) {
		this.regionKor = regionKor;
	}

	public static Region from(String region) {
		validateRegion(region);
		return Region.valueOf(region.toUpperCase());
	}

	private static void validateRegion(String region) {
		if (Arrays
			.stream(Region.values())
			.noneMatch(inputRegion ->
				inputRegion.toString().equalsIgnoreCase(region))) {
			throw new IllegalArgumentException(INVALID_REGION);
		}
	}
}
