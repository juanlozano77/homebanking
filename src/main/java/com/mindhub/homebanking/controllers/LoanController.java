package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.LoanAplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;


import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private LoanRepository repo;
    @Autowired
    private ClientRepository repoClient;
    @Autowired
    private AccountRepository repoAccount;

    @Autowired
    private ClientLoanRepository repoClientLoan;
    @Autowired
    private TransactionRepository repoTransaction;
    @RequestMapping("/loans")
    public List<LoanDTO> getLoans() {
        return repo.findAll().stream().map(LoanDTO::new).collect(toList());
    }
    @PostMapping("/loans")
    @Transactional
    public ResponseEntity<Object> createLoan(Authentication authentication, @RequestBody LoanAplicationDTO loanAplicationDTO) {
        Client currentClient = repoClient.findByEmail(authentication.getName());
        System.out.println(loanAplicationDTO.getAmount());
        if (loanAplicationDTO.getToAccountNumber()==null)  {
            return new ResponseEntity<>("Account does not exit", HttpStatus.FORBIDDEN);
        }
        if (loanAplicationDTO.getAmount()<= 0){
            return new ResponseEntity<>("Amount must be greater than 0", HttpStatus.FORBIDDEN);

        }

        Loan currentLoan=repo.findById(loanAplicationDTO.getLoanId()).orElse(null);
        if (currentLoan==null){
            return new ResponseEntity<>("Loan does not exist", HttpStatus.FORBIDDEN);
        }
        if( !currentLoan.getPayments().contains(loanAplicationDTO.getPayments())) {
            return new ResponseEntity<>("That number of payments does not exist.", HttpStatus.FORBIDDEN);
        }

        if (loanAplicationDTO.getAmount()>currentLoan.getMaxAmount()){
            return new ResponseEntity<>("Amount exceeding the maximum", HttpStatus.FORBIDDEN);
        }
        Account accountDest=repoAccount.findByNumber(loanAplicationDTO.getToAccountNumber());
        if (accountDest==null){
            return new ResponseEntity<>("Destination Account does not exist", HttpStatus.FORBIDDEN);
        }
        if (!accountDest.getClient().equals(currentClient)) {

            return new ResponseEntity<>("The account does not belong to the client", HttpStatus.FORBIDDEN);
        }
        ClientLoan clientLoan=new ClientLoan(currentClient,currentLoan,loanAplicationDTO.getAmount()*1.2,loanAplicationDTO.getPayments());
        Transaction transaction=new Transaction(TransactionType.CREDIT,loanAplicationDTO.getAmount(),currentLoan.getName()+" Loan Aprobed", LocalDateTime.now());
        accountDest.addTransaction(transaction);
        currentClient.addClientLoan(clientLoan);
        repoTransaction.save(transaction);
        repoAccount.save(accountDest);
        repoClient.save(currentClient);
        repoClientLoan.save(clientLoan);

        return new ResponseEntity<>("Loan created!", HttpStatus.CREATED);

    }
}
