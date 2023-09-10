package com.mindhub.homebanking.services.implement;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import static com.mindhub.homebanking.utils.Utils.generateAccountNumber;
import static java.util.stream.Collectors.toList;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ClientDTO getCurrentClientDTO(Authentication authentication){
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }
    @Override
    public List<ClientDTO> getClientsDTO(){
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());
    }
    @Override
    public ClientDTO getClientDtoById(Long id){
        return clientRepository.findById(id).map(ClientDTO::new).orElse(null);
    }
    @Override
    public Client getClientByEmail(String email){
        return clientRepository.findByEmail(email);
    }
    @Override
    public void saveClient(Client client){
        clientRepository.save(client);
    }

    @Override
    public void registerClient(String firstName, String lastName, String email, String password){
        Account newAccount = new Account(generateAccountNumber(accountRepository), LocalDate.now(),0);
        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        client.addAccount(newAccount);
        this.saveClient(client);
        accountRepository.save(newAccount);
    }
}
