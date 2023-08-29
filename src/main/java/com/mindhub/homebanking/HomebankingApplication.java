package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {
	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {

		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository,
									  LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository)    {
		return (args) -> {

			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com",passwordEncoder.encode("melba"));
			Account account1 = new Account("VIN001", LocalDate.now(), 5000);
			Account account2 = new Account("VIN002", LocalDate.now().plusDays(1), 7000);
			account1.setClient(client1);
			account2.setClient(client1);
			client1.addAccount(account1);
			client1.addAccount(account2);
			Account account3 = new Account("VIN003", LocalDate.now(), 8000);
			Client client2 = new Client("Juan", "Perez", "juanperez@mindhub.com",passwordEncoder.encode("1234"));
			Client admin=new Client("Juan", "Lozano", "admin@mindhub.com",passwordEncoder.encode("admin"));
			account3.setClient(client2);
			admin.setAdmin(true);
			Loan loan1 = new Loan("Hipotecario", 500000, Arrays.asList(12, 24, 36, 48, 60));
			Loan loan2 = new Loan("Personal", 100000, Arrays.asList(6, 12, 24));
			Loan loan3 = new Loan("Automotriz", 300000, Arrays.asList(6, 12, 24, 36));
			loanRepository.saveAll(List.of(loan1,loan2,loan3));


			ClientLoan clientLoan1 = new ClientLoan(client1, loan1, 400000, 60);
			ClientLoan clientLoan2 = new ClientLoan(client1, loan2, 50000, 12);
			ClientLoan clientLoan3 = new ClientLoan(client2, loan2, 100000, 24);
			ClientLoan clientLoan4 = new ClientLoan(client2, loan3, 200000, 36);
			client1.addClientLoan(clientLoan1);
			client1.addClientLoan(clientLoan2);
			client2.addClientLoan(clientLoan3);
			client2.addClientLoan(clientLoan4);

			Card card1 = new Card(client1.toString(), CardType.DEBIT, CardColor.GOLD,"4278-3600-0827-4719",100,LocalDate.now(),LocalDate.now().plusYears(5));
			Card card2 = new Card(client1.toString(),CardType.CREDIT,CardColor.TITANIUM,"3590-3600-0827-4654",681,LocalDate.now(),LocalDate.now().plusYears(5));

			Card card3 = new Card(client2.toString(),CardType.CREDIT,CardColor.SILVER,"4348-3600-4541-8960",511,LocalDate.now(),LocalDate.now().plusYears(5));

			client1.addCard(card1);
			client1.addCard(card2);
			client2.addCard(card3);









			Transaction transaction1=new Transaction(TransactionType.DEBIT,770,"Merpago*mcdonalds", LocalDateTime.now());
			Transaction transaction2=new Transaction(TransactionType.DEBIT,1000,"Merpago*shel", LocalDateTime.now());
			Transaction transaction3=new Transaction(TransactionType.DEBIT,1000,"CIC -Lomas", LocalDateTime.now());
			Transaction transaction4=new Transaction(TransactionType.CREDIT,3000,"Transf. Recibida", LocalDateTime.now());
			Transaction transaction5=new Transaction(TransactionType.CREDIT,30000,"Dep Sueldo", LocalDateTime.now());
			account1.addTransaction(transaction1);
			account1.addTransaction(transaction2);
			account2.addTransaction(transaction3);
			account2.addTransaction(transaction4);
			account3.addTransaction(transaction5);

			clientRepository.saveAll(List.of(client1,client2,admin));
			cardRepository.saveAll(List.of(card1,card2,card3));
			clientLoanRepository.saveAll(List.of(clientLoan1,clientLoan2,clientLoan3,clientLoan4));

			accountRepository.saveAll(List.of(account1,account2,account3));
			transactionRepository.saveAll(List.of(transaction1,transaction2,transaction3,transaction4,transaction5));
		};
	}
}
