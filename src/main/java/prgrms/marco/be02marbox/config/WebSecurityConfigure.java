package prgrms.marco.be02marbox.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import prgrms.marco.be02marbox.domain.user.jwt.CustomAccessDeniedHandler;
import prgrms.marco.be02marbox.domain.user.jwt.CustomAuthenticationEntryPoint;
import prgrms.marco.be02marbox.domain.user.jwt.Jwt;
import prgrms.marco.be02marbox.domain.user.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {

	private final JwtConfigure jwtConfigure;

	public WebSecurityConfigure(JwtConfigure jwtConfigure) {
		this.jwtConfigure = jwtConfigure;
	}

	@Bean
	public Jwt jwt() {
		return new Jwt(
			jwtConfigure.issuer(),
			jwtConfigure.clientSecret(),
			jwtConfigure.expirySeconds());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		Jwt jwt = getApplicationContext().getBean(Jwt.class);
		return new JwtAuthenticationFilter(jwtConfigure.header(), jwt);
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return new CustomAuthenticationEntryPoint();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			.antMatchers(HttpMethod.POST, "/users/sign-up", "/users/sign-in", "/users/refresh").permitAll()
			.antMatchers(HttpMethod.GET, "/theaters", "/schedules/**", "/swagger-ui/**",
				"/swagger-resources/**", "/docs/**").permitAll()
			.anyRequest().hasAnyRole("ADMIN")
			.and()

			.formLogin()
			.disable()

			.csrf()
			.disable()

			.headers()
			.disable()

			.httpBasic()
			.disable()

			.rememberMe()
			.disable()

			.logout()
			.disable()

			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()

			.addFilterAfter(jwtAuthenticationFilter(), SecurityContextPersistenceFilter.class)

			.exceptionHandling()
			.authenticationEntryPoint(authenticationEntryPoint())
			.accessDeniedHandler(accessDeniedHandler());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui",
			"/swagger-resources", "/configuration/security",
			"/swagger-ui.html", "/webjars/**", "/swagger/**");
	}
}
