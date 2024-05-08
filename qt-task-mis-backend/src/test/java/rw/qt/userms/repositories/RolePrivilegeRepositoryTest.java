//package rw.tekana.v2.userms.repositories;
//
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import rw.tekana.v2.userms.models.Role;
//import rw.tekana.v2.userms.models.RolePrivilege;
//import rw.tekana.v2.userms.models.enums.EStatus;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@RunWith(SpringRunner.class)
//@DataJpaTest
//public class RolePrivilegeRepositoryTest {
//    @Autowired
//    private IRolePrivilegeRepository rolePrivilegeRepository;
//
//    @Autowired
//    private IRoleRepository roleRepository;
//
//    @Test
//    public void itShouldFindByRoleAndPrivilegeId() {
//        Role role1 = this.roleRepository.save(new Role("Role 1", "Role 1 Description"));
//        Role role2 = this.roleRepository.save(new Role("Role 2", "Role 2 Description"));
//
//        UUID privilege1 = UUID.randomUUID();
//        UUID privilege2 = UUID.randomUUID();
//
//        this.rolePrivilegeRepository.save(new RolePrivilege(role1, privilege1));
//        this.rolePrivilegeRepository.save(new RolePrivilege(role1, privilege2));
//        this.rolePrivilegeRepository.save(new RolePrivilege(role2, privilege2));
//
//        Optional<RolePrivilege> optionalRolePrivilege1 = this.rolePrivilegeRepository.findByRoleAndPrivilegeId(role1, privilege1);
//        Optional<RolePrivilege> optionalRolePrivilege2 = this.rolePrivilegeRepository.findByRoleAndPrivilegeId(role1, UUID.randomUUID());
//
//        assertTrue(optionalRolePrivilege1.isPresent());
//        assertTrue(optionalRolePrivilege2.isEmpty());
//    }
//
//}
