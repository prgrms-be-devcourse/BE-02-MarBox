package prgrms.marco.be02marbox.domain.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "users")
public class User {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "role", nullable = false)
	@Enumerated(value = EnumType.STRING)
	private Role role;

	@Column(name = "email", unique = true, nullable = false)
	private String email;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "name", nullable = false)
	private String name;

	protected User() {
	}

	public User(String email, String password, String name, Role role) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.role = role;
	}

	public void checkPassword(PasswordEncoder passwordEncoder, String password) {
		if (!passwordEncoder.matches(password, this.password)) {
			throw new BadCredentialsException("비밀번호가 틀렸습니다.");
		}
	}

	public Long getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getRoleName() {
		return this.role.name();
	}

	public String getEmail() {
		return this.email;
	}

	public String getPassword() {
		return this.password;
	}
}
