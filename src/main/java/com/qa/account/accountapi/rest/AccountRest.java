package com.qa.account.accountapi.rest;

import com.qa.account.accountapi.persistence.domain.Account;
import com.qa.account.accountapi.service.AccountService;

import com.qa.account.accountapi.util.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RequestMapping(Constants.URL_BASE)
@RestController
public class AccountRest {

    @Autowired
    private AccountService service;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${url.generator}")
    private String generatorURL;
    
    @Value("${path.genAccountNum}")
    private String accountNumGeneratorPath;
    
    @Value("${url.prize}")
    private String prizeURL;
    
    @Value("${path.determinePrize}")
    private String determinePrizePath;

    @GetMapping(Constants.URL_GET_ACCOUNTS)
    public List<Account> getAccounts() {
        return service.getAccounts();
    }

    @GetMapping(Constants.URL_GET_ACCOUNT_BY_ID)
    public Account getAccount(@PathVariable Long id) {
        return service.getAccount(id);
    }

    @DeleteMapping(Constants.URL_DELETE_ACCOUNT)
    public ResponseEntity<Object> deleteAccount(@PathVariable Long id) {
        return service.deleteAccount(id);
    }

    @PutMapping(Constants.URL_UPDATE_ACCOUNT)
    public ResponseEntity<Object> updateAccount(@RequestBody Account account, @PathVariable Long id) {
        return service.updateAccount(account, id);
    }
    
    @PostMapping(Constants.URL_CREATE_ACCOUNT)
    public List<Object> createAccount(@RequestBody Account account) {
    	String generatedNum = restTemplate.getForObject(generatorURL + accountNumGeneratorPath, String.class);
    	Integer prizeWon = restTemplate.getForObject(prizeURL + determinePrizePath + generatedNum, Integer.class);
 	
    	account.setAccountNumber(generatedNum);
    	
    	return prizeAndAccount(service.addAccount(account), prizeWon);
    }

    private List<Object> prizeAndAccount(Account account, Integer prizeWon){
        List<Object> prizeAndAccount = new ArrayList<>();
        prizeAndAccount.add(account);
        prizeAndAccount.add(prizeWon);
        return prizeAndAccount;
    }

}
