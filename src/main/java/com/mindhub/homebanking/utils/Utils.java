package com.mindhub.homebanking.utils;

import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;

public class Utils {
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountRepository accountRepository;
    public static String generateAccountNumber(AccountRepository accountRepository) {
        String accountNumber;
        do {
            int randomNumber = (int) (Math.random() * 99999999) + 1;
            accountNumber = "VIN" + randomNumber;
        } while (accountRepository.findByNumber(accountNumber) != null);
        return accountNumber;
    }
    public static int generateNumber(int digit) {
        int number;
        number = (int) (Math.random() * Math.pow(10, digit)) + 1;
        return number;
    }
    public static String generateCardNumber() {
        StringBuilder formattedNumber = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int number = generateNumber(4);
            if (number < 1000) {
                formattedNumber.append(String.format("%04d", number));
            } else {
                formattedNumber.append(number);
            }
            if (i<3) {
                formattedNumber.append("-");
            }
        }
        return formattedNumber.toString();
    }


}

