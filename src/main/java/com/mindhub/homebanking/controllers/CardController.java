package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private CardRepository repo;
    @Autowired
    private ClientRepository repoClient;
    @GetMapping("/clients/current/cards")
    public Set<CardDTO> getAuthenticatedClient(Authentication authentication) {
           Set<Card> clientCards = repoClient.findByEmail(authentication.getName()).getCards();
            return clientCards.stream().map(CardDTO::new).collect(toSet());
        }

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> addCard(Authentication authentication,
                                          @RequestParam CardType cardType, @RequestParam CardColor cardColor) {

        Client currentClient = repoClient.findByEmail(authentication.getName());

        if (currentClient == null) {
            return new ResponseEntity<>("Clients doesnt exist", HttpStatus.FORBIDDEN);
        }

        if (cardType == null || cardColor == null) {
            return new ResponseEntity<>("Invalid card data", HttpStatus.BAD_REQUEST);
        }

        if (currentClient.getCards().stream().filter( card -> card.getType().equals(cardType)).count() >= 3) {
            return new ResponseEntity<>("Already 3 cards", HttpStatus.FORBIDDEN);
        }
        String cardNumber;
        //Crea un numero de tarjeta al azar y verifica que no se haya creado
        do {
            cardNumber = generateCardNumber();
        } while (repo.findByNumber(cardNumber)!= null);
        int cvv=generateNumber(3);
        Card newCard = new Card(currentClient.toString(), cardType, cardColor, cardNumber, cvv, LocalDate.now(), LocalDate.now().plusYears(5));
        newCard.setClient(currentClient);
        repo.save(newCard);

        return new ResponseEntity<>("Card created!", HttpStatus.CREATED);


    }
    private static int generateNumber(int digit) {
        int number;
        number = (int) (Math.random() * Math.pow(10, digit)) + 1;
        return number;
    }
    private static String generateCardNumber() {
        StringBuilder formattedNumber = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int number = generateNumber(4);
            if (number < 1000) {
                formattedNumber.append(String.format("%04d", number));
            } else {
                formattedNumber.append(number);
            }
            if (i<3) {
                formattedNumber.append("-");
            }
        }
        return formattedNumber.toString();
    }


}
