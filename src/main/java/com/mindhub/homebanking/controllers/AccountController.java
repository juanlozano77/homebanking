package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return  accountService.getAccountsDTO();
    }
    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountService.getAccountById(id);
    }
    @GetMapping("/clients/current/accounts")
    public Set<AccountDTO> getAuthenticatedClient(Authentication authentication) {
        return accountService.getCurrentClientAccountsDTO(authentication);
    }
    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication) {
        Client currentClient = clientService.getClientByEmail(authentication.getName());

        if (currentClient == null) {
            return new ResponseEntity<>("Clients doesnt exist", HttpStatus.FORBIDDEN);
        }

        if (currentClient.getAccounts().size() >= 3) {
            return new ResponseEntity<>("Already 3 accounts", HttpStatus.FORBIDDEN);
        }

        accountService.create(authentication,currentClient);

        return new ResponseEntity<>("Account created!", HttpStatus.CREATED);


    }

}
