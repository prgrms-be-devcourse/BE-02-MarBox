package prgrms.marco.be02marbox.domain.user.jwt;

import static org.apache.commons.lang3.ClassUtils.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import prgrms.marco.be02marbox.domain.user.dto.ResponseLoginUser;
import prgrms.marco.be02marbox.domain.user.service.UserService;

public class JwtAuthenticationProvider implements AuthenticationProvider {

	private final Jwt jwt;
	private final UserService userService;

	public JwtAuthenticationProvider(Jwt jwt, UserService userService) {
		this.jwt = jwt;
		this.userService = userService;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return isAssignable(JwtAuthenticationToken.class, authentication);
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		JwtAuthenticationToken jwtAuthentication = (JwtAuthenticationToken)authentication;
		return processUserAuthentication(
			String.valueOf(jwtAuthentication.getPrincipal()),
			jwtAuthentication.getCredentials()
		);
	}

	private Authentication processUserAuthentication(String principal, String credentials) {
		try {
			ResponseLoginUser responseLoginUser = userService.login(principal, credentials);

			String token = getToken(responseLoginUser.name(), responseLoginUser.role());

			List<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority(responseLoginUser.role()));
			return new JwtAuthenticationToken(
				new JwtAuthentication(token, responseLoginUser.name()), null, authorities);
		} catch (IllegalArgumentException e) {
			throw new BadCredentialsException(e.getMessage());
		} catch (DataAccessException e) {
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
	}

	private String getToken(String username, String role) {
		return jwt.sign(Jwt.Claims.from(username, role));
	}
}
