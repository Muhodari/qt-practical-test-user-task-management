package rw.qt.userms.models.dtos;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class MultipleIdsDTO {
    public List<UUID> ids;
}
