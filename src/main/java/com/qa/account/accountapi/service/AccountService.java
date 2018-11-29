package com.qa.account.accountapi.service;

import org.springframework.http.ResponseEntity;

import com.qa.account.persistence.domain.Account;

import java.util.List;

public interface AccountService {

    List<Account> getAccounts();

    Account getAccount(Long id);

    Account addAccount(Account account);

    ResponseEntity<Object> deleteAccount(Long id);

    ResponseEntity<Object> updateAccount(Account account, Long id);
}
