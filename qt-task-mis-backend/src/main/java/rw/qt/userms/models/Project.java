package rw.qt.userms.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import rw.qt.userms.models.dtos.CreateProjectDTO;
import rw.qt.userms.models.enums.EStatus;

import java.io.Serial;
import java.util.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "projects")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Project implements GrantedAuthority {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable=false, unique=true)
    private String name;

    @Column(name="description")
    private String description;


    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private EStatus status = EStatus.ACTIVE;

    @JsonIgnore
    @ManyToMany(mappedBy = "projects", fetch = FetchType.LAZY)
    private List<Task> tasks = new ArrayList<>();

    public Project(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Project(String name, String description, EStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }



    public Project(CreateProjectDTO dto) {
        this.name = dto.getName();
        this.description = dto.getDescription();
    }

    @JsonIgnore
    public String getIdString() {
       return this.id.toString();
    }

    @Override
    public String getAuthority() {
        return name;
    }
}
