package prgrms.marco.be02marbox.domain.reservation.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import prgrms.marco.be02marbox.domain.reservation.Account;
import prgrms.marco.be02marbox.domain.reservation.repository.AccountRepository;
import prgrms.marco.be02marbox.domain.user.Role;
import prgrms.marco.be02marbox.domain.user.User;
import prgrms.marco.be02marbox.domain.user.repository.UserRepository;

@DataJpaTest
@Import(AccountService.class)
class AccountServiceTest {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AccountService accountService;

	@ParameterizedTest(name = "{index} totalMoney : {0}, paymentAmount : {1}")
	@CsvSource({"1000, 1000", "1000, 999"})
	void testPaySuccess(int totalMoney, int paymentAmount) {
		//given
		User user = new User(
			"hyuk@mail.com",
			"encrypted",
			"hyuk",
			Role.ROLE_CUSTOMER);

		User savedUser = userRepository.save(user);

		Account savedAccount = accountRepository.save(new Account(savedUser, totalMoney));

		//when
		Integer retrievedPaymentAmount = accountService.pay(savedUser, paymentAmount);

		//then
		assertAll(
			() -> assertThat(retrievedPaymentAmount).isEqualTo(paymentAmount),
			() -> assertThat(savedAccount.getMoney()).isEqualTo(totalMoney - paymentAmount));
	}

	@Test
	@DisplayName("pay 실패 - 잔액 부족")
	void testPayFail() {
		//given
		User user = new User(
			"hyuk@mail.com",
			"encrypted",
			"hyuk",
			Role.ROLE_CUSTOMER);

		User savedUser = userRepository.save(user);

		int initMoney = 100_000;
		Account savedAccount = accountRepository.save(new Account(savedUser, initMoney));

		int paymentAmount = savedAccount.getMoney() + 1;

		//when then
		assertThatThrownBy(() -> accountService.pay(savedUser, paymentAmount))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("잔액이 부족합니다.");
	}
}
