package co.edu.hotel.cleinteservice.config;

import co.edu.hotel.cleinteservice.domain.Role;
import co.edu.hotel.cleinteservice.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.UUID;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner loadRoles(RoleRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                List<Role> roles = List.of(
                        new Role(UUID.fromString("2bed922b-bc39-4842-8297-4a3d9be34856"), "RECEPTIONIST", "Recepcionista del hotel"),
                        new Role(UUID.fromString("1f674d6a-7978-42e3-87fb-af0740c9e8d2"), "ADMIN", "Administrador del sistema"),
                        new Role(UUID.fromString("878c5024-f10c-4d35-a8a1-b78332344e54"), "CUSTOMER", "Cliente del hotel"),
                        new Role(UUID.fromString("a1541ebd-1323-43a7-8d16-30309bb6ade9"), "STAFF", "Personal del hotel")
                );
                repository.saveAll(roles);
                System.out.println("✅ Roles cargados correctamente");
            }
        };
    }
}
