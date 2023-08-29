package com.mindhub.homebanking.models;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


@Entity
public class ClientLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="loan_id")
    private Loan loan;

    private double amount;
    private int payments;

    public ClientLoan(){}
    public ClientLoan(Client client, Loan loan, double amount, int payments) {
        this.client = client;
        this.loan = loan;
        this.amount = amount;
        this.payments = payments;
    }

    public Long getId() {
        return id;
    }


    public Client getClient() {
        return client;
    }


    public Loan getLoan() {
        return loan;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public void setClient(Client client) {
        this.client = client;
    }
    public void setPayments(int payments) {
        this.payments = payments;
    }


}
