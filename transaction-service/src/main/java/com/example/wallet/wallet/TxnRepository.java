package com.example.wallet.wallet;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TxnRepository extends JpaRepository<Transaction,Integer> {
}
