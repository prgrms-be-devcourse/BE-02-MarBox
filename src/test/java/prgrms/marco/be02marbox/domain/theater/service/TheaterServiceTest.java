package prgrms.marco.be02marbox.domain.theater.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import prgrms.marco.be02marbox.domain.converter.TheaterConverter;
import prgrms.marco.be02marbox.domain.theater.Region;
import prgrms.marco.be02marbox.domain.theater.Theater;
import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateTheater;
import prgrms.marco.be02marbox.domain.theater.repository.TheaterRepository;

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
			() -> assertThat(findTheater.getId()).isEqualTo(1L),
			() -> assertThat(findTheater.getRegion()).isEqualTo(Region.valueOf("SEOUL")),
			() -> assertThat(findTheater.getName()).isEqualTo("CGV 강남점")
		);
	}
}
