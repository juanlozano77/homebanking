package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.utils.Utils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest

@AutoConfigureTestDatabase(replace = NONE)

public class RepositoriesTest {



    @Autowired

    LoanRepository loanRepository;

    @Autowired

    CardRepository cardRepository;

    @Autowired

    AccountRepository accountRepository;

    @Autowired

    ClientRepository clientRepository;

    @Autowired

    TransactionRepository transactionRepository;



    @Test

    public void existLoans(){

        List<Loan> loans = loanRepository.findAll();

        assertThat(loans,is(not(empty())));

    }

    @Test

    public void existPersonalLoan(){

        List<Loan> loans = loanRepository.findAll();

        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));

    }

    //CardRepository
    @Test
    public void existCards(){

        List<Card> cards = cardRepository.findAll();

        assertThat(cards,is(not(empty())));

    }

    @Test

    public void existCardSilver(){

        List<Card> cards = cardRepository.findAll();

        boolean silverCardExists = cards.stream()
                .anyMatch(card -> CardColor.SILVER.equals(card.getColor()));

        assertThat(silverCardExists, is(true));

    }
    //AccountRepository
    @Test

    public void existAccount(){

        List<Account> accounts = accountRepository.findAll();

        assertThat(accounts,is(not(empty())));

    }

    @Test

    public void existAccountVIN001(){

        List<Account> accounts = accountRepository.findAll();

        assertThat(accounts, hasItem(hasProperty("number", is("VIN001"))));

    }
    //ClientRepository
    @Test

    public void existClients(){

        List<Client> clients = clientRepository.findAll();

        assertThat(clients,is(not(empty())));

    }

    @Test

    public void existClientMelba(){

        List<Client> clients = clientRepository.findAll();

        assertThat(clients, hasItem(hasProperty("firstName", is("Melba"))));

    }
    //TransactionRepository
    @Test
    public void existTransactions(){

        List<Transaction> transactions = transactionRepository.findAll();

        assertThat(transactions,is(not(empty())));

    }

    @Test

    public void existDebitTransactions(){

        List<Transaction> transactions = transactionRepository.findAll();

        boolean transactionDebitExists = transactions.stream()
                .anyMatch(transaction -> TransactionType.DEBIT.equals(transaction.getType()));

        assertThat(transactionDebitExists, is(true));

    }
    @Test

    public void cardNumberIsCreated(){

        String cardNumber = Utils.generateCardNumber();

        assertThat(cardNumber,is(not(emptyOrNullString())));

    }
    public void AccountNumberIsCreated(){

        String accountNumber = Utils.generateAccountNumber();

        assertThat(accountNumber,is(not(emptyOrNullString())));

    }
    public void CvvIsCreated(){

        int cvvNumber = Utils.generateCvv();

        assertThat(cvvNumber, is(notNullValue()));
    }



}


