package com.mindhub.homebanking.services.implement;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountService accountService;

    @Override
    public void saveTransaction(Transaction transaction){
        transactionRepository.save(transaction);
    }
    @Override
    public void createTransaction(Account accountFrom, Account accountTo, Double amount, String description) {
        Transaction transactionDebit = new Transaction(TransactionType.DEBIT, -amount, description + " " + accountTo.getNumber(), LocalDateTime.now());

        Transaction transactionCredit = new Transaction(TransactionType.CREDIT, amount, description + " " + accountFrom.getNumber(), LocalDateTime.now());

        accountFrom.addTransaction(transactionDebit);
        accountTo.addTransaction(transactionCredit);

        accountService.save(accountFrom);
        accountService.save(accountTo);
        this.saveTransaction(transactionDebit);
        this.saveTransaction(transactionCredit);
    }

}
