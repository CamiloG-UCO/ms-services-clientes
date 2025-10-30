package co.edu.hotel.cleinteservice.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Data
@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "client_code", unique = true, nullable = false)
    private String clientCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false)
    private DocumentType documentType;

    @Column(name = "document_number", unique = true, nullable = false)
    private String documentNumber;

    @Column(nullable = false)
    private String name;

    @Column(name = "last_names", nullable = false)
    private String lastNames;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String phone;
}