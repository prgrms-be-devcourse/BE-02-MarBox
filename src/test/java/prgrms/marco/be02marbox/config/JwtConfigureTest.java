package prgrms.marco.be02marbox.config;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = JwtConfigure.class)
@TestPropertySource("classpath:jwt-config.properties")
class JwtConfigureTest {

	@Autowired
	private JwtConfigure jwtConfigure;

	@Test
	@DisplayName("Jwt 설정 바인딩 테스트")
	void testJwtConfigureSuccess() {
		//given when then
		assertAll(
			() -> assertThat(jwtConfigure.header()).isEqualTo("testToken"),
			() -> assertThat(jwtConfigure.issuer()).isEqualTo("testIssuer"),
			() -> assertThat(jwtConfigure.clientSecret())
				.isEqualTo("lsuvomkeoiotkbzdjrejfgdsfngusviykzsvjoriyukscpmxiijmpfsdwveljsml"),
			() -> assertThat(jwtConfigure.expirySeconds()).isEqualTo(100)
		);
	}
}
