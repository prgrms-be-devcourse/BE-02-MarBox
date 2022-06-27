package prgrms.marco.be02marbox.domain.theater.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import prgrms.marco.be02marbox.config.WebSecurityConfigure;
import prgrms.marco.be02marbox.domain.theater.Region;
import prgrms.marco.be02marbox.domain.theater.Theater;
import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateTheater;
import prgrms.marco.be02marbox.domain.theater.dto.ResponseFindTheater;
import prgrms.marco.be02marbox.domain.theater.service.TheaterService;

@WebMvcTest(controllers = TheaterController.class,
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigure.class))
@AutoConfigureMockMvc
class TheaterControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private TheaterService theaterService;

	@Test
	@DisplayName("영화관 단건 조회 테스트")
	@WithMockUser(roles = "ADMIN")
	void testGetOneTheater() throws Exception {
		// given
		Long theaterId = 1L;

		// when
		given(theaterService.findTheater(theaterId)).willReturn(
			new ResponseFindTheater(Region.getRegion("SEOUL"), "theater0"));

		// then
		mockMvc.perform(get("/theaters/{theaterId}", theaterId)
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.region").value("SEOUL"))
			.andExpect(jsonPath("$.theaterName").value("theater0"))
			.andDo(print());
	}

	@Test
	@DisplayName("영화관 전체 조회 테스트")
	@WithMockUser(roles = "ADMIN")
	void testGetTheaters() throws Exception {
		// given
		List<Theater> request = IntStream.range(0, 10)
			.mapToObj(i ->
				new Theater(Region.getRegion("SEOUL"), "theater" + i))
			.toList();

		List<ResponseFindTheater> response = request.stream()
			.map(theater -> new ResponseFindTheater(theater.getRegion(), theater.getName()))
			.collect(Collectors.toList());

		given(theaterService.findTheaters()).willReturn(response);

		// expected
		mockMvc.perform(get("/theaters")
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	@DisplayName("영화관 생성 테스트")
	@WithMockUser(roles = "ADMIN")
	void testSaveTheater() throws Exception {
		// given
		RequestCreateTheater request = new RequestCreateTheater("SEOUL", "theater0");
		given(theaterService.createTheater(request)).willReturn(1L);
		given(theaterService.findTheater(1L)).willReturn(
			new ResponseFindTheater(Region.getRegion(request.region()), request.name()));

		// then
		mockMvc.perform(post("/theaters")
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.region").value("SEOUL"))
			.andExpect(jsonPath("$.theaterName").value("theater0"))
			.andDo(print());
	}

	@Test
	@DisplayName("영화관 지역별 조회 테스트")
	@WithMockUser(roles = "ADMIN")
	void getTheater() throws Exception {
		// given
		RequestCreateTheater requestSeoul = new RequestCreateTheater("SEOUL", "theater0");
		RequestCreateTheater requestBusan = new RequestCreateTheater("BUSAN", "theater0");
		theaterService.createTheater(requestSeoul);
		theaterService.createTheater(requestBusan);

		given(theaterService.findTheaterByRegion("seoul")).willReturn(
			List.of(new ResponseFindTheater(Region.SEOUL, requestBusan.name())));

		mockMvc.perform(get("/theaters/region")
				.param("name", "seoul")
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(print());
	}
}
