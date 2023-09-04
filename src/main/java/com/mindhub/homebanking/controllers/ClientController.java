package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientRepository repo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountController accountController;

    @Autowired
    private AccountRepository repoAccount;

    @RequestMapping("/clients")
    public List<ClientDTO> getClients() {
        return repo.findAll().stream().map(ClientDTO::new).collect(toList());

    }
    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return repo.findById(id).map(ClientDTO::new).orElse(null);
    }

    @GetMapping("/clients/current")
    public ClientDTO getAuthenticatedClient(Authentication authentication) {
        return new ClientDTO(repo.findByEmail(authentication.getName()));
    }

    @PostMapping("/clients")

    public ResponseEntity<Object> register(

            @RequestParam String firstName, @RequestParam String lastName,

            @RequestParam String email, @RequestParam String password) {



        List<String> missingFields = new ArrayList<>();

        if (firstName == null || firstName.isBlank()) {
            missingFields.add("firstName");
        }
        if (lastName == null || lastName.isBlank()) {
            missingFields.add("lastName");
        }
        if (email == null || email.isBlank()) {
            missingFields.add("email");
        }
        if (password == null || password.isBlank()) {
            missingFields.add("password");
        }

        if (!missingFields.isEmpty()) {
            String errorMessage = "Missing or empty fields: " + String.join(", ", missingFields);
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }


        if (repo.findByEmail(email) !=  null) {

            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);

        }
        String accountNumber=accountController.generateAccountNumber();
        Account newAccount = new Account(accountNumber, LocalDate.now(), 0);

        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        newAccount.setClient(client);
        client.addAccount(newAccount);
        repo.save(client);
        repoAccount.save(newAccount);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }






}
