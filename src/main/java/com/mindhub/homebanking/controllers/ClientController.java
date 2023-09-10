package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.ClientDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.mindhub.homebanking.services.ClientService;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api")
public class ClientController {


    @Autowired
    private ClientService clientService;

    @GetMapping("/clients")
    public List<ClientDTO> getClients() {
        return clientService.getClientsDTO();
    }
    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return clientService.getClientDtoById(id);
    }
    @GetMapping("/clients/current")
    public ClientDTO getAuthenticatedClient(Authentication authentication) {
        return clientService.getCurrentClientDTO(authentication);
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


        if (clientService.getClientByEmail(email) !=  null) {

            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);

        }
        clientService.registerClient(firstName, lastName, email, password);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }



}
