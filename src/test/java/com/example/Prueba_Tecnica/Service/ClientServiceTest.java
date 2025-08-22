package com.example.Prueba_Tecnica.Service;

import com.example.Prueba_Tecnica.Entity.Client;
import com.example.Prueba_Tecnica.Repository.ClientRepository;
import com.example.Prueba_Tecnica.Util.BusinessException;
import com.example.Prueba_Tecnica.Util.ClientMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientServiceTest {

    @Mock
    private ClientRepository repository;

    @InjectMocks
    private ClientService clientService;

    private Client john;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        john = Client.builder()
                .id(1L)
                .identificationType("CC")
                .identificationNumber("123456789")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .birthDate(LocalDate.of(1990, 5, 10))
                .build();
    }

    @Test
    void shouldSaveValidClient() {
        when(repository.save(any(Client.class))).thenReturn(john);
        Client saved = clientService.save(john);

        assertNotNull(saved);
        assertEquals("John", saved.getFirstName());
        verify(repository, times(1)).save(any(Client.class));
    }

    @Test
    void shouldThrowWhenClientIsUnderage() {
        Client minor = Client.builder()
                .firstName("Ana")
                .lastName("Perez")
                .email("ana@example.com")
                .birthDate(LocalDate.now().minusYears(15))
                .identificationType("TI")
                .identificationNumber("987654321")
                .build();

        Exception ex = assertThrows(BusinessException.class, () -> clientService.save(minor));
        assertEquals(ClientMessages.CLIENT_MUST_BE_ADULT, ex.getMessage());
    }

    @Test
    void shouldUpdateClient() {
        when(repository.findById(1L)).thenReturn(Optional.of(john));
        when(repository.save(any(Client.class))).thenReturn(john);

        john.setLastName("Smith");
        Client updated = clientService.update(john, 1L);

        assertEquals("Smith", updated.getLastName());
        verify(repository, times(1)).save(any(Client.class));
    }
}
