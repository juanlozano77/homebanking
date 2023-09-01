package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountRepository repo;
    @Autowired
    private ClientRepository repoClient;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return repo.findAll().stream().map(AccountDTO::new).collect(toList());

    }
    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return repo.findById(id).map(AccountDTO::new).orElse(null);
    }
    @GetMapping("/clients/current/accounts")
    public Set<AccountDTO> getAuthenticatedClient(Authentication authentication) {
        Set<Account> clientAccounts = repoClient.findByEmail(authentication.getName()).getAccounts();
        return clientAccounts.stream().map(AccountDTO::new).collect(toSet());
    }
    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication) {
        Client currentClient = repoClient.findByEmail(authentication.getName());


        if (currentClient == null) {
            return new ResponseEntity<>("Clients doesnt exist", HttpStatus.FORBIDDEN);
        }


        if (currentClient.getAccounts().size() >= 3) {
            return new ResponseEntity<>("Already 3 accounts", HttpStatus.FORBIDDEN);
        }

        String accountNumber = generateAccountNumber();
        Account newAccount = new Account(accountNumber, LocalDate.now(), 0);
        newAccount.setClient(currentClient);
        currentClient.addAccount(newAccount);

        repo.save(newAccount);

        return new ResponseEntity<>("Account created!", HttpStatus.CREATED);


    }
    private String generateAccountNumber() {
        String accountNumber;
        do {
            int randomNumber = (int) (Math.random() * 99999999) + 1;
            accountNumber = "VIN" + randomNumber;
        } while (repo.findByNumber(accountNumber)!= null);
        return accountNumber;
    }
}
