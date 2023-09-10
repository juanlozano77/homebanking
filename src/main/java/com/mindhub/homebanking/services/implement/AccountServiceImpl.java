package com.mindhub.homebanking.services.implement;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import static com.mindhub.homebanking.utils.Utils.generateAccountNumber;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientService clientService;
    @Override
    public List<AccountDTO> getAccountsDTO() {
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());
    }

    @Override
    public AccountDTO getAccountById(Long id) {
        return accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }

    @Override
    public void create(Authentication authentication, Client currentClient) {
        Account newAccount = new Account(generateAccountNumber(accountRepository), LocalDate.now(),0);
        currentClient.addAccount(newAccount);
        clientService.saveClient(currentClient);
        this.save(newAccount);
    }

    @Override
    public Set<AccountDTO> getCurrentClientAccountsDTO(Authentication authentication) {
        Set<Account> clientAccounts = clientService.getClientByEmail(authentication.getName()).getAccounts();
        return clientAccounts.stream().map(AccountDTO::new).collect(Collectors.toSet());
    }

    @Override
    public Account getByNumber(String accountNumber) {
        Account account = accountRepository.findByNumber(accountNumber);
        return account;
    }
    @Override
    public void save(Account account) {
        accountRepository.save(account);
    }



}