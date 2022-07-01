package prgrms.marco.be02marbox.domain.reservation.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static prgrms.marco.be02marbox.domain.theater.dto.document.ResponseCreateSeatDoc.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import prgrms.marco.be02marbox.config.WebSecurityConfigure;
import prgrms.marco.be02marbox.domain.reservation.service.ReservedSeatService;
import prgrms.marco.be02marbox.domain.theater.dto.ResponseFindSeat;
import prgrms.marco.be02marbox.domain.theater.dto.document.ResponseCreateSeatDoc;

@WebMvcTest(controllers = ReservedSeatController.class,
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigure.class)
	}
)
@AutoConfigureRestDocs
class ReservedSeatControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ReservedSeatService reservedSeatService;

	@Autowired
	private ObjectMapper objectMapper;

	private static final String RESERVED_SEAT_URL = "/reserved-seat";

	@Test
	@WithMockUser(roles = {"USER", "ADMIN"})
	@DisplayName("선택 된 스케줄에 예약된 좌석 정보 조회")
	void testFindByScheduleId() throws Exception {
		Long scheduleId = 1L;

		List<ResponseFindSeat> seatList = List.of(
			new ResponseFindSeat(0, 0),
			new ResponseFindSeat(0, 1),
			new ResponseFindSeat(0, 2)
		);

		given(reservedSeatService.findByScheduleId(scheduleId)).willReturn(seatList);

		mockMvc.perform(get(RESERVED_SEAT_URL + "/{scheduleId}", scheduleId)
			.with(SecurityMockMvcRequestPostProcessors.csrf())
		)
			.andExpect(status().isOk())
			.andDo(document("reserved-seat",
				responseFields()
					.andWithPrefix(ARRAY_PREFIX.getField(), ResponseCreateSeatDoc.get())
			));
	}

	@Test
	@WithMockUser(roles = {"USER", "ADMIN"})
	@DisplayName("선택 된 스케줄에 예약된 좌석 정보 조회")
	void testFindReservePossibleSeatsd() throws Exception {
		Long scheduleId = 1L;

		List<ResponseFindSeat> seatList = List.of(
			new ResponseFindSeat(0, 0),
			new ResponseFindSeat(0, 1)
		);

		given(reservedSeatService.findReservePossibleSeats(scheduleId)).willReturn(seatList);

		mockMvc.perform(get(RESERVED_SEAT_URL + "/possible/{scheduleId}", scheduleId)
			.with(SecurityMockMvcRequestPostProcessors.csrf())
		)
			.andExpect(status().isOk())
			.andDo(document("reserved-possible-seat",
				responseFields()
					.andWithPrefix(ARRAY_PREFIX.getField(), ResponseCreateSeatDoc.get())
			));
	}
}
