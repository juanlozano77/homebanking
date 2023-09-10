package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.LoanAplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.mindhub.homebanking.services.LoanService;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private ClientService clientService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private AccountService accountService;
    @RequestMapping("/loans")
   public List<LoanDTO> getLoans() {
       return loanService.getLoans();
    }
    @PostMapping("/loans")
    @Transactional
    public ResponseEntity<Object> createLoan(Authentication authentication, @RequestBody LoanAplicationDTO loanAplicationDTO) {
        Client currentClient = clientService.getClientByEmail(authentication.getName());
        if (loanAplicationDTO.getToAccountNumber()==null)  {
            return new ResponseEntity<>("Account does not exit", HttpStatus.FORBIDDEN);
        }
        if (loanAplicationDTO.getAmount()<= 0){
            return new ResponseEntity<>("Amount must be greater than 0", HttpStatus.FORBIDDEN);

        }
        Loan currentLoan=loanService.getLoanById(loanAplicationDTO.getLoanId());
        if (currentLoan==null){
            return new ResponseEntity<>("Loan does not exist", HttpStatus.FORBIDDEN);
        }
        if( !currentLoan.getPayments().contains(loanAplicationDTO.getPayments())) {
            return new ResponseEntity<>("That number of payments does not exist.", HttpStatus.FORBIDDEN);
        }

        if (loanAplicationDTO.getAmount()>currentLoan.getMaxAmount()){
            return new ResponseEntity<>("Amount exceeding the maximum", HttpStatus.FORBIDDEN);
        }
        Account accountDest=accountService.getByNumber(loanAplicationDTO.getToAccountNumber());

        if (accountDest==null){
            return new ResponseEntity<>("Destination Account does not exist", HttpStatus.FORBIDDEN);
        }
        if (!accountDest.getClient().equals(currentClient)) {

            return new ResponseEntity<>("The account does not belong to the client", HttpStatus.FORBIDDEN);
        }

        loanService.createLoan(currentClient,currentLoan,accountDest,loanAplicationDTO);

        return new ResponseEntity<>("Loan created!", HttpStatus.CREATED);

    }
}
