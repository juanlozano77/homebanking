package com.mindhub.homebanking.services.implement;
import com.mindhub.homebanking.dtos.LoanAplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.LoanService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class LoanServiceImpl implements LoanService {
    @Autowired
    private ClientService clientService;
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientLoanRepository clientLoanRepository;
    @Autowired
    private LoanRepository loanRepository;
    @Override
    public List<LoanDTO> getLoans() {
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(toList());
    }
    @Override
    public Loan getLoanById(Long id) {
        return loanRepository.findById(id).orElse(null);
    }

    @Override
    public void createLoan(Client currentClient, Loan currentLoan, Account accountDest, LoanAplicationDTO loanAplicationDTO) {
        ClientLoan clientLoan=new ClientLoan(currentClient,currentLoan,loanAplicationDTO.getAmount()*1.2,loanAplicationDTO.getPayments());
        Transaction transaction=new Transaction(TransactionType.CREDIT,loanAplicationDTO.getAmount(),currentLoan.getName()+" Loan Aprobed", LocalDateTime.now());
        accountDest.addTransaction(transaction);
        currentClient.addClientLoan(clientLoan);

        transactionService.saveTransaction(transaction);
        accountService.save(accountDest);
        clientService.saveClient(currentClient);
        clientLoanRepository.save(clientLoan);
    }
}

