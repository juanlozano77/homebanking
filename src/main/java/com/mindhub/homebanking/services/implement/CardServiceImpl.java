package com.mindhub.homebanking.services.implement;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Set;

import static com.mindhub.homebanking.utils.Utils.generateCardNumber;
import static com.mindhub.homebanking.utils.Utils.generateNumber;
import static java.util.stream.Collectors.toSet;

@Service
public class CardServiceImpl implements CardService {
    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CardRepository cardRepository;

    @Override
    public Set<CardDTO> getAuthenticatedClient(Authentication authentication) {
        Set<Card> clientCards = clientService.getClientByEmail(authentication.getName()).getCards();
        return clientCards.stream().map(CardDTO::new).collect(toSet());
    }
    @Override
    public Account getAccountByNumber(String number){
        return accountService.getByNumber(number);
    }
    @Override

    public void saveCard(Card card){
        cardRepository.save(card);
    }

    @Override
    public void registerCard(Client currentClient, CardType cardType, CardColor cardColor) {
        String cardNumber;
        //Crea un numero de tarjeta al azar y verifica que no se haya creado
        do {
            cardNumber = generateCardNumber();
        } while (this.getAccountByNumber(cardNumber)!=null);
        int cvv=generateNumber(3);
        Card newCard = new Card(currentClient.toString(), cardType, cardColor, cardNumber, cvv, LocalDate.now(), LocalDate.now().plusYears(5));
        newCard.setClient(currentClient);
        this.saveCard(newCard);

    }
}