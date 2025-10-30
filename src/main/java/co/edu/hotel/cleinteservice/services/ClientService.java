package co.edu.hotel.cleinteservice.services;

import co.edu.hotel.cleinteservice.domain.Client;
import co.edu.hotel.cleinteservice.domain.DocumentType;
import co.edu.hotel.cleinteservice.exceptions.ClientAlreadyExistsException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de cliente usando un mapa en memoria (in-memory Map)
 * para simular la persistencia, eliminando la dependencia del ClientRepository.
 */
@Service
public class ClientService {

    // Almacenamiento en memoria: Clave = Número de Documento (asumido único)
    private final Map<String, Client> clientStore = new ConcurrentHashMap<>();

    private final ClientCodeGenerator codeGenerator;
    private static final String PREFIX = "CLI-";

    // Se eliminó la inyección de ClientRepository
    public ClientService(ClientCodeGenerator codeGenerator) {
        this.codeGenerator = codeGenerator;
    }

    public Client create(Client client) {
        // VALIDACIÓN DE NEGOCIO: Evitar duplicidad de documento (la clave es el número de documento)
        if (clientStore.containsKey(client.getDocumentNumber())) {
            throw new ClientAlreadyExistsException(
                    client.getDocumentType().toString(),
                    client.getDocumentNumber()
            );
        }

        // Generación de código
        if (client.getClientCode() == null || client.getClientCode().isBlank()) {
            // Simular generación de código secuencial
            String nextCode = codeGenerator.generate(PREFIX);
            client.setClientCode(nextCode);
        }

        // Simular guardado en base de datos (se usa el documentNumber como clave)
        clientStore.put(client.getDocumentNumber(), client);

        return client;
    }

    public Client update(Client client) {
        if (!clientStore.containsKey(client.getDocumentNumber())) {
            throw new RuntimeException("Client not found for update: " + client.getDocumentNumber());
        }
        // Simular actualización
        clientStore.put(client.getDocumentNumber(), client);
        return client;
    }

    public List<Client> findAll() {
        return List.copyOf(clientStore.values());
    }

    public Optional<Client> findByDocument(DocumentType documentType, String documentNumber) {
        // Búsqueda por número de documento (la clave) y filtrado por tipo
        return Optional.ofNullable(clientStore.get(documentNumber))
                .filter(c -> c.getDocumentType().equals(documentType));
    }

    public boolean existsByDocument(DocumentType documentType, String documentNumber) {
        return findByDocument(documentType, documentNumber).isPresent();
    }

    public Optional<Client> findByEmail(String email) {
        return clientStore.values().stream()
                .filter(c -> email.equals(c.getEmail()))
                .findFirst();
    }

    public Optional<Client> findByPhone(String phone) {
        return clientStore.values().stream()
                .filter(c -> phone.equals(c.getPhone()))
                .findFirst();
    }

    public boolean existsByDocumentNumber(String documentNumber) {
        return clientStore.containsKey(documentNumber);
    }

    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }

    public boolean existsByPhone(String phone) {
        return findByPhone(phone).isPresent();
    }

    /**
     * Método auxiliar para limpieza en pruebas BDD.
     */
    public void deleteByDocumentNumber(String documentNumber) {
        clientStore.remove(documentNumber);
    }
}