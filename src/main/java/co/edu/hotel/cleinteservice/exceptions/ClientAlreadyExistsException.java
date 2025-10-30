package co.edu.hotel.cleinteservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando se intenta crear un cliente con un documento que ya existe.
 * Mapea automáticamente a un código de estado HTTP 409 Conflict, ideal para la API.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ClientAlreadyExistsException extends RuntimeException {

    public ClientAlreadyExistsException(String documentType, String documentNumber) {
        super(String.format("Ya existe un cliente registrado con el tipo de documento %s y número %s.",
                documentType, documentNumber));
    }
}
