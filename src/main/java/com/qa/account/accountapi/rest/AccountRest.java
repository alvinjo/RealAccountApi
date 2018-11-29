package com.qa.account.accountapi.rest;

import com.qa.account.accountapi.service.AccountService;

import com.qa.account.accountapi.util.constants.Constants;
import com.qa.account.persistence.domain.Account;
import com.qa.account.persistence.domain.Prize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@CrossOrigin
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
    public Account createAccount(@RequestBody Account account) {
        account = setAccountNumberAndPrize(account);
    	return service.addAccount(account);
    }

    private Account setAccountNumberAndPrize(Account account){
        String generatedAccountNum = restTemplate.getForObject(generatorURL + accountNumGeneratorPath, String.class);
        Prize prizeWon = restTemplate.getForObject(prizeURL + determinePrizePath + generatedAccountNum, Prize.class);

        account.setAccountNumber(generatedAccountNum);
        account.setPrize(prizeWon);

        return account;
    }

}
