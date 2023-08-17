package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();
    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private Set<Card> cards = new HashSet<>();
    public Client() { }
    public Client (String first, String last,String email) {
        this.firstName = first;
        this.lastName = last;
        this.email=email;
    }

    public long getId() {return id;}
    public Set<Account> getAccounts() {
        return accounts;
    }
    public String getFirstName() {return firstName;}
    public String getLastName() {return lastName;}
    public String getEmail() {return email;}
    public Set<Card> getCards() {return cards;}
    public Set<ClientLoan> getClientLoans() {return clientLoans;}
    public void setAccounts(Set<Account> accounts) {this.accounts = accounts;}
    public void setFirstName(String firstName) {this.firstName = firstName;}
    public void setLastName(String lastName) {this.lastName = lastName;}

    public void setEmail(String email) {this.email = email;}
    public void addAccount(Account account) {
        account.setClient(this);
        this.accounts.add(account);
    }
    public void addClientLoan(ClientLoan clientLoan) {
        clientLoan.setClient(this);
        clientLoans.add(clientLoan);
    }

    public void addCard(Card card) {
        card.setClient(this);
        this.cards.add(card);
    }

    public void setCards(Set<Card> cards) {this.cards = cards;}

    public void setClientLoans(Set<ClientLoan> clientLoans) {
        this.clientLoans = clientLoans;
    }

    public String toString() {
        return firstName + " " + lastName;
    }
}
