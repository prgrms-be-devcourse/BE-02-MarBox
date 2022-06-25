package prgrms.marco.be02marbox.domain.theater.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

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

import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateSchedule;
import prgrms.marco.be02marbox.domain.theater.service.ScheduleService;

@WebMvcTest(controllers = ScheduleController.class)
@AutoConfigureRestDocs
class ScheduleControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ScheduleService scheduleService;

	@Test
	@DisplayName("스케줄 생성 테스트")
	@WithMockUser(roles = "ADMIN")
	void testCreateSchedule() throws Exception {
		RequestCreateSchedule requestCreateSchedule = new RequestCreateSchedule(1L, 1L, LocalDateTime.now(),
			LocalDateTime.now());

		given(scheduleService.createSchedule(requestCreateSchedule)).willReturn(1L);

		mockMvc.perform(post("/schedules")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestCreateSchedule)))
			.andExpect(status().isCreated())
			.andExpect(header().string("location", "schedules/1"))
			.andDo(print());
	}

	@Test
	@DisplayName("상영관 ID가 존재하지 않는 경우 예외 발생")
	@WithMockUser(roles = "ADMIN")
	void testCreateScheduleFail() throws Exception {
		RequestCreateSchedule requestCreateSchedule = new RequestCreateSchedule(100L, 1L, LocalDateTime.now(),
			LocalDateTime.now());

		given(scheduleService.createSchedule(requestCreateSchedule)).willThrow(
			new IllegalArgumentException("존재하지 않는 상영관 ID"));

		mockMvc.perform(post("/schedules")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestCreateSchedule)))
			.andExpect(status().isBadRequest())
			.andDo(print());
	}

}
