package prgrms.marco.be02marbox.domain.theater.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import prgrms.marco.be02marbox.domain.reservation.repository.RepositoryTestUtil;
import prgrms.marco.be02marbox.domain.theater.dto.ResponseFindTheaterRoom;
import prgrms.marco.be02marbox.domain.theater.service.utils.TheaterConverter;
import prgrms.marco.be02marbox.domain.theater.service.utils.TheaterRoomConverter;

@Import({TheaterRoomService.class, TheaterRoomConverter.class, TheaterConverter.class})
class TheaterRoomServiceTest extends RepositoryTestUtil {

	@Autowired
	TheaterRoomService theaterRoomService;

	@Test
	@DisplayName("전체 상영관 정보 조회")
	void testFindAll() {
		saveTheaterRoom("A관");
		saveTheaterRoom("B관");

		List<ResponseFindTheaterRoom> theaterRoomList = theaterRoomService.findAll();
		assertAll(
			() -> assertThat(theaterRoomList).hasSize(2),
			() -> assertThat(theaterRoomList.get(0).totalCount()).isZero(),
			() -> assertThat(theaterRoomList.get(1).totalCount()).isZero()
		);
	}
}

