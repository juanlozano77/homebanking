package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private CardService cardService;
    @Autowired
    private ClientService clientService;
    @GetMapping("/clients/current/cards")
    public Set<CardDTO> getAuthenticatedClient(Authentication authentication) {
        return cardService.getAuthenticatedClient(authentication);
        }

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> addCard(Authentication authentication,
                                          @RequestParam CardType cardType, @RequestParam CardColor cardColor) {

        Client currentClient = clientService.getClientByEmail(authentication.getName());

        if (currentClient == null) {
            return new ResponseEntity<>("Clients doesnt exist", HttpStatus.FORBIDDEN);
        }

        if (cardType == null || cardColor == null) {
            return new ResponseEntity<>("Invalid card data", HttpStatus.BAD_REQUEST);
        }

        if (currentClient.getCards().stream().filter( card -> card.getType().equals(cardType)).count() >= 3) {
            return new ResponseEntity<>("Already 3 cards", HttpStatus.FORBIDDEN);
        }
        cardService.registerCard(currentClient,cardType,cardColor);

        return new ResponseEntity<>("Card created!", HttpStatus.CREATED);


    }



}
