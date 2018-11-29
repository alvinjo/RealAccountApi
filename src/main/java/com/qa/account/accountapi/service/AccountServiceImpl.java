package com.qa.account.accountapi.service;

import com.qa.account.accountapi.persistence.repository.AccountRepository;
import com.qa.account.accountapi.util.exceptions.AccountNotFoundException;
import com.qa.account.persistence.domain.Account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository repo;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Override
    public List<Account> getAccounts() {
        return repo.findAll();
    }

    @Override
    public Account getAccount(Long id) {
        Optional<Account> account = repo.findById(id);
        return account.orElseThrow(() -> new AccountNotFoundException(id.toString()));
    }

    @Override
    public Account addAccount(Account account) {
        jmsTemplate.convertAndSend("AccountQueue", account);
        return repo.save(account);
    }

    @Override
    public ResponseEntity<Object> deleteAccount(Long id) {
        if(accountExists(id)){
            repo.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Object> updateAccount(Account account, Long id) {
        if(!accountExists(id)){
            account.setId(id);
            repo.save(account);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }


    private boolean accountExists(Long id){
        Optional<Account> accountOptional = repo.findById(id);
        return accountOptional.isPresent();
    }

}
