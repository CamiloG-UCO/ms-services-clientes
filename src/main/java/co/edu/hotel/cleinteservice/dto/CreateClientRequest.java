package co.edu.hotel.cleinteservice.dto;

import co.edu.hotel.cleinteservice.domain.DocumentType;
import jakarta.validation.constraints.*;

public record CreateClientRequest(
        @NotNull DocumentType documentType,
        @NotBlank String documentNumber,
        @NotBlank String name,
        @NotBlank String lastNames,
        @Email @NotBlank String email,
        @NotBlank String phone
) {}
