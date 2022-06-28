package prgrms.marco.be02marbox.domain.theater.service;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.IntStream;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import prgrms.marco.be02marbox.domain.exception.custom.theater.DuplicateTheaterNameException;
import prgrms.marco.be02marbox.domain.theater.Region;
import prgrms.marco.be02marbox.domain.theater.Theater;
import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateTheater;
import prgrms.marco.be02marbox.domain.theater.dto.ResponseFindTheater;
import prgrms.marco.be02marbox.domain.theater.repository.TheaterRepository;
import prgrms.marco.be02marbox.domain.theater.service.utils.TheaterConverter;

@DataJpaTest
@Import({TheaterService.class, TheaterConverter.class})
class TheaterServiceTest {

	@Autowired
	TheaterRepository theaterRepository;
	@Autowired
	TheaterService theaterService;

	@Test
	@DisplayName("영화관 추가 성공")
	void testCreateTheaterSuccess() {
		// given
		RequestCreateTheater request = new RequestCreateTheater("SEOUL", "CGV 강남점");
		// when
		Long savedTheaterId = theaterService.createTheater(request);
		Theater findTheater = theaterRepository.findById(savedTheaterId)
			.orElseThrow(EntityNotFoundException::new);
		// then
		assertAll(
			() -> assertThat(findTheater.getId()).isEqualTo(savedTheaterId),
			() -> assertThat(findTheater.getRegion()).isEqualTo(Region.valueOf("SEOUL")),
			() -> assertThat(findTheater.getName()).isEqualTo("CGV 강남점")
		);
	}

	@Test
	@DisplayName("영화관 추가 실패 - 잘못된 Region")
	void testCreateTheaterFailed() {
		// given
		String wrongRegion = "NEWYORK";
		RequestCreateTheater request = new RequestCreateTheater(wrongRegion, "CGV 강남점");
		// expected
		assertThatThrownBy(
			() -> theaterService.createTheater(request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("사전에 등록되지 않은 지역입니다");
	}

	@Test
	@DisplayName("영화관 추가 실패 - 중복된 영화관 이름")
	void testCreateTheaterFailedByDuplicateName() {
		// given
		Theater theater = new Theater(Region.from("SEOUL"), "CGV 강남점");
		theaterRepository.save(theater);
		RequestCreateTheater request = new RequestCreateTheater("SEOUL", "CGV 강남점");

		// expected
		assertThatThrownBy(
			() -> theaterService.createTheater(request))
			.isInstanceOf(DuplicateTheaterNameException.class)
			.hasMessageContaining("이미 존재하는 영화관입니다.");

	}

	@Test
	@DisplayName("영화관 단건 조회")
	void testGetOneTheater() {
		// given
		Theater theater = new Theater(Region.from("SEOUL"), "theater1");
		Theater insertedTheater = theaterRepository.save(theater);

		//when
		ResponseFindTheater findTheater = theaterService.findTheater(insertedTheater.getId());

		//then
		assertAll(
			() -> assertThat(findTheater.region()).isEqualTo(insertedTheater.getRegion()),
			() -> assertThat(findTheater.theaterName()).isEqualTo(insertedTheater.getName())
		);
	}

	@Test
	@DisplayName("영화관 단건 조회 - 존재하지 않는 영화관")
	void testGetOneTheaterFailed() {
		// given
		Long wrongId = 1L;

		// expected
		assertThatThrownBy(
			() -> theaterService.findTheater(wrongId))
			.isInstanceOf(EntityNotFoundException.class)
			.hasMessageContaining("극장 정보를 조회할 수 없습니다.");
	}

	@Test
	@DisplayName("관리자 영화관 전체 조회 - 페이징 X")
	void testGetAllTheater() {
		// given
		List<Theater> theaters = IntStream.range(0, 20)
			.mapToObj(i -> new Theater(Region.from("SEOUL"), "theater" + i)).collect(toList());
		theaterRepository.saveAll(theaters);

		// when
		List<ResponseFindTheater> findTheaters = theaterService.findTheaters();

		// then
		assertAll(
			() -> assertThat(findTheaters).hasSize(20),
			() -> assertThat(findTheaters.get(0).region().toString()).isEqualTo("SEOUL"),
			() -> assertThat(findTheaters.get(0).theaterName()).isEqualTo("theater0")
		);
	}

	@Test
	@DisplayName("영화관 지역별 조회")
	void testGetTheatersByRegion() {
		// given
		List<Theater> theatersOfSeoul = IntStream.range(0, 5)
			.mapToObj(i -> new Theater(Region.from("SEOUL"), "theater" + i))
			.collect(toList());
		theaterRepository.saveAll(theatersOfSeoul);

		List<Theater> theatersOfBusan = IntStream.range(0, 5)
			.mapToObj(i -> new Theater(Region.from("BUSAN"), "theater" + i))
			.collect(toList());
		theaterRepository.saveAll(theatersOfBusan);

		// when
		List<ResponseFindTheater> findTheaterOfSeoul = theaterService.findTheaterByRegion("seoul");
		List<ResponseFindTheater> findTheaterOfBusan = theaterService.findTheaterByRegion("busan");

		// then
		assertAll(
			() -> assertThat(findTheaterOfSeoul).hasSize(5),
			() -> assertThat(findTheaterOfSeoul.get(0).region().toString()).isEqualTo("SEOUL"),
			() -> assertThat(findTheaterOfSeoul.get(0).theaterName()).isEqualTo("theater0"),

			() -> assertThat(findTheaterOfBusan).hasSize(5),
			() -> assertThat(findTheaterOfBusan.get(0).region().toString()).isEqualTo("BUSAN"),
			() -> assertThat(findTheaterOfBusan.get(0).theaterName()).isEqualTo("theater0")
		);
	}

}
