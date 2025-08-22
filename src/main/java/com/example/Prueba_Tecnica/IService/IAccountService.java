package com.example.Prueba_Tecnica.IService;

import com.example.Prueba_Tecnica.Entity.Account;
import java.util.List;
import java.util.Optional;

public interface IAccountService {

    List<Account> findAll();
    Optional<Account> findById(Long id);
    Account save(Account account);
    Account update(Account account, Long id);
    void delete(Long id); // 👈 Logical delete instead of physical
    void activateAccount(Long id);
    void deactivateAccount(Long id);
    void cancelAccount(Long id);
}
