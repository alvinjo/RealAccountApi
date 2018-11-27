package com.qa.account.accountapi.persistence.repository;

import com.qa.account.accountapi.persistence.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
}
