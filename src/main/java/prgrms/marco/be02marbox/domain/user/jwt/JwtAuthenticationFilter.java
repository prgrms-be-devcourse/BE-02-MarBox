package prgrms.marco.be02marbox.domain.user.jwt;

import static org.apache.commons.lang3.StringUtils.*;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

public class JwtAuthenticationFilter extends GenericFilterBean {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final String headerKey;
	private final Jwt jwt;

	public JwtAuthenticationFilter(String headerKey, Jwt jwt) {
		this.headerKey = headerKey;
		this.jwt = jwt;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws
		IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;

		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			String token = getToken(request);
			if (isNotEmpty(token)) {
				try {
					Jwt.Claims claims = verify(token);
					log.debug("Jwt 파싱 결과: {}", claims);

					JwtAuthenticationToken authentication = generateJwtAuthenticationToken(claims, token);
					if (authentication != null) {
						authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(authentication);
					}
				} catch (Exception e) {
					log.warn("Jwt processing 실패: {}", e.getMessage());
				}
			}
		} else {
			log.debug("SecurityContextHolder에 이미 다른 security token이 들어있음: '{}'",
				SecurityContextHolder.getContext().getAuthentication());
		}

		chain.doFilter(request, response);
	}

	private String getToken(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (Objects.isNull(cookies)) {
			return null;
		}

		String token = Arrays.stream(cookies)
			.filter(cookie -> cookie.getName().equals(this.headerKey))
			.map(Cookie::getValue)
			.findFirst()
			.orElse(null);

		if (isNotEmpty(token)) {
			log.debug("요청 헤더에 jwt token이 존재합니다: {} ", token);
			return URLDecoder.decode(token, StandardCharsets.UTF_8);
		}
		return null;
	}

	private Jwt.Claims verify(String token) {
		return jwt.verify(token);
	}

	private List<GrantedAuthority> getAuthorities(Jwt.Claims claims) {
		String role = claims.role;
		List<GrantedAuthority> authorities = new ArrayList<>();
		if (isNotEmpty(role)) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		return authorities;
	}

	private JwtAuthenticationToken generateJwtAuthenticationToken(Jwt.Claims claims, String token) {
		String username = claims.username;
		List<GrantedAuthority> authorities = getAuthorities(claims);

		if (isNotEmpty(username) && !authorities.isEmpty()) {
			return new JwtAuthenticationToken(
				new JwtAuthentication(token, username),
				null,
				authorities);
		}
		return null;
	}
}
