package co.edu.hotel.cleinteservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * Entidad que representa un cliente del hotel.
 */
@Entity
@Table(name = "clients",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_document", columnNames = {"document_type", "document_number"}),
                @UniqueConstraint(name = "uk_email", columnNames = {"email"})
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String clientCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false)
    private DocumentType documentType;

    @Column(name = "document_number", nullable = false, length = 30)
    private String documentNumber;

    @Column(nullable = false, length = 60)
    private String name;

    @Column(name = "last_names", nullable = false, length = 60)
    private String lastNames;

    @Email
    @Column(nullable = false, length = 120)
    private String email;

    @Column(nullable = false, length = 20)
    private String phone;
}
