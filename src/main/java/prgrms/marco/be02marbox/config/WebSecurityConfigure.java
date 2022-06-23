package prgrms.marco.be02marbox.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			.anyRequest().permitAll()
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
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
}
