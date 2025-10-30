package co.edu.hotel.cleinteservice.controller;

import co.edu.hotel.cleinteservice.domain.Client;
import co.edu.hotel.cleinteservice.domain.DocumentType;
import co.edu.hotel.cleinteservice.services.ClientService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "http://localhost:4200")
public class ClientController {

    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Client client) {
        try {
            Client saved = service.create(client);
            String idPath = saved.getId() != null ? saved.getId().toString() : "";
            HttpHeaders headers = new HttpHeaders();
            if (!idPath.isBlank()) {
                headers.setLocation(URI.create("/clients/" + idPath));
            }
            return new ResponseEntity<>(saved, headers, HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            // Si hay validación de duplicados
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Client>> findAll() {
        List<Client> all = service.findAll();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/document")
    public ResponseEntity<Client> findByDocument(
            @RequestParam("number") String documentNumber) {
        Optional<Client> client = service.findByDocument(documentNumber);
        return client.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/email")
    public ResponseEntity<Client> findByEmail(@RequestParam("email") String email) {
        Optional<Client> client = service.findByEmail(email);
        return client.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
    }

    @GetMapping("/phone")
    public ResponseEntity<Client> findByPhone(@RequestParam("phone") String phone) {
        Optional<Client> client = service.findByPhone(phone);
        return client.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/exists/document")
    public ResponseEntity<Boolean> existsByDocument(
            @RequestParam("type") String documentTypeStr,
            @RequestParam("number") String documentNumber) {
        // Verificar si el string coincide con algún valor del enum
        boolean validType = Arrays.stream(DocumentType.values()).anyMatch(d -> d.name().equalsIgnoreCase(documentTypeStr));
        if (!validType) {// Si el tipo de documento no es válido, devolvemos false o 400
            return ResponseEntity.badRequest().body(false);
        }
        DocumentType documentType = DocumentType.valueOf(documentTypeStr.toUpperCase());
        boolean exists = service.existsByDocument(documentType, documentNumber);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/exists/email")
    public ResponseEntity<Boolean> existsByEmail(@RequestParam("email") String email) {
        boolean exists = service.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/exists/phone")
    public ResponseEntity<Boolean> existsByPhone(@RequestParam("phone") String phone) {
        boolean exists = service.existsByPhone(phone);
        return ResponseEntity.ok(exists);
    }

    // Opcional: obtener por id
    @GetMapping("/{id}")
    public ResponseEntity<Client> findById(@PathVariable("id") UUID id) {
        try {
            Optional<Client> client = service.findAll().stream().filter(c -> id.equals(c.getId())).findFirst();
            return client.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody Client client) {
        try {
            service.updateClient(client);
            return ResponseEntity.status(HttpStatus.OK).body("Cliente actualizado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }
}