package prgrms.marco.be02marbox.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prgrms.marco.be02marbox.domain.user.dto.ResponseLoginToken;

@Service
@Transactional(readOnly = true)
public class JwtService {
	
	public ResponseLoginToken login(String email, String password) {
		return null;
	}
}
