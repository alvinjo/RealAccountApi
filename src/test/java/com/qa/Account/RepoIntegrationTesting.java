package com.qa.Account;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.junit4.SpringRunner;

import com.qa.account.accountapi.rest.AccountRest;
import com.qa.account.accountapi.service.AccountService;
import com.qa.account.accountapi.util.exceptions.AccountNotFoundException;
import com.qa.account.persistence.domain.Account;
import com.qa.account.persistence.domain.Prize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RepoIntegrationTesting {
	
	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Autowired
	private AccountRest rest;
	
	@Autowired
	private AccountService service;
	
	private TestRestTemplate restTemp = new TestRestTemplate();
	
	private static final String MOCK_GEN_URL = "Gen URL";
	private static final String MOCK_PRIZE_URL = "prize URL";
	private static final Account MOCK_BLANK_ACCOUNT = new Account();
	private static final String MOCK_ACCOUNT_NUMBER = "B:646473";
	private static final Prize MOCK_PRIZE = new Prize(1L, 50, "test date");
	private static final Account MOCK_ACCOUNT = new Account(1L, "Ben", "Taylor", "A:746463");
	private static final Account MOCK_UPDATED_ACCOUNT = new Account(1L, "Alvin", "Joseph", "B:745634"); 
	
	@Test
	public void aAddAccountTest() {
		service.addAccount(MOCK_ACCOUNT);
		assertEquals(MOCK_ACCOUNT.toString(), service.getAccount(1L).toString());
	}
	
	@Test
	public void bGetAccountTest() {
		assertEquals(MOCK_ACCOUNT.toString(), rest.getAccount(1L).toString());
		
		exception.expect(AccountNotFoundException.class);
		rest.getAccount(2L);
	}
	
	@Test
	public void cGetAccountsTest() {
		List<Account> accounts = new ArrayList<>();
		accounts.add(MOCK_ACCOUNT);
		
		assertEquals(accounts.toString(), rest.getAccounts().toString());
	}
	
	@Test
	public void dUpdateAccountTest() {
		assertEquals(new ResponseEntity<Object>(HttpStatus.OK), rest.updateAccount(MOCK_UPDATED_ACCOUNT, 1L));
		assertEquals(new ResponseEntity<Object>(HttpStatus.NOT_FOUND), rest.updateAccount(MOCK_UPDATED_ACCOUNT, 2L));
		assertEquals(MOCK_UPDATED_ACCOUNT.toString(), service.getAccount(1L).toString());
	}
	
	@Test
	public void eDeleteAccountTest() {
		List<Account> emptyList = new ArrayList<>();
		assertEquals(new ResponseEntity<Object>(HttpStatus.OK), rest.deleteAccount(1L));
		assertEquals(new ResponseEntity<Object>(HttpStatus.NOT_FOUND), rest.deleteAccount(2L));
		assertEquals(emptyList, rest.getAccounts());
	}
	
//	@Test
//	public void fCreateAccountTest() {
//		MOCK_BLANK_ACCOUNT.setFirstName("Malcolm");
//		MOCK_BLANK_ACCOUNT.setLastName("Lindsay");
//		
//		Account testAccount = rest.createAccount(MOCK_BLANK_ACCOUNT);
//		Account expectedAccount = new Account(2L, "Malcolm", "Lindsay", MOCK_ACCOUNT_NUMBER);
//		
//		assertEquals(expectedAccount.toString(), testAccount.toString());
//		assertEquals(MOCK_PRIZE.toString(), testAccount.getPrize().toString());
//	}
}
