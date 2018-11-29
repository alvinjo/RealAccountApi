package com.qa.account.persistence.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Account {
    @Id
    @GeneratedValue
    private Long accountId;

    private String firstName;

    private String lastName;

    private String accountNumber;
    
    @OneToOne(cascade=CascadeType.ALL)
    private Prize prize;

    public Account() {
    }

    public Account(String firstName, String lastName, String accountNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.accountNumber = accountNumber;
    }

    public Long getId() {
        return accountId;
    }

    public void setId(Long id) {
        this.accountId = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    public Prize getPrize() {
    	return prize;
    }
    
    public void setPrize(Prize prize) {
    	this.prize = prize;
    }
}
