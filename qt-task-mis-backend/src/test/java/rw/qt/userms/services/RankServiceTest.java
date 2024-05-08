//package rw.tekana.v2.userms.services;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import rw.tekana.v2.userms.exceptions.DuplicateRecordException;
//import rw.tekana.v2.userms.exceptions.ResourceNotFoundException;
//import rw.tekana.v2.userms.models.Rank;
//import rw.tekana.v2.userms.models.User;
//import rw.tekana.v2.userms.models.dtos.CreateRankDTO;
//import rw.tekana.v2.userms.models.enums.EStatus;
//import rw.tekana.v2.userms.models.enums.EUserStatus;
//import rw.tekana.v2.userms.repositories.IRankRepository;
//import rw.tekana.v2.userms.repositories.IUserRepository;
//import rw.tekana.v2.userms.services.impl.RankServiceImpl;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.is;
//import static org.hamcrest.Matchers.sameInstance;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertThrows;
//import static org.mockito.Mockito.*;
//
//
//@ExtendWith(MockitoExtension.class)
//public class RankServiceTest {
//    @InjectMocks
//    RankServiceImpl rankService;
//    @Mock
//    IRankRepository rankRepositoryMock;
//
//    @Mock
//    IUserRepository userRepositoryMock;
//
//    @Test
//    public void itShouldGetAllPaginated() {
//        Pageable pageable = (Pageable) PageRequest.of(0, 5, Sort.Direction.DESC,"id");
//
//        List<Rank> ranks = new ArrayList<>(List.of(
//                new Rank("Rank 1", "R1", 1),
//                new Rank("Rank 2", "R2", 2)));
//
//        when(rankRepositoryMock.findAllByStatusNotOrderByPriorityAsc(EStatus.DELETED, pageable))
//                .thenReturn(
//                        new PageImpl<>(ranks)
//                );
//
//        Assertions.assertEquals(2, rankService.getAllPaginated(pageable).getTotalElements());
//        Assertions.assertEquals("R1", rankService.getAllPaginated(pageable).getContent().get(0).getAcronym());
//    }
//
//    @Test
//    public void itShouldGetAllByStatus() {
//        Pageable pageable = (Pageable) PageRequest.of(0, 5, Sort.Direction.DESC,"id");
//
//        List<Rank> ranks = new ArrayList<>(List.of(
//                new Rank("Rank 1", "R1", 1),
//                new Rank("Rank 2", "R2", 2)));
//
//        when(rankRepositoryMock.findAllByStatusOrderByPriorityAsc(Mockito.any(EStatus.class)))
//                .thenReturn(
//                        ranks
//                );
//
//        Assertions.assertEquals(1, rankService.getAllByStatus(EStatus.INACTIVE).get(0).getPriority());
//    }
//
//    @Test
//    public void itShouldSearchAll() {
//        Pageable pageable = (Pageable) PageRequest.of(0, 5, Sort.Direction.DESC,"id");
//
//        List<Rank> ranks = new ArrayList<>(List.of(
//                new Rank("Rank 1", "R1", 1),
//                new Rank("Rank 2", "R2", 2)));
//
//        when(rankRepositoryMock.searchAll(Mockito.any(String.class)))
//                .thenReturn(
//                       ranks
//                );
//
//        Assertions.assertEquals(2, rankService.searchAll("q", pageable).getTotalElements());
//        Assertions.assertEquals(2, rankService.searchAll("q", pageable).getContent().get(1).getPriority());
//    }
//
//    @Test
//    public void whenIsValidData_thenCreateSuccessfully() throws DuplicateRecordException {
//        CreateRankDTO dto = new CreateRankDTO();
//        dto.setName("Rank 1");
//        dto.setAcronym("R1");
//        dto.setPriority(1);
//
//        Rank mock = new Rank("Rank 1", "R1", 1);
//        when(rankRepositoryMock.save(Mockito.any(Rank.class))).thenReturn(mock);
//
//        Rank rank = this.rankService.create(dto);
//
//        assertThat("result", rank, is(sameInstance(mock)));
//        assertEquals("Rank 1", rank.getName());
//    }
//
//    @Test()
//    public void whenIsDuplicateName_thenCreateFailed()  {
//        CreateRankDTO dto1 = new CreateRankDTO();
//        dto1.setName("Rank 1");
//        dto1.setAcronym("R1");
//        dto1.setPriority(1);
//
//        Rank mock1 = new Rank("Rank 1", "R1", 1);
//        when(rankRepositoryMock.findByName(Mockito.any(String.class))).thenReturn(Optional.of(mock1));
//
//        assertThrows(DuplicateRecordException.class, () -> {
//            this.rankService.create(dto1);
//        });
//    }
//
//    @Test()
//    public void whenIsDuplicateAccronym_thenCreateFailed()  {
//        CreateRankDTO dto1 = new CreateRankDTO();
//        dto1.setName("Rank 1");
//        dto1.setAcronym("R1");
//        dto1.setPriority(1);
//
//        Rank mock1 = new Rank("Rank 1", "R2", 1);
//        when(rankRepositoryMock.findByAcronym(Mockito.any(String.class))).thenReturn(Optional.of(mock1));
//
//        assertThrows(DuplicateRecordException.class, () -> {
//            this.rankService.create(dto1);
//        });
//    }
//
//
//    @Test()
//    public void whenIsDuplicatePriority_thenCreateFailed()  {
//        CreateRankDTO dto1 = new CreateRankDTO();
//        dto1.setName("Rank 1");
//        dto1.setAcronym("R1");
//        dto1.setPriority(1);
//
//        Rank mock1 = new Rank("Rank 1", "R1", 1);
//        when(rankRepositoryMock.findByPriority(Mockito.any(Integer.class))).thenReturn(Optional.of(mock1));
//
//        assertThrows(DuplicateRecordException.class, () -> {
//            this.rankService.create(dto1);
//        });
//    }
//
//    @Test
//    public void whenRecordExists_thenGetByIdSuccessfully() throws ResourceNotFoundException {
//        UUID id = UUID.randomUUID();
//
//        Rank mock = new Rank("Rank 1", "R1", 1);
//        mock.setId(id);
//        when(rankRepositoryMock.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(mock));
//
//        Rank rank = this.rankService.getById(id);
//        assertThat("result", rank, is(sameInstance(mock)));
//        assertEquals(mock.getId(), rank.getId());
//    }
//
//    @Test
//    public void whenRecordDoesNotExist_thenGetByIdFailed() {
//        UUID id = UUID.randomUUID();
//
//        Rank mock = new Rank("Rank 1", "R1", 1);
//        mock.setId(id);
//        when(rankRepositoryMock.findById(Mockito.any(UUID.class))).thenReturn(Optional.empty());
//
//        assertThrows(ResourceNotFoundException.class, () -> {
//            this.rankService.getById(UUID.randomUUID());
//        });
//    }
//
//    @Test
//    public void whenIsValidData_thenUpdateSuccessfully() throws DuplicateRecordException, ResourceNotFoundException {
//        UUID id = UUID.randomUUID();
//
//        CreateRankDTO dto = new CreateRankDTO();
//        dto.setName("Rank 1");
//        dto.setAcronym("R1");
//        dto.setPriority(1);
//
//        CreateRankDTO updateDto = new CreateRankDTO();
//        updateDto.setName("Rank 1 Updated Name");
//        updateDto.setAcronym("R1U");
//        updateDto.setPriority(1);
//
//        Rank mock = new Rank("Rank 1", "R1", 1);
//        mock.setId(id);
//        when(rankRepositoryMock.save(Mockito.any(Rank.class))).thenReturn(mock);
//        this.rankService.create(dto);
//
//        when(rankRepositoryMock.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(mock));
//        Rank updated = this.rankService.updateById(id, updateDto);
//
//        assertThat("result", updated, is(sameInstance(mock)));
//        Assertions.assertEquals(mock.getId(), updated.getId());
//        Assertions.assertEquals(updateDto.getName(), updated.getName());
//    }
//
//
//    @Test
//    public void whenRecordDoesNotExist_thenUpdateFailed() {
//        CreateRankDTO updateDto = new CreateRankDTO();
//        updateDto.setName("Rank 1");
//        updateDto.setAcronym("R1");
//        updateDto.setPriority(1);
//
//        when(rankRepositoryMock.findById(Mockito.any(UUID.class))).thenReturn(Optional.empty());
//
//        assertThrows(ResourceNotFoundException.class, () -> {
//            this.rankService.updateById(UUID.randomUUID(), updateDto);
//        });
//    }
//
//    @Test
//    public void whenIsDuplicateName_thenUpdateFailed() {
//        UUID id = UUID.randomUUID();
//        CreateRankDTO updateDto = new CreateRankDTO();
//        updateDto.setName("Rank 2");
//        updateDto.setAcronym("R2");
//        updateDto.setPriority(2);
//
//        Rank mock = new Rank("Rank 1", "R1", 1);
//        mock.setId(id);
//        Rank mock2 = new Rank("R2", "R2", 1);
//        mock2.setId(UUID.randomUUID());
//
//        when(rankRepositoryMock.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(mock));
//        when(rankRepositoryMock.findByName(Mockito.any(String.class))).thenReturn(Optional.of(mock2));
//
//        assertThrows(DuplicateRecordException.class, () -> {
//            this.rankService.updateById(id, updateDto);
//        });
//    }
//
//
//    @Test
//    public void whenIsDuplicateAccronym_thenUpdateFailed() {
//        UUID id = UUID.randomUUID();
//        CreateRankDTO updateDto = new CreateRankDTO();
//        updateDto.setName("Rank 2");
//        updateDto.setAcronym("R2");
//        updateDto.setPriority(2);
//
//        Rank mock = new Rank("Rank 1", "R1", 1);
//        mock.setId(id);
//        Rank mock2 = new Rank("R2", "R2", 1);
//        mock2.setId(UUID.randomUUID());
//
//        when(rankRepositoryMock.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(mock));
//        when(rankRepositoryMock.findByAcronym(Mockito.any(String.class))).thenReturn(Optional.of(mock2));
//
//        assertThrows(DuplicateRecordException.class, () -> {
//            this.rankService.updateById(id, updateDto);
//        });
//    }
//
//    @Test
//    public void whenIsDuplicatePriority_thenUpdateFailed() {
//        UUID id = UUID.randomUUID();
//        CreateRankDTO updateDto = new CreateRankDTO();
//        updateDto.setName("Rank 2");
//        updateDto.setAcronym("R2");
//        updateDto.setPriority(2);
//
//        Rank mock = new Rank("Rank 1", "R1", 1);
//        mock.setId(id);
//        Rank mock2 = new Rank("R2", "R2", 1);
//        mock2.setId(UUID.randomUUID());
//
//        when(rankRepositoryMock.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(mock));
//        when(rankRepositoryMock.findByPriority(Mockito.any(Integer.class))).thenReturn(Optional.of(mock2));
//
//        assertThrows(DuplicateRecordException.class, () -> {
//            this.rankService.updateById(id, updateDto);
//        });
//    }
//
//    @Test
//    public void whenRecordExists_thenUpdateByIdSuccessfully() throws ResourceNotFoundException {
//        UUID id = UUID.randomUUID();
//
//        Rank mock = new Rank("Rank 1", "R1", 1);
//        mock.setId(id);
//         List<User> users = new ArrayList<>(List.of(
//                 new User(),
//                 new User()));
//
//         when(rankRepositoryMock.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(mock));
//
//         when(userRepositoryMock.findAllByRankAndStatusNot(Mockito.any(Rank.class), Mockito.any(EUserStatus.class))).thenReturn((users));
//        when(rankRepositoryMock.save(Mockito.any(Rank.class))).thenReturn(mock);
//
//        this.rankService.deleteById(id);
//
//        Assertions.assertEquals(mock.getStatus(), EStatus.DELETED);
//    }
//
//    @Test
//    public void whenRecordExists_thenChangeStatusByIdSuccessfully() throws ResourceNotFoundException {
//        UUID id = UUID.randomUUID();
//
//        Rank mock = new Rank("Rank 1", "R1", 1);
//        mock.setId(id);
//
//        when(rankRepositoryMock.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(mock));
//        when(rankRepositoryMock.save(Mockito.any(Rank.class))).thenReturn(mock);
//
//        this.rankService.changeStatusById(id, EStatus.INACTIVE);
//
//        Assertions.assertEquals(mock.getStatus(), EStatus.INACTIVE);
//
//    }
//
//    @Test
//    public void whenRecordExists_thenDeleteByIdSuccessfully() throws ResourceNotFoundException {
//        UUID id = UUID.randomUUID();
//
//        Rank mock = new Rank("Rank 1", "R1", 1);
//        mock.setId(id);
//        when(rankRepositoryMock.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(mock));
//        when(userRepositoryMock.findAllByRankAndStatusNot(Mockito.any(Rank.class), Mockito.any(EUserStatus.class))).thenReturn(new ArrayList<>());
//        doNothing().when(rankRepositoryMock).deleteById(Mockito.any(UUID.class));
//       this.rankService.deleteById(id);
//
//        verify(rankRepositoryMock, times(1)).deleteById(id);
//    }
//
//    @Test
//    public void whenRecordDoesNotExist_thenDeleteByIdFailed() {
//        UUID id = UUID.randomUUID();
//
//        Rank mock = new Rank("Rank 1", "R1", 1);
//        mock.setId(id);
//        when(rankRepositoryMock.findById(Mockito.any(UUID.class))).thenReturn(Optional.empty());
//
//        assertThrows(ResourceNotFoundException.class, () -> {
//            this.rankService.deleteById(id);
//        });
//    }
//
//}
//
