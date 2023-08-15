package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
<<<<<<< Updated upstream

=======
import javax.persistence.OneToMany;
import javax.persistence.FetchType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
>>>>>>> Stashed changes
@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String firstName;
    private String lastName;
    private String email;
<<<<<<< Updated upstream
=======
    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy = "client",fetch=FetchType.EAGER)
    private Set<ClientLoan> loans = new HashSet<>();
>>>>>>> Stashed changes

    public Client() { }

    public Client (String first, String last,String email) {
        this.firstName = first;
        this.lastName = last;
        this.email=email;
    }

    public long getId() {
        return id;
    }
<<<<<<< Updated upstream
=======
    public String getFirstName() {return firstName;}
    public String getLastName() {return lastName;}
    public String getEmail() {return email;}
    public List<Loan> getLoans() {return loans.stream().map(sub -> sub.getLoan()).collect(toList());}
    public Set<ClientLoan> getClientLoans() {return loans;}

    public void setAccounts(Set<Account> accounts) {this.accounts = accounts;}
    public void setFirstName(String firstName) {this.firstName = firstName;}
    public void setLastName(String lastName) {this.lastName = lastName;}
>>>>>>> Stashed changes

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void setClientLoans(Set<ClientLoan> clientLoans) {
        this.loans = clientLoans;
    }

    public String toString() {
        return firstName + " " + lastName;
    }
}
