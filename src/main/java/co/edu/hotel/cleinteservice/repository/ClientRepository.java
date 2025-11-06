package co.edu.hotel.cleinteservice.repository;

import co.edu.hotel.cleinteservice.domain.Client;
import co.edu.hotel.cleinteservice.domain.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    // Buscar cliente por tipo y número de documento
    Optional<Client> findByDocumentTypeAndDocumentNumber(DocumentType documentType, String documentNumber);

    // Verificar existencia por tipo y número de documento
    boolean existsByDocumentTypeAndDocumentNumber(DocumentType documentType, String documentNumber);

    // Buscar por número de documento (sin tipo, si lo necesitas)
    Optional<Client> findByDocumentNumber(String documentNumber);

    // Buscar por correo electrónico
    Optional<Client> findByEmail(String email);

    // Buscar por teléfono
    Optional<Client> findByPhone(String phone);

    // Verificar existencia por número de documento
    boolean existsByDocumentNumber(String documentNumber);

    // Verificar existencia por correo electrónico
    boolean existsByEmail(String email);

    // Verificar existencia por teléfono
    boolean existsByPhone(String phone);
}
