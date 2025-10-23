package co.edu.hotel.cleinteservice.repository;

import co.edu.hotel.cleinteservice.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {

    // Obtener el último registro cuyo clientCode comienza con el prefijo (orden descendente)
    Optional<Client> findTopByClientCodeStartingWithOrderByClientCodeDesc(String prefix);

    // Consultas comunes
    Optional<Client> findByDocumentTypeAndDocumentNumber(String documentType, String documentNumber);
    Optional<Client> findByDocumentNumber(String documentNumber);
    Optional<Client> findByEmail(String email);
    Optional<Client> findByPhone(String phone);

    // Comprobaciones de existencia
    boolean existsByDocumentTypeAndDocumentNumber(String documentType, String documentNumber);
    boolean existsByDocumentNumber(String documentNumber);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}