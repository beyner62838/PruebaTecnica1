package com.example.Prueba_Tecnica.Service;

import com.example.Prueba_Tecnica.Dto.ClientDTO;
import com.example.Prueba_Tecnica.Entity.Client;
import com.example.Prueba_Tecnica.IService.IClientService;
import com.example.Prueba_Tecnica.Repository.ClientRepository;
import com.example.Prueba_Tecnica.Util.BusinessException;
import com.example.Prueba_Tecnica.Util.ClientMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClientService implements IClientService {

    @Autowired
    private ClientRepository repository;

    @Override
    public Optional<Client> findByIdentificationNumber(String identificationNumber) {
        return repository.findByIdentificationNumber(identificationNumber);
    }

    @Override
    public boolean existsByIdentificationNumber(String identificationNumber) {
        return repository.existsByIdentificationNumber(identificationNumber);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public List<Client> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Client> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Client save(Client client) {
        client.setCreationDate(LocalDateTime.now());
        client.setModificationDate(LocalDateTime.now());
        client.setActive(true);
        return repository.save(client);
    }

    @Override
    public Client update(Client clientDetails, Long id) {
        return repository.findById(id).map(client -> {
            client.setFirstName(clientDetails.getFirstName());
            client.setLastName(clientDetails.getLastName());
            client.setIdentificationNumber(clientDetails.getIdentificationNumber());
            client.setEmail(clientDetails.getEmail());
            client.setBirthDate(clientDetails.getBirthDate());
            client.setModificationDate(LocalDateTime.now());
            return repository.save(client);
        }).orElseThrow(() -> new BusinessException(ClientMessages.CLIENT_NOT_FOUND + id));
    }

    @Override
    public void delete(Long id) {
        Client client = repository.findById(id)
                .orElseThrow(() -> new BusinessException(ClientMessages.CLIENT_NOT_FOUND + id));
        client.setActive(false);
        client.setModificationDate(LocalDateTime.now());
        repository.save(client);
    }

    // Métodos para trabajar con DTOs
    public ClientDTO createClient(ClientDTO clientDTO) {
        Client client = Client.builder()
                .firstName(clientDTO.getFirstName())
                .lastName(clientDTO.getLastName())
                .identificationNumber(clientDTO.getIdentificationNumber())
                .email(clientDTO.getEmail())
                .birthDate(clientDTO.getBirthDate())
                .build();
        Client saved = save(client);
        return toDTO(saved);
    }

    public List<ClientDTO> getAllClients() {
        return findAll().stream().map(this::toDTO).toList();
    }

    public ClientDTO getClientById(Long id) {
        Client client = findById(id)
                .orElseThrow(() -> new BusinessException(ClientMessages.CLIENT_NOT_FOUND + id));
        return toDTO(client);
    }

    public ClientDTO updateClient(Long id, ClientDTO clientDTO) {
        Client clientDetails = Client.builder()
                .firstName(clientDTO.getFirstName())
                .lastName(clientDTO.getLastName())
                .identificationNumber(clientDTO.getIdentificationNumber())
                .email(clientDTO.getEmail())
                .birthDate(clientDTO.getBirthDate())
                .build();
        Client updated = update(clientDetails, id);
        return toDTO(updated);
    }

    public void deleteClient(Long id) {
        delete(id);
    }

    private ClientDTO toDTO(Client client) {
        return ClientDTO.builder()
                .id(client.getId())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .identificationNumber(client.getIdentificationNumber())
                .email(client.getEmail())
                .birthDate(client.getBirthDate())
                .build();
    }
}
