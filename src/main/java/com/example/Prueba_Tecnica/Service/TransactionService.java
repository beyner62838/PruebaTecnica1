package com.example.Prueba_Tecnica.Service;

import com.example.Prueba_Tecnica.Dto.DepositDTO;
import com.example.Prueba_Tecnica.Dto.WithdrawDTO;
import com.example.Prueba_Tecnica.Dto.TransferDTO;
import com.example.Prueba_Tecnica.Entity.Account;
import com.example.Prueba_Tecnica.Entity.Transaction;
import com.example.Prueba_Tecnica.Entity.Enums.TransactionType;
import com.example.Prueba_Tecnica.IService.ITransactionService;
import com.example.Prueba_Tecnica.Repository.AccountRepository;
import com.example.Prueba_Tecnica.Repository.TransactionRepository;
import com.example.Prueba_Tecnica.Util.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TransactionService implements ITransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void deposit(DepositDTO depositDTO) {
        Account account = accountRepository.findById(depositDTO.getAccountId())
                .orElseThrow(() -> new BusinessException("Account not found"));

        account.setBalance(account.getBalance().add(depositDTO.getAmount()));

        Transaction transaction = Transaction.builder()
                .originAccount(account)
                .transactionType(TransactionType.DEPOSIT)
                .amount(depositDTO.getAmount())
                .date(LocalDateTime.now())
                .description("Deposit")
                .build();

        transactionRepository.save(transaction);
        accountRepository.save(account);
    }

    @Override
    public void withdraw(WithdrawDTO withdrawDTO) {
        Account account = accountRepository.findById(withdrawDTO.getAccountId())
                .orElseThrow(() -> new BusinessException("Account not found"));

        if (account.getBalance().compareTo(withdrawDTO.getAmount()) < 0) {
            throw new BusinessException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(withdrawDTO.getAmount()));

        Transaction transaction = Transaction.builder()
                .originAccount(account)
                .transactionType(TransactionType.WITHDRAWAL)
                .amount(withdrawDTO.getAmount())
                .date(LocalDateTime.now())
                .description("Withdraw")
                .build();

        transactionRepository.save(transaction);
        accountRepository.save(account);
    }

    @Override
    public void transfer(TransferDTO transferDTO) {
        Account originAccount = accountRepository.findById(transferDTO.getOriginAccountId())
                .orElseThrow(() -> new BusinessException("Origin account not found"));

        Account destinationAccount = accountRepository.findById(transferDTO.getDestinationAccountId())
                .orElseThrow(() -> new BusinessException("Destination account not found"));

        if (originAccount.getBalance().compareTo(transferDTO.getAmount()) < 0) {
            throw new BusinessException("Insufficient balance for transfer");
        }

        originAccount.setBalance(originAccount.getBalance().subtract(transferDTO.getAmount()));
        destinationAccount.setBalance(destinationAccount.getBalance().add(transferDTO.getAmount()));

        Transaction transaction = Transaction.builder()
                .originAccount(originAccount)
                .destinationAccount(destinationAccount)
                .transactionType(TransactionType.TRANSFER)
                .amount(transferDTO.getAmount())
                .date(LocalDateTime.now())
                .description("Transfer between accounts")
                .build();

        transactionRepository.save(transaction);
        accountRepository.save(originAccount);
        accountRepository.save(destinationAccount);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Transaction not found"));
    }
}
