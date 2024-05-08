package rw.qt.userms.repositories;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.qt.userms.models.File;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IFileRepository extends JpaRepository<File, UUID>{

    Optional<File> findByName(String name);


    Optional<File> findById(@NotNull UUID id);
}
