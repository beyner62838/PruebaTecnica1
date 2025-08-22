package com.example.Prueba_Tecnica.Service;

import com.example.Prueba_Tecnica.Entity.Account;
import com.example.Prueba_Tecnica.Entity.Client;
import com.example.Prueba_Tecnica.Entity.Enums.AccountStatus;
import com.example.Prueba_Tecnica.Entity.Enums.AccountType;
import com.example.Prueba_Tecnica.Repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    @Mock
    private AccountRepository repository;

    @InjectMocks
    private AccountService accountService;

    private Account savingsAccount;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Client john = Client.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build();

        savingsAccount = Account.builder()
                .id(1L)
                .accountType(AccountType.SAVINGS)
                .accountNumber("5312345678")
                .status(AccountStatus.ACTIVE)
                .balance(new BigDecimal("0.00"))
                .gmfExempt(false)
                .client(john)
                .build();
    }

    @Test
    void shouldActivateAccount() {
        when(repository.findById(1L)).thenReturn(Optional.of(savingsAccount));
        accountService.activateAccount(1L);

        assertEquals(AccountStatus.ACTIVE, savingsAccount.getStatus());
    }

    @Test
    void shouldDeactivateAccount() {
        when(repository.findById(1L)).thenReturn(Optional.of(savingsAccount));
        accountService.deactivateAccount(1L);

        assertEquals(AccountStatus.INACTIVE, savingsAccount.getStatus());
    }

    @Test
    void shouldCloseAccountWithZeroBalance() {
        when(repository.findById(1L)).thenReturn(Optional.of(savingsAccount));
        accountService.cancelAccount(1L);

        assertEquals(AccountStatus.CLOSED, savingsAccount.getStatus());
    }
}
