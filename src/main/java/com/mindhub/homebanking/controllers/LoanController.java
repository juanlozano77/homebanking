package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class LoanController {
    @Autowired
    private LoanRepository repo;

    @RequestMapping("/loans")
    public List<LoanDTO> getLoans() {
        return repo.findAll().stream().map(LoanDTO::new).collect(toList());

    }
    @RequestMapping("/loans/{id}")
    public LoanDTO getLoans(@PathVariable Long id){
        return repo.findById(id).map(LoanDTO::new).orElse(null);
    }

}
