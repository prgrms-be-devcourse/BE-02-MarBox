package prgrms.marco.be02marbox.domain.reservation.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static prgrms.marco.be02marbox.domain.theater.dto.document.ResponseFindReservedSeatDoc.*;

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
import prgrms.marco.be02marbox.domain.reservation.dto.ResponseFindReservedSeat;
import prgrms.marco.be02marbox.domain.reservation.service.ReservationService;
import prgrms.marco.be02marbox.domain.reservation.service.ReservedSeatService;
import prgrms.marco.be02marbox.domain.theater.dto.document.ResponseFindReservedSeatDoc;

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

	@MockBean
	private ReservationService reservationService;

	@Autowired
	private ObjectMapper objectMapper;

	private static final String RESERVED_SEAT_URL = "/reserved-seats";

	@Test
	@WithMockUser(roles = {"USER", "ADMIN"})
	@DisplayName("선택 된 스케줄에 예약된 좌석 정보 조회 - id 리스트 반환")
	void testFindReservedIdListByScheduleId() throws Exception {
		Long scheduleId = 1L;

		List<ResponseFindReservedSeat> seatList = List.of(
			new ResponseFindReservedSeat(0, 0, false),
			new ResponseFindReservedSeat(0, 1, true));

		given(reservationService.findReservePossibleSeatList(scheduleId)).willReturn(seatList);

		mockMvc.perform(get(RESERVED_SEAT_URL + "/{scheduleId}", scheduleId)
			.with(SecurityMockMvcRequestPostProcessors.csrf())
		)
			.andExpect(status().isOk())
			.andDo(document("reserved-seat",
				responseFields()
					.andWithPrefix(ARRAY_PREFIX.getField(), ResponseFindReservedSeatDoc.get())
			));
	}
}
