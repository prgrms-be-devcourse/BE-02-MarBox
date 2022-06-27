package prgrms.marco.be02marbox.domain.theater;

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

	private final String regionKor;

	Region(String regionKor) {
		this.regionKor = regionKor;
	}

	public static Region getRegion(String region) {
		return Region.valueOf(region.toUpperCase());
	}
}
