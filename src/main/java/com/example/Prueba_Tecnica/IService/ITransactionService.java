package com.example.Prueba_Tecnica.IService;

import com.example.Prueba_Tecnica.Dto.DepositDTO;
import com.example.Prueba_Tecnica.Dto.WithdrawDTO;
import com.example.Prueba_Tecnica.Dto.TransferDTO;
import com.example.Prueba_Tecnica.Entity.Transaction;
import java.util.List;

public interface ITransactionService {
    void deposit(DepositDTO depositDTO);
    void withdraw(WithdrawDTO withdrawDTO);
    void transfer(TransferDTO transferDTO);
    List<Transaction> getAllTransactions();
    Transaction getTransactionById(Long id);
}
