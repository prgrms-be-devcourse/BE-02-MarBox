package prgrms.marco.be02marbox.domain.theater;

public enum Region {
	SEOUL("SEOUL"),
	INCHEON("INCHEON"),
	BUSAN("BUSAN"),
	ULSAN("ULSAN"),
	DAEGU("DAEGU"),
	DAEJEON("DAEJEON"),
	GWANGJU("GWANGJU"),
	GYEONGGI("GYEONGGI"),
	CHUNGCHEONG("CHUNGCHEONG"),
	GANGWON("GANGWON"),
	GYEONGSANG("GYEONGSANG"),
	JEOLLA("JEOLLA"),
	JEJU("JEJU");

	private final String regionEng;

	Region(String regionEng) {
		this.regionEng = regionEng;
	}

	public static Region makeRegion(String region) {
		return Region.valueOf(region.toUpperCase());
	}
}
