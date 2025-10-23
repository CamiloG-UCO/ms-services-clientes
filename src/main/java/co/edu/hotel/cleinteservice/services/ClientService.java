package co.edu.hotel.cleinteservice.services;

import co.edu.hotel.cleinteservice.domain.Client;
import co.edu.hotel.cleinteservice.repository.ClientRepository;
import co.edu.hotel.cleinteservice.services.ClientCodeGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository repository;
    private final ClientCodeGenerator codeGenerator;
    private static final String PREFIX = "CLI-";

    public ClientService(ClientRepository repository, ClientCodeGenerator codeGenerator) {
        this.repository = repository;
        this.codeGenerator = codeGenerator;
    }

    @Transactional
    public Client create(Client client) {
        if (client.getClientCode() == null || client.getClientCode().isBlank()) {
            // Busca el último código con el prefijo
            Optional<Client> last = repository.findTopByClientCodeStartingWithOrderByClientCodeDesc(PREFIX);
            if (last.isEmpty() || last.get().getClientCode() == null) {
                client.setClientCode(PREFIX + "0001");
            } else {
                client.setClientCode(codeGenerator.generate(PREFIX));
            }
        }
        return repository.save(client);
    }

    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Client> findByDocument(String documentType, String documentNumber) {
        return repository.findByDocumentTypeAndDocumentNumber(documentType, documentNumber);
    }

    @Transactional(readOnly = true)
    public boolean existsByDocument(String documentType, String documentNumber) {
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
}