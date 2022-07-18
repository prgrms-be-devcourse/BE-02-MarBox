package prgrms.marco.be02marbox.domain.reservation;

import static prgrms.marco.be02marbox.domain.exception.custom.Message.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import prgrms.marco.be02marbox.domain.user.User;

@Entity
@Table(name = "account")
public class Account {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "money")
	@Min(value = 0)
	private Integer money;

	protected Account() {
	}

	public Account(User user, Integer money) {
		this.user = user;
		this.money = money;
	}

	public void withdraw(Integer paymentAmount) {
		if (this.money < paymentAmount) {
			throw new IllegalArgumentException(NO_MONEY_EXP_MSG.getMessage());
		}
		this.money -= paymentAmount;
	}

	public Integer getMoney() {
		return money;
	}

	public Long getId() {
		return id;
	}
}
