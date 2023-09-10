package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.*;
import org.springframework.security.core.Authentication;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

public interface CardService {
   Set<CardDTO> getAuthenticatedClient(Authentication authentication);
   Account getAccountByNumber(String email);
   void registerCard(Client currentClient, CardType cardType, CardColor cardColor);

   void saveCard(Card card);
}
