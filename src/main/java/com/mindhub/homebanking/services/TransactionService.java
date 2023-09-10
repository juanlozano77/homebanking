package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;

public interface TransactionService {
    void saveTransaction(Transaction transaction);
    void createTransaction(Account accountFrom, Account accountTo, Double amount, String description);

}
