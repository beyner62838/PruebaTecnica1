package com.example.Prueba_Tecnica.Controller;

import com.example.Prueba_Tecnica.Dto.DepositDTO;
import com.example.Prueba_Tecnica.Dto.WithdrawDTO;
import com.example.Prueba_Tecnica.Dto.TransferDTO;
import com.example.Prueba_Tecnica.Service.TransactionService;
import com.example.Prueba_Tecnica.Util.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody DepositDTO depositDTO) {
        transactionService.deposit(depositDTO);
        return ResponseEntity.ok("Deposit completed successfully");
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody WithdrawDTO withdrawDTO) {
        transactionService.withdraw(withdrawDTO);
        return ResponseEntity.ok("Withdrawal completed successfully");
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransferDTO transferDTO) {
        transactionService.transfer(transferDTO);
        return ResponseEntity.ok("Transfer completed successfully");
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleBusinessException(BusinessException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}