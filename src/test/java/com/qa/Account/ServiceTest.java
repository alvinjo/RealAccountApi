package com.qa.Account;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.qa.account.accountapi.persistence.repository.AccountRepository;
import com.qa.account.accountapi.service.AccountServiceImpl;
import com.qa.account.accountapi.util.exceptions.AccountNotFoundException;
import com.qa.account.accountapi.persistence.domain.Account;

@RunWith(MockitoJUnitRunner.class)
public class ServiceTest {
	
	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@InjectMocks
	AccountServiceImpl service;
	
	@Mock
	AccountRepository repo;
	
	private static final Account MOCK_ACCOUNT_1 = new Account(1L, "Ben", "Taylor", "C:634893");
	private static final Account MOCK_ACCOUNT_2 = new Account(2L, "Alvin", "Joseph", "B:749452");
	private static final Optional<Account> MOCK_ACCOUNT_OPTIONAL = Optional.of(MOCK_ACCOUNT_1);
	private static final Optional<Account> MOCK_NULL_OPTIONAL = Optional.empty();
	private static final ResponseEntity<Object> MOCK_OK_RESPONSE = new ResponseEntity<Object>(HttpStatus.OK);
	private static final ResponseEntity<Object> MOCK_NOT_FOUND_RESPONSE = new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
	
	@Test
	public void getAccountsTest() {
		List<Account> MOCK_LIST = new ArrayList<>();;
		MOCK_LIST.add(MOCK_ACCOUNT_1);
		MOCK_LIST.add(MOCK_ACCOUNT_2);
		Mockito.when(repo.findAll()).thenReturn(MOCK_LIST);
		assertEquals(MOCK_LIST, service.getAccounts());
		Mockito.verify(repo).findAll();
	}
	
	@Test
	public void getAccountByIdTest() {
		Mockito.when(repo.findById(1L)).thenReturn(MOCK_ACCOUNT_OPTIONAL);
		Mockito.when(repo.findById(3L)).thenReturn(MOCK_NULL_OPTIONAL);
		assertEquals(service.getAccount(1L), MOCK_ACCOUNT_1);
		
		exception.expect(AccountNotFoundException.class);
		service.getAccount(3L);
		
		Mockito.verify(repo).findById(1L);
	}
	
	@Test
	public void addAccountTest() {
		Mockito.when(repo.save(MOCK_ACCOUNT_1)).thenReturn(MOCK_ACCOUNT_1);
		assertEquals(MOCK_ACCOUNT_1, service.addAccount(MOCK_ACCOUNT_1));
		Mockito.verify(repo).save(MOCK_ACCOUNT_1);
	}
	
	@Test
	public void deleteAccountTest() {
		Mockito.when(repo.findById(1L)).thenReturn(MOCK_ACCOUNT_OPTIONAL);
		Mockito.when(repo.findById(3L)).thenReturn(MOCK_NULL_OPTIONAL);
		assertEquals(MOCK_OK_RESPONSE, service.deleteAccount(1L));
		assertEquals(MOCK_NOT_FOUND_RESPONSE, service.deleteAccount(3L));
		Mockito.verify(repo).findById(1L);
	}
	
	@Test
	public void updateAccountTest() {
		Mockito.when(repo.findById(1L)).thenReturn(MOCK_ACCOUNT_OPTIONAL);
		Mockito.when(repo.findById(3L)).thenReturn(MOCK_NULL_OPTIONAL);
		assertEquals(MOCK_OK_RESPONSE, service.updateAccount(MOCK_ACCOUNT_1, 1L));
		assertEquals(MOCK_NOT_FOUND_RESPONSE, service.updateAccount(MOCK_ACCOUNT_1, 3L));
		Mockito.verify(repo).findById(1L);
	}
}
