package prgrms.marco.be02marbox.domain.theater.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static prgrms.marco.be02marbox.domain.theater.dto.document.RequestCreateTheaterRoomDoc.*;

import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateSeat;
import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateTheaterRoom;
import prgrms.marco.be02marbox.domain.theater.dto.document.RequestCreateSeatDoc;
import prgrms.marco.be02marbox.domain.theater.dto.document.RequestCreateTheaterRoomDoc;
import prgrms.marco.be02marbox.domain.theater.service.TheaterRoomService;

@WebMvcTest(controllers = TheaterRoomController.class)
@AutoConfigureRestDocs
class TheaterRoomControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TheaterRoomService theaterRoomService;

	@Autowired
	private ObjectMapper objectMapper;

	private Set<RequestCreateSeat> requestCreateSeats = Set.of(
		new RequestCreateSeat(0, 0),
		new RequestCreateSeat(0, 1)
	);

	private static final String THEATER_ROOM_SAVE_URL = "/theater-rooms";

	@Test
	@DisplayName("관리자는 새로운 상영관을 추가할 수 있다.")
	@WithMockUser(roles = "ADMIN")
	void testSave() throws Exception {
		Long theaterId = 1L;
		RequestCreateTheaterRoom requestDto = new RequestCreateTheaterRoom(theaterId, "A관", requestCreateSeats);
		given(theaterRoomService.save(requestDto)).willReturn(theaterId);

		mockMvc.perform(post(THEATER_ROOM_SAVE_URL)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(requestDto))
		)
			.andExpect(status().isCreated())
			.andExpect(redirectedUrl(THEATER_ROOM_SAVE_URL + "/" + theaterId))
			.andDo(print())
			.andDo(document("theater-room-save",
				requestFields()
					.andWithPrefix(SEAT_LIST_PREFIX.getField(), RequestCreateSeatDoc.get())
					.and(RequestCreateTheaterRoomDoc.get())
				)
			);
	}

	@Test
	@DisplayName("극장 ID의 조회 결과가 없는 경우 400 error를 반환한다.")
	@WithMockUser(roles = "ADMIN")
	void testSave_400() throws Exception {
		Long theaterId = 1L;
		RequestCreateTheaterRoom requestDto = new RequestCreateTheaterRoom(theaterId, "A관", requestCreateSeats);

		given(theaterRoomService.save(requestDto)).willThrow(new EntityNotFoundException("극장 정보를 조회할 수 없습니다."));

		mockMvc.perform(post(THEATER_ROOM_SAVE_URL)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(requestDto))
		)
			.andDo(print())
			.andExpect(status().isBadRequest()
			);
	}
}
