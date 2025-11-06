package co.edu.hotel.cleinteservice.web;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String,String>> conflict(IllegalArgumentException ex) {
        HttpStatus status = ex.getMessage().toLowerCase().contains("existe") ? HttpStatus.CONFLICT : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> badReq(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(Map.of("message","Datos inválidos"));
    }
}
