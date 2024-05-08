package rw.qt.userms.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rw.qt.userms.models.Task;
import rw.qt.userms.models.enums.EPriority;
import rw.qt.userms.models.enums.EStatus;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ITaskRepository extends JpaRepository<Task, UUID> {

    @Query("""
            SELECT t FROM Task t WHERE (t.name ILIKE CONCAT('%', :query, '%') OR t.description ILIKE CONCAT('%', :query, '%'))  AND t.status <> 'DELETED'
            """)
    Page<Task> searchAll(String query, Pageable pageable);

    @Query("""
            SELECT t FROM Task t
           WHERE (:query IS NULL OR :query = '' OR t.name ILIKE CONCAT('%', :query, '%') OR t.description ILIKE CONCAT('%', :query, '%'))
           AND (:status IS NULL OR t.status = :status)
           AND (:priority IS NULL OR t.priority = :priority)
            """)
    Page<Task> searchWithStatusAndPriority(String query, EStatus status, EPriority priority, Pageable pageable);


    Optional<Task> findByAssigneesIdAndId(UUID assigneeId, UUID id);
    Optional<Task> findByProjectsIdAndId(UUID projectId, UUID id);
}
