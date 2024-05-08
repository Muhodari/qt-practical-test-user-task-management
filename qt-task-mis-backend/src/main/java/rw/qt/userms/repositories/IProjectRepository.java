package rw.qt.userms.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rw.qt.userms.models.Project;
import rw.qt.userms.models.enums.EStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IProjectRepository extends JpaRepository<Project, UUID> {
    Page<Project> findAllByStatusNot(EStatus status, Pageable pageable);

    Optional<Project> findByName(String name);

    @Query("SELECT p FROM Project p WHERE (:query IS NULL OR :query = '' OR p.name ILIKE CONCAT('%', :query, '%') OR p.description ILIKE CONCAT('%', :query, '%'))" +
            " AND p.status <> 'DELETED'")
    Page<Project> searchAll(String query, Pageable pageable);
}
