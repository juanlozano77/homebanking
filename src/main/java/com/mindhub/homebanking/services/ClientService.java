package com.mindhub.homebanking.services;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ClientService {
    ClientDTO getCurrentClientDTO(Authentication authentication);
    List<ClientDTO> getClientsDTO();
    ClientDTO getClientDtoById(Long id);
    Client getClientByEmail(String email);
    void saveClient(Client client);
    void registerClient(String firstName, String lastName, String email, String password);
}

