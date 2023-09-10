package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ClientService clientService;

    @Autowired

    private AccountService accountService;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object>addTransaction(Authentication authentication,
                                                @RequestParam String fromAccountNumber, @RequestParam String toAccountNumber,
                                                @RequestParam Double amount, @RequestParam String description) {


        Client currentClient =clientService.getClientByEmail(authentication.getName());

        if (amount<=0){
            return new ResponseEntity<>("Amount must be greater than 0", HttpStatus.FORBIDDEN);
        }
        List<String> missingFields = new ArrayList<>();

        if (fromAccountNumber == null || fromAccountNumber.isBlank()) {
            return new ResponseEntity<>("Missing from Account Number", HttpStatus.FORBIDDEN);
        }

        if (toAccountNumber == null || toAccountNumber.isBlank()) {
            return new ResponseEntity<>("Missing to Account Number", HttpStatus.FORBIDDEN);
        }

        if (description == null || description.isBlank()) {
            return new ResponseEntity<>("Missing description", HttpStatus.FORBIDDEN);
        }

        if (fromAccountNumber.equals(toAccountNumber)) {
            return new ResponseEntity<>("Same Accounts", HttpStatus.FORBIDDEN);
        }
        Account accountFrom = accountService.getByNumber(fromAccountNumber);

        Account accountTo=accountService.getByNumber(toAccountNumber);
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

        transactionService.createTransaction(accountFrom,accountTo,amount,description);

        return new ResponseEntity<>("Transaction created!", HttpStatus.CREATED);



    }
}