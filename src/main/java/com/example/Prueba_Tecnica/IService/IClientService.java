package com.example.Prueba_Tecnica.IService;

import com.example.Prueba_Tecnica.Entity.Client;
import java.util.List;
import java.util.Optional;

public interface IClientService {

    Optional<Client> findByIdentificationNumber(String identificationNumber);

    boolean existsByIdentificationNumber(String identificationNumber);

    boolean existsByEmail(String email);

    List<Client> findAll();

    Optional<Client> findById(Long id);

    Client save(Client client);

    Client update(Client client, Long id);

    void delete(Long id); // 👈 Logical delete only
}
