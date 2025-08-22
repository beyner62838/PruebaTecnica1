package com.example.Prueba_Tecnica.Service;

import com.example.Prueba_Tecnica.Dto.DepositDTO;
import com.example.Prueba_Tecnica.Dto.WithdrawDTO;
import com.example.Prueba_Tecnica.Dto.TransferDTO;
import com.example.Prueba_Tecnica.Entity.Account;
import com.example.Prueba_Tecnica.Entity.Client;
import com.example.Prueba_Tecnica.Entity.Enums.AccountStatus;
import com.example.Prueba_Tecnica.Entity.Enums.AccountType;
import com.example.Prueba_Tecnica.Repository.AccountRepository;
import com.example.Prueba_Tecnica.Repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Account savingsAccount;
    private Account checkingAccount;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Client maria = Client.builder()
                .id(1L)
                .firstName("Maria")
                .lastName("Gomez")
                .build();

        savingsAccount = Account.builder()
                .id(1L)
                .accountType(AccountType.SAVINGS)
                .accountNumber("5311111111")
                .status(AccountStatus.ACTIVE)
                .balance(new BigDecimal("1000.00"))
                .client(maria)
                .build();

        checkingAccount = Account.builder()
                .id(2L)
                .accountType(AccountType.CHECKING)
                .accountNumber("3311111111")
                .status(AccountStatus.ACTIVE)
                .balance(new BigDecimal("500.00"))
                .client(maria)
                .build();
    }

    @Test
    void shouldDepositMoney() {
        DepositDTO dto = DepositDTO.builder()
                .accountId(1L)
                .amount(new BigDecimal("200.00"))
                .build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(savingsAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(savingsAccount);

        transactionService.deposit(dto);

        assertEquals(new BigDecimal("1200.00"), savingsAccount.getBalance());
        verify(transactionRepository, times(1)).save(any());
        verify(accountRepository, times(1)).save(any());
    }

    @Test
    void shouldWithdrawMoney() {
        WithdrawDTO dto = WithdrawDTO.builder()
                .accountId(1L)
                .amount(new BigDecimal("300.00"))
                .build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(savingsAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(savingsAccount);

        transactionService.withdraw(dto);

        assertEquals(new BigDecimal("700.00"), savingsAccount.getBalance());
        verify(transactionRepository, times(1)).save(any());
        verify(accountRepository, times(1)).save(any());
    }

    @Test
    void shouldTransferMoneyBetweenAccounts() {
        TransferDTO dto = TransferDTO.builder()
                .originAccountId(1L)
                .destinationAccountId(2L)
                .amount(new BigDecimal("400.00"))
                .build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(savingsAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(checkingAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(savingsAccount);

        transactionService.transfer(dto);

        assertEquals(new BigDecimal("600.00"), savingsAccount.getBalance());
        assertEquals(new BigDecimal("900.00"), checkingAccount.getBalance());
        verify(transactionRepository, times(1)).save(any());
        verify(accountRepository, times(2)).save(any());
    }
}
