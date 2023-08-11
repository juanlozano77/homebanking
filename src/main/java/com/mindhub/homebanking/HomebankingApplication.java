package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {

		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
		return (args) -> {

			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com");
			Account account1 = new Account("VIN001", LocalDate.now(), 5000);
			Account account2 = new Account("VIN002", LocalDate.now().plusDays(1), 7000);
			account1.setClient(client1);
			account2.setClient(client1);
			client1.addAccount(account1);
			client1.addAccount(account2);
			Account account3 = new Account("VIN003", LocalDate.now(), 8000);
			Client client2 = new Client("Juan", "Lozano", "juanlozano@gmail.com");
			account3.setClient(client2);
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
			clientRepository.saveAll(List.of(client1,client2));
			accountRepository.saveAll(List.of(account1,account2,account3));
			transactionRepository.saveAll(List.of(transaction1,transaction2,transaction3,transaction4,transaction5));
		};
	}
}
