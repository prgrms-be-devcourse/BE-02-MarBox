package prgrms.marco.be02marbox.domain.theater.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import prgrms.marco.be02marbox.config.WebSecurityConfigure;
import prgrms.marco.be02marbox.domain.movie.Genre;
import prgrms.marco.be02marbox.domain.movie.LimitAge;
import prgrms.marco.be02marbox.domain.movie.dto.ResponseFindCurrentMovie;
import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateSchedule;
import prgrms.marco.be02marbox.domain.theater.dto.ResponseFindMovieAndDate;
import prgrms.marco.be02marbox.domain.theater.service.ScheduleService;

@WebMvcTest(controllers = ScheduleController.class,
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigure.class)
)
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
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestCreateSchedule)))
			.andExpect(status().isCreated())
			.andExpect(header().string("location", "schedules/1"))
			.andDo(document("schedule-save",
				requestFields(
					fieldWithPath("theaterRoomId").type(JsonFieldType.NUMBER).description("상영관 ID"),
					fieldWithPath("movieId").type(JsonFieldType.NUMBER).description("영화 ID"),
					fieldWithPath("startTime").type(JsonFieldType.STRING).description("영화 시작 날짜와 시간"),
					fieldWithPath("endTime").type(JsonFieldType.STRING).description("영화 종료 날짜와 시간")
				)));

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
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestCreateSchedule)))
			.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("현재 상영중인 영화 리스트 반환 테스트")
	@WithMockUser(roles = {"ADMIN", "USER"})
	void testGetCurrentMovieList() throws Exception {

		List<ResponseFindCurrentMovie> currentMovieList = List.of(
			new ResponseFindCurrentMovie("테스트1", LimitAge.CHILD, Genre.ACTION, 100, "test/location"),
			new ResponseFindCurrentMovie("테스트2", LimitAge.CHILD, Genre.ROMANCE, 150, "test/location"),
			new ResponseFindCurrentMovie("테스트3", LimitAge.ADULT, Genre.ACTION, 120, "test/location"));

		given(scheduleService.findCurrentMovieList()).willReturn(currentMovieList);

		mockMvc.perform(get("/schedules/current-movies"))
			.andExpect(status().isOk())
			.andDo(document("schedule-get-current-movies",
				responseFields(
					fieldWithPath("[].name").type(JsonFieldType.STRING).description("영화 이름"),
					fieldWithPath("[].limitAge").type(JsonFieldType.STRING).description("연령 제한"),
					fieldWithPath("[].genre").type(JsonFieldType.STRING).description("장르"),
					fieldWithPath("[].runningTime").type(JsonFieldType.NUMBER).description("상영시간"),
					fieldWithPath("[].posterImgLocation").type(JsonFieldType.STRING).description("포스터 이미지 경로")
				)));
	}

	@Test
	@DisplayName("영화관 ID로 요청하면 영화와 날짜 리스트 반환 테스트")
	@WithMockUser(roles = {"ADMIN", "USER"})
	void testGetMovieAndDateListInOneTheater() throws Exception {
		List<ResponseFindMovieAndDate> movieAndDateList = List.of(
			new ResponseFindMovieAndDate("영화1", LocalDate.now()),
			new ResponseFindMovieAndDate("영화2", LocalDate.now()),
			new ResponseFindMovieAndDate("영화1", LocalDate.now().plusDays(1))
		);

		given(scheduleService.findMovieAndDateWithTheaterId(1L)).willReturn(movieAndDateList);

		mockMvc.perform(get("/schedules")
				.param("theaterId", "1"))
			.andExpect(status().isOk())
			.andDo(document("schedule-get-movie-and-date-in-theater",
				responseFields(
					fieldWithPath("[].movieName").type(JsonFieldType.STRING).description("영화 이름"),
					fieldWithPath("[].date").type(JsonFieldType.STRING).description("상영 날짜")
				)));
	}

}
