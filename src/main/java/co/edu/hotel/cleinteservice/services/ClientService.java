package co.edu.hotel.cleinteservice.services;

import co.edu.hotel.cleinteservice.domain.Client;
import co.edu.hotel.cleinteservice.domain.ClientCodeGenerator;
import co.edu.hotel.cleinteservice.domain.DocumentType;
import co.edu.hotel.cleinteservice.dto.ClientResponse;
import co.edu.hotel.cleinteservice.dto.CreateClientRequest;
import co.edu.hotel.cleinteservice.repository.ClientRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository repository;
    private final ClientCodeGenerator codeGenerator = new ClientCodeGenerator();

    public ClientService(ClientRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ClientResponse create(CreateClientRequest r) {
        if (repository.existsByDocumentTypeAndDocumentNumber(r.documentType(), r.documentNumber()))
            throw new IllegalArgumentException("Cliente ya existe (documento)");
        if (repository.existsByEmail(r.email()))
            throw new IllegalArgumentException("Cliente ya existe (email)");

        String clientCode = codeGenerator.next();

        Client client = Client.builder()
                .clientCode(clientCode)
                .documentType(r.documentType())
                .documentNumber(r.documentNumber())
                .name(r.name())
                .lastNames(r.lastNames())
                .email(r.email())
                .phone(r.phone())
                .build();

        try {
            repository.save(client);
        } catch (DataIntegrityViolationException e) {
            // fallback por si llegan duplicados concurrentes
            throw new IllegalArgumentException("Duplicidad detectada");
        }
        return new ClientResponse(client.getClientCode(),
                r.name() + " " + r.lastNames(),
                r.email(),
                r.phone());
    }

    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Client> findByDocument(String documentNumber) {
        return repository.findByDocumentNumber(documentNumber);
    }

    @Transactional(readOnly = true)
    public boolean existsByDocument(DocumentType documentType, String documentNumber) {
        if (documentType == null || documentNumber == null || documentNumber.isBlank()) return false;
        return repository.existsByDocumentTypeAndDocumentNumber(documentType, documentNumber);
    }

    @Transactional(readOnly = true)
    public Optional<Client> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<Client> findByPhone(String phone) {
        return repository.findByPhone(phone);
    }

    @Transactional(readOnly = true)
    public boolean existsByDocumentNumber(String documentNumber) {
        return repository.existsByDocumentNumber(documentNumber);
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean existsByPhone(String phone) {
        return repository.existsByPhone(phone);
    }

    @Transactional
    public void updateClient(Client client) {
        Optional<Client> clientOptional = repository.findById(client.getId());
        if (clientOptional.isPresent()) {
            Client c = clientOptional.get();
            c.setName(client.getName());
            c.setEmail(client.getEmail());
            c.setPhone(client.getPhone());
            c.setDocumentNumber(client.getDocumentNumber());
            repository.save(c);
        } else {
            throw new IllegalArgumentException("el cliente no fue encontrado");
        }
    }
}
