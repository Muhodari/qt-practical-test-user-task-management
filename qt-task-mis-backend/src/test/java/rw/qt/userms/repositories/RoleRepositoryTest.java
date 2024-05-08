//package rw.tekana.v2.userms.repositories;
//
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import rw.tekana.v2.userms.models.Role;
//import rw.tekana.v2.userms.models.enums.EStatus;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@RunWith(SpringRunner.class)
//@DataJpaTest
//public class RoleRepositoryTest {
//    @Autowired
//    private IRoleRepository roleRepository;
//
//    @Test
//    public void itShouldFindAllByStatus() {
//
//        this.roleRepository.save(new Role("Role 1", "Role 1 Description", EStatus.INACTIVE));
//        this.roleRepository.save(new Role("Role 2", "Role 2 Description", EStatus.ACTIVE));
//        this.roleRepository.save(new Role("Role 3", "Role 3 Description", EStatus.INACTIVE));
//
//        List<Role> inactiveRoles = this.roleRepository.findAllByStatus(EStatus.INACTIVE);
//
//        assertEquals(2, inactiveRoles.size());
//    }
//
//
//    @Test
//    public void itShouldSearchAll() {
//
//        this.roleRepository.save(new Role("Role 4", "Role 4 Description"));
//        this.roleRepository.save(new Role("Role 5", "Role 5 Description"));
//        this.roleRepository.save(new Role("Role 6", "Role 6 Description"));
//        this.roleRepository.save(new Role("Role 7", "Role 7 Description"));
//
//        List<Role> searchResults = this.roleRepository.searchAll("Role 6");
//
//        assertEquals(1, searchResults.size());
//    }
//
//    @Test
//    public void itShouldFindByName() {
//        String name = "Role 8";
//
//        this.roleRepository.save(new Role(name, "Role 8 Description"));
//
//        Optional<Role> role1 = this.roleRepository.findByName(name);
//        Optional<Role> role2 = this.roleRepository.findByName("Role 9");
//
//
//        assertTrue(role1.isPresent());
//        assertEquals(name, role1.get().getName());
//        assertTrue(role2.isEmpty());
//    }
//
//}
