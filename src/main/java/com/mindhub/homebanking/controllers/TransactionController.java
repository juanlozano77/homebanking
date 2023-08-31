package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private TransactionRepository repo;
    @Autowired
    private ClientRepository repoClient;

    @Autowired

    private AccountRepository repoAccount;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object>addTransaction(Authentication authentication,
                                                @RequestParam String fromAccountNumber, @RequestParam String toAccountNumber,
                                                @RequestParam Double amount, @RequestParam String description) {


        Client currentClient = repoClient.findByEmail(authentication.getName());

        if (amount<=0){
            return new ResponseEntity<>("Amount must be greater than 0", HttpStatus.FORBIDDEN);
        }

        if (fromAccountNumber.isEmpty() || toAccountNumber.isEmpty() || description.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (fromAccountNumber.equals(toAccountNumber)) {
            return new ResponseEntity<>("Same Accounts", HttpStatus.FORBIDDEN);
        }
        Account accountFrom = repoAccount.findByNumber(fromAccountNumber);
        Account accountTo=repoAccount.findByNumber(toAccountNumber);
        if (accountFrom==null){
            return new ResponseEntity<>("The source account does not exist ", HttpStatus.FORBIDDEN);
        }
        if (accountTo==null){
            return new ResponseEntity<>("The  destination account does not exist ", HttpStatus.FORBIDDEN);
        }
        if (!accountFrom.getClient().equals(currentClient)){
            return new ResponseEntity<>("The source account is not owned by the client", HttpStatus.FORBIDDEN);
        }
        if (accountFrom.getBalance() < amount) {
            return new ResponseEntity<>("The amount is greater than the account balance", HttpStatus.FORBIDDEN);
        }

        Transaction transactionDebit=new Transaction(TransactionType.DEBIT,-amount,description, LocalDateTime.now());

        Transaction transactionCredit=new Transaction(TransactionType.CREDIT,amount,description, LocalDateTime.now());

        accountFrom.addTransaction(transactionDebit);
        accountTo.addTransaction(transactionCredit);

        repoAccount.save(accountFrom);
        repoAccount.save(accountTo);
        repo.save(transactionDebit);
        repo.save(transactionCredit);

        return new ResponseEntity<>("Transaction created!", HttpStatus.CREATED);



    }
}