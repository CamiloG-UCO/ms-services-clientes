package co.edu.hotel.cleinteservice.services;

import co.edu.hotel.cleinteservice.domain.ClientSequence;
import co.edu.hotel.cleinteservice.repository.ClientSequenceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientCodeGenerator {

    private final ClientSequenceRepository sequenceRepository;
    private static final int WIDTH = 4;

    public ClientCodeGenerator(ClientSequenceRepository sequenceRepository) {
        this.sequenceRepository = sequenceRepository;
    }

    @Transactional
    public String generate(String prefix) {
        // obtener fila con bloqueo para evitar condiciones de carrera
        ClientSequence seq = sequenceRepository.findByNameForUpdate(prefix);
        if (seq == null) {
            seq = new ClientSequence(prefix, 1L);
        } else {
            seq.setValue(seq.getValue() + 1L);
        }
        sequenceRepository.save(seq);
        return prefix + String.format("%0" + WIDTH + "d", seq.getValue());
    }
}