package com.fdmgroup.accountcustomermngtspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fdmgroup.accountcustomermngtspring.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
