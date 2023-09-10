package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.LoanAplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Loan;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface LoanService  {
    List<LoanDTO> getLoans();
    Loan getLoanById(Long id);
    void createLoan(Client currentClient, Loan currentLoan, Account accountDest, LoanAplicationDTO loanAplicationDTO);

}

