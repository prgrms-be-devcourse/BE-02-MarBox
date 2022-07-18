package prgrms.marco.be02marbox.domain.reservation.service;

import static prgrms.marco.be02marbox.domain.exception.custom.Message.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prgrms.marco.be02marbox.domain.reservation.Account;
import prgrms.marco.be02marbox.domain.reservation.repository.AccountRepository;
import prgrms.marco.be02marbox.domain.user.User;

@Service
@Transactional(readOnly = true)
public class AccountService implements PayService {

	private final AccountRepository accountRepository;

	public AccountService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	/**
	 * 결제하기
	 * @param user
	 * @param paymentAmount
	 * @return paymentAmount
	 * @throws IllegalArgumentException
	 */
	@Override
	@Transactional
	public Integer pay(User user, Integer paymentAmount) {
		Account account = accountRepository.findByUser(user)
			.orElseThrow(() -> new IllegalArgumentException(NO_ACCOUNT_EXP_MSG.getMessage()));

		account.withdraw(paymentAmount);

		return paymentAmount;
	}
}
