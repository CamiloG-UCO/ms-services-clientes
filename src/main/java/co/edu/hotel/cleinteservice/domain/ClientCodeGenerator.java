package co.edu.hotel.cleinteservice.domain;

import java.util.concurrent.atomic.AtomicInteger;

public class ClientCodeGenerator {
    private final AtomicInteger seq = new AtomicInteger(1000);
    public String next() { return "CLI-" + seq.incrementAndGet(); }
}

