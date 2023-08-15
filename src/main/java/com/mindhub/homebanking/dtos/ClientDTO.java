package com.mindhub.homebanking.dtos;
import com.mindhub.homebanking.models.Client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class ClientDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<AccountDTO> accounts = new HashSet<>();

    private List<ClientLoanDTO> loans = new ArrayList<>();

    public ClientDTO() {
        //
    }
    public ClientDTO(Client client) {
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.accounts=client.getAccounts().stream().map(AccountDTO::new).collect(toSet());
        this.loans=client.getClientLoans().stream().map(ClientLoanDTO::new).collect(toList());


    }

    public long getId() {
        return id;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getEmail() {
        return email;
    }
    public Set<AccountDTO> getAccounts() {
                return accounts;
    }

    public List<ClientLoanDTO> getLoans() {return loans;}


}
