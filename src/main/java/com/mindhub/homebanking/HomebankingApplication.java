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

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {

		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
		return (args) -> {
			Client melba = new Client("Melba", "Morel", "melba@mindhub.com");
			Account account1 = new Account("VIN001", LocalDate.now(), 5000);
			Account account2 = new Account("VIN002", LocalDate.now().plusDays(1), 7000);

			Account account3 = new Account("VIN003", LocalDate.now(), 8000);

			account1.setClient(melba);
			account2.setClient(melba);


			melba.addAccount(account1);
			melba.addAccount(account2);




			Client juan = new Client("Juan", "Lozano", "juanlozano@gmail.com");
			account3.setClient(juan);



			Transaction tmelba1=new Transaction(TransactionType.DEBIT,770,"Merpago*mcdonalds", LocalDateTime.now());
			Transaction tmelba2=new Transaction(TransactionType.DEBIT,1000,"Merpago*shel", LocalDateTime.now());
			Transaction tmelba3=new Transaction(TransactionType.DEBIT,1000,"CIC -Lomas", LocalDateTime.now());
			Transaction tmelba4=new Transaction(TransactionType.CREDIT,3000,"Transf. Recibida", LocalDateTime.now());

			Transaction tjuan1=new Transaction(TransactionType.CREDIT,30000,"Dep Sueldo", LocalDateTime.now());

			account1.addTransaction(tmelba1);
			account1.addTransaction(tmelba2);
			account2.addTransaction(tmelba3);
			account2.addTransaction(tmelba4);

			account3.addTransaction(tjuan1);

			clientRepository.save(melba);

			clientRepository.save(juan);

			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);




			transactionRepository.save(tmelba1);
			transactionRepository.save(tmelba2);
			transactionRepository.save(tmelba3);
			transactionRepository.save(tmelba4);
			transactionRepository.save(tjuan1);











		};
	}
}
