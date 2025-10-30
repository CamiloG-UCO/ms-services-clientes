package co.edu.hotel.cleinteservice.repository;

import co.edu.hotel.cleinteservice.domain.ClientSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.persistence.LockModeType;

@Repository
public interface ClientSequenceRepository extends JpaRepository<ClientSequence, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from ClientSequence s where s.name = :name")
    ClientSequence findByNameForUpdate(@Param("name") String name);
}