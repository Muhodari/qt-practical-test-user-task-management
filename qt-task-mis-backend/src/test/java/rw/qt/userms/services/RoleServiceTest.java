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
//import rw.tekana.v2.userms.models.Role;
//import rw.tekana.v2.userms.models.User;
//import rw.tekana.v2.userms.models.dtos.CreateRankDTO;
//import rw.tekana.v2.userms.models.dtos.CreateRoleDTO;
//import rw.tekana.v2.userms.models.dtos.UpdateRoleDTO;
//import rw.tekana.v2.userms.models.enums.EStatus;
//import rw.tekana.v2.userms.repositories.IRankRepository;
//import rw.tekana.v2.userms.repositories.IRoleRepository;
//import rw.tekana.v2.userms.repositories.IUserRepository;
//import rw.tekana.v2.userms.services.impl.RoleServiceImpl;
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
//public class RoleServiceTest {
//    @InjectMocks
//    RoleServiceImpl roleService;
//    @Mock
//    IRoleRepository roleRepositoryMock;
//
//    @Mock
//    IUserRepository userRepositoryMock;
//
//    @Test
//    public void itShouldGetAllPaginated() {
//        Pageable pageable = (Pageable) PageRequest.of(0, 5, Sort.Direction.DESC,"id");
//
//        List<Role> roles = new ArrayList<>(List.of(
//                new Role("Role 1", "Role 1 Description"),
//                new Role("Role 2", "Role 2 Description")));
//
//        when(roleRepositoryMock.findAllByStatusNot(EStatus.DELETED, pageable))
//                .thenReturn(
//                        new PageImpl<>(roles)
//                );
//
//        Assertions.assertEquals(2, roleService.getAllPaginated(pageable).getTotalElements());
//        Assertions.assertEquals("Role 1", roleService.getAllPaginated(pageable).getContent().get(0).getName());
//    }
//
//    @Test
//    public void itShouldGetAllByStatus() {
//        Pageable pageable = (Pageable) PageRequest.of(0, 5, Sort.Direction.DESC,"id");
//
//        List<Role> roles = new ArrayList<>(List.of(
//                new Role("Role 1", "Role 1 Description"),
//                new Role("Role 2", "Role 2 Description")));
//
//        when(roleRepositoryMock.findAllByStatus(Mockito.any(EStatus.class)))
//                .thenReturn(
//                        roles
//                );
//
//        Assertions.assertEquals("Role 1", roleService.getAllByStatus(EStatus.INACTIVE).get(0).getName());
//    }
//
//
//    @Test
//    public void itShouldGetAllAttachedPrivileges() {
//        Pageable pageable = (Pageable) PageRequest.of(0, 5, Sort.Direction.DESC,"id");
//
//        List<Role> roles = new ArrayList<>(List.of(
//                new Role("Role 1", "Role 1 Description"),
//                new Role("Role 2", "Role 2 Description")));
//
//        when(roleRepositoryMock.findAllByStatus(Mockito.any(EStatus.class)))
//                .thenReturn(
//                        roles
//                );
//
//        Assertions.assertEquals("Role 1", roleService.getAllByStatus(EStatus.INACTIVE).get(0).getName());
//    }
//
//
//
//    @Test
//    public void itShouldSearchAll() {
//        Pageable pageable = (Pageable) PageRequest.of(0, 5, Sort.Direction.DESC,"id");
//
//        List<Role> roles = new ArrayList<>(List.of(
//                new Role("Role 1", "Role 1 Description"),
//                new Role("Role 2", "Role 2 Description")));
//
//        when(roleRepositoryMock.searchAll(Mockito.any(String.class)))
//                .thenReturn(
//                       roles
//                );
//
//        Assertions.assertEquals("Role 2", roleService.searchAll("role", pageable).getContent().get(1).getName());
//    }
//
//    @Test
//    public void whenIsValidData_thenCreateSuccessfully() throws DuplicateRecordException, ResourceNotFoundException {
//        CreateRoleDTO dto = new CreateRoleDTO();
//        dto.setName("Role 1");
//        dto.setDescription("Role 1 Description");
//        dto.setPrivilegeIds(new ArrayList<>());
//
//        Role mock = new Role("Role 1", "Role Description");
//        when(roleRepositoryMock.save(Mockito.any(Role.class))).thenReturn(mock);
//
//        Role role = this.roleService.create(dto);
//
//        assertThat("result", role, is(sameInstance(mock)));
//        assertEquals("Role 1", role.getName());
//    }
//
//    @Test()
//    public void whenIsDuplicateName_thenCreateFailed()  {
//        CreateRoleDTO dto1 = new CreateRoleDTO();
//        dto1.setName("Role 1");
//        dto1.setDescription("Role 1 Description");
//
//        Role mock = new Role("Role 1", "Role 1 Description");
//        when(roleRepositoryMock.findByName(Mockito.any(String.class))).thenReturn(Optional.of(mock));
//
//        assertThrows(DuplicateRecordException.class, () -> {
//            this.roleService.create(dto1);
//        });
//    }
//
//    @Test
//    public void whenRecordExists_thenGetByIdSuccessfully() throws ResourceNotFoundException {
//        UUID id = UUID.randomUUID();
//
//        Role mock = new Role("Role 1", "Role 1 Description");
//        mock.setId(id);
//        when(roleRepositoryMock.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(mock));
//
//        Role role = this.roleService.getById(id);
//        assertThat("result", role, is(sameInstance(mock)));
//        assertEquals(mock.getId(), role.getId());
//    }
//
//    @Test
//    public void whenRecordDoesNotExist_thenGetByIdFailed() {
//        UUID id = UUID.randomUUID();
//
//        Rank mock = new Rank("Rank 1", "R1", 1);
//        mock.setId(id);
//        when(roleRepositoryMock.findById(Mockito.any(UUID.class))).thenReturn(Optional.empty());
//
//        assertThrows(ResourceNotFoundException.class, () -> {
//            this.roleService.getById(UUID.randomUUID());
//        });
//    }
//
//    @Test
//    public void whenIsValidData_thenUpdateSuccessfully() throws DuplicateRecordException, ResourceNotFoundException {
//        UUID id = UUID.randomUUID();
//
//        CreateRoleDTO dto = new CreateRoleDTO();
//        dto.setName("Role 1");
//        dto.setDescription("Role 1 Description");
//        dto.setPrivilegeIds(new ArrayList<>());
//
//        UpdateRoleDTO updateDto = new UpdateRoleDTO();
//        updateDto.setName("Role 1 Updated Name");
//        updateDto.setDescription("Role 1 Description Updated");
//
//        Role mock = new Role("Role 1", "Role 1 Description");
//        mock.setId(id);
//        when(roleRepositoryMock.save(Mockito.any(Role.class))).thenReturn(mock);
//        this.roleService.create(dto);
//
//        when(roleRepositoryMock.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(mock));
//        Role updated = this.roleService.updateById(id, updateDto);
//
//        assertThat("result", updated, is(sameInstance(mock)));
//        Assertions.assertEquals(mock.getId(), updated.getId());
//        Assertions.assertEquals(updateDto.getName(), updated.getName());
//    }
//
//
//    @Test
//    public void whenRecordDoesNotExist_thenUpdateFailed() {
//        UpdateRoleDTO updateDto = new UpdateRoleDTO();
//        updateDto.setName("Role 1");
//        updateDto.setDescription("Role 1 Description");
//
//        when(roleRepositoryMock.findById(Mockito.any(UUID.class))).thenReturn(Optional.empty());
//
//        assertThrows(ResourceNotFoundException.class, () -> {
//            this.roleService.updateById(UUID.randomUUID(), updateDto);
//        });
//    }
//
//    @Test
//    public void whenIsDuplicateName_thenUpdateFailed() {
//        UUID id = UUID.randomUUID();
//        UpdateRoleDTO updateDto = new UpdateRoleDTO();
//        updateDto.setName("Role 1");
//        updateDto.setDescription("Role 1 Description");
//
//        Role mock = new Role("Role 1", "Role Description 1");
//        mock.setId(id);
//        Role mock2 = new Role("Role 2", "Role Description 2");
//        mock2.setId(UUID.randomUUID());
//
//        when(roleRepositoryMock.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(mock));
//        when(roleRepositoryMock.findByName(Mockito.any(String.class))).thenReturn(Optional.of(mock2));
//
//        assertThrows(DuplicateRecordException.class, () -> {
//            this.roleService.updateById(id, updateDto);
//        });
//    }
//
//    @Test
//    public void whenRecordExists_thenUpdateByIdSuccessfully() throws ResourceNotFoundException {
//        UUID id = UUID.randomUUID();
//
//        Role mock = new Role("Role 1", "Role 1 Description");
//        mock.setId(id);
//         List<User> users = new ArrayList<>(List.of(
//                 new User(),
//                 new User()));
//
//         when(roleRepositoryMock.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(mock));
//
//         when(userRepositoryMock.findAllByRole(Mockito.any(Role.class))).thenReturn((users));
//        when(roleRepositoryMock.save(Mockito.any(Role.class))).thenReturn(mock);
//
//        this.roleService.deleteById(id);
//
//        Assertions.assertEquals(mock.getStatus(), EStatus.DELETED);
//    }
//
//    @Test
//    public void whenRecordExists_thenChangeStatusByIdSuccessfully() throws ResourceNotFoundException {
//        UUID id = UUID.randomUUID();
//
//        Role mock = new Role("Role 1", "Role 1 Description");
//        mock.setId(id);
//
//        when(roleRepositoryMock.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(mock));
//        when(roleRepositoryMock.save(Mockito.any(Role.class))).thenReturn(mock);
//
//        this.roleService.changeStatusById(id, EStatus.INACTIVE);
//
//        Assertions.assertEquals(mock.getStatus(), EStatus.INACTIVE);
//
//    }
//
//    @Test
//    public void whenRecordExists_thenDeleteByIdSuccessfully() throws ResourceNotFoundException {
//        UUID id = UUID.randomUUID();
//
//        Role mock = new Role("Role 1", "Role 1 Description");
//        mock.setId(id);
//        when(roleRepositoryMock.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(mock));
//        when(userRepositoryMock.findAllByRole(Mockito.any(Role.class))).thenReturn(new ArrayList<>());
//        doNothing().when(roleRepositoryMock).deleteById(Mockito.any(UUID.class));
//       this.roleService.deleteById(id);
//
//        verify(roleRepositoryMock, times(1)).deleteById(id);
//    }
//
//    @Test
//    public void whenRecordDoesNotExist_thenDeleteByIdFailed() {
//        UUID id = UUID.randomUUID();
//
//        when(roleRepositoryMock.findById(Mockito.any(UUID.class))).thenReturn(Optional.empty());
//
//        assertThrows(ResourceNotFoundException.class, () -> {
//            this.roleService.deleteById(id);
//        });
//    }
//
//}
//
