package co.edu.hotel.cleinteservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "client_sequences")
public class ClientSequence {

    @Id
    private String name;

    @Column(nullable = false)
    private long value;

    public ClientSequence() {}

    public ClientSequence(String name, long value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}