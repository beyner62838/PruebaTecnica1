package com.example.Prueba_Tecnica.Service;

import com.example.Prueba_Tecnica.Entity.Account;
import com.example.Prueba_Tecnica.Entity.Enums.AccountStatus;
import com.example.Prueba_Tecnica.IService.IAccountService;
import com.example.Prueba_Tecnica.Repository.AccountRepository;
import com.example.Prueba_Tecnica.Util.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AccountService implements IAccountService {

    @Autowired
    private AccountRepository repository;

    @Override
    public List<Account> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Account> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Account save(Account account) {
        account.setCreationDate(LocalDateTime.now());
        account.setModificationDate(LocalDateTime.now());
        account.setActive(true);
        return repository.save(account);
    }

    @Override
    public Account update(Account accountDetails, Long id) {
        return repository.findById(id).map(account -> {
            account.setAccountType(accountDetails.getAccountType());
            account.setAccountNumber(accountDetails.getAccountNumber());
            account.setStatus(accountDetails.getStatus());
            account.setBalance(accountDetails.getBalance());
            account.setGmfExempt(accountDetails.isGmfExempt());
            account.setModificationDate(LocalDateTime.now());
            return repository.save(account);
        }).orElseThrow(() -> new BusinessException("Account not found with id " + id));
    }

    @Override
    public void delete(Long id) {
        Account account = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Account not found with id " + id));
        account.setActive(false);
        account.setModificationDate(LocalDateTime.now());
        repository.save(account);
    }

    @Override
    public void activateAccount(Long id) {
        Account account = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Account not found with id " + id));
        account.setStatus(AccountStatus.ACTIVE);
        repository.save(account);
    }

    @Override
    public void deactivateAccount(Long id) {
        Account account = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Account not found with id " + id));
        account.setStatus(AccountStatus.INACTIVE);
        repository.save(account);
    }

    @Override
    public void cancelAccount(Long id) {
        Account account = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Account not found with id " + id));
        if (account.getBalance().compareTo(java.math.BigDecimal.ZERO) == 0) {
            account.setStatus(AccountStatus.CLOSED);
            repository.save(account);
        } else {
            throw new BusinessException("Cannot cancel an account with a non-zero balance.");
        }
    }
}
