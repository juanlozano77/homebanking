package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Set;

public interface AccountService {
    List<AccountDTO> getAccountsDTO();
    AccountDTO getAccountById(Long id);
    void create(Authentication authentication, Client currentClient);
    Set<AccountDTO> getCurrentClientAccountsDTO(Authentication authentication);
    Account getByNumber(String accountNumber);
    void save(Account account);

}