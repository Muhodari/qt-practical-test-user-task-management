//package rw.tekana.v2.userms.repositories;
//
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.test.context.junit4.SpringRunner;
//import rw.tekana.v2.userms.models.Rank;
//import rw.tekana.v2.userms.models.Role;
//import rw.tekana.v2.userms.models.RolePrivilege;
//import rw.tekana.v2.userms.models.User;
//import rw.tekana.v2.userms.models.enums.EUserStatus;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@RunWith(SpringRunner.class)
//@DataJpaTest
//public class UserRepositoryTest {
//    @Autowired
//    private IUserRepository userRepository;
//
//    @Autowired
//    private IRoleRepository roleRepository;
//
//    @Autowired
//    private IRankRepository rankRepository;
//
//    @Test
//    public void itShouldFindAllByStatus() {
//        Role role = this.roleRepository.save(new Role("Role 1", "Role 1 Description"));
//        Rank rank = this.rankRepository.save(new Rank("Rank 1", "R1", 1));
//
//        List<Role> roles = new ArrayList<>();
//            roles.add(role);
//
//        Pageable pageable = (Pageable) PageRequest.of(0, 10, Sort.Direction.DESC,"id");
//
//        this.userRepository.save(new User(UUID.randomUUID(), "user1@rnp.rw", UUID.randomUUID(), "250784562320", "HBJY47@nfs8", rank, new HashSet<>(roles), EUserStatus.ACTIVE));
//        this.userRepository.save(new User(UUID.randomUUID(), "user2@rnp.rw", UUID.randomUUID(), "25078456232", "HBJY47@nfs8", rank, new HashSet<>(roles), EUserStatus.ACTIVE));
//        this.userRepository.save(new User(UUID.randomUUID(), "user3@rnp.rw", UUID.randomUUID(), "0784562321", "HBJY47@nfs8", rank, new HashSet<>(roles), EUserStatus.PENDING));
//
//        Page<User> users = this.userRepository.findAllByStatus(EUserStatus.ACTIVE, pageable);
//
//        assertEquals(2, users.getTotalElements());
//    }
//
//    @Test
//    public void itShouldFindAllByRank() {
//        Role role = this.roleRepository.save(new Role("Role 2", "Role 2 Description"));
//        Rank rank1 = this.rankRepository.save(new Rank("Rank 2", "R3", 2));
//        Rank rank2 = this.rankRepository.save(new Rank("Rank 3", "R4", 3));
//
//        List<Role> roles = new ArrayList<>();
//        roles.add(role);
//
//        Pageable pageable = (Pageable) PageRequest.of(0, 10, Sort.Direction.DESC,"id");
//
//        this.userRepository.save(new User(UUID.randomUUID(), "user4@rnp.rw", UUID.randomUUID(), "0784562323", "HBJY47@nfs8", rank1, new HashSet<>(roles)));
//        this.userRepository.save(new User(UUID.randomUUID(), "user5@rnp.rw", UUID.randomUUID(), "0784562324", "HBJY47@nfs8", rank2, new HashSet<>(roles)));
//        this.userRepository.save(new User(UUID.randomUUID(), "user6@rnp.rw", UUID.randomUUID(), "0784562325", "HBJY47@nfs8", rank2, new HashSet<>(roles)));
//
//        Page<User> users = this.userRepository.findAllByRankAndStatusNot(rank2, EUserStatus.DELETED, pageable);
//
//        assertEquals(2, users.getTotalElements());
//    }
//
//    @Test
//    public void itShouldFindAllByRole() {
//        Role role1 = this.roleRepository.save(new Role("Role 3", "Role 3 Description"));
//        Role role2 = this.roleRepository.save(new Role("Role 4", "Role 4 Description"));
//        Rank rank1 = this.rankRepository.save(new Rank("Rank 3", "R4", 3));
//        Rank rank2 = this.rankRepository.save(new Rank("Rank 4", "R5", 4));
//
//        List<Role> roles1 = new ArrayList<>();
//        roles1.add(role1);
//
//        List<Role> roles2 = new ArrayList<>();
//        roles2.add(role2);
//
//
//        this.userRepository.save(new User(UUID.randomUUID(), "user7@rnp.rw", UUID.randomUUID(), "0784562326", "HBJY47@nfs8", rank1, new HashSet<>(roles1)));
//        this.userRepository.save(new User(UUID.randomUUID(), "user8@rnp.rw", UUID.randomUUID(), "0784562327", "HBJY47@nfs8", rank2, new HashSet<>(roles2)));
//        this.userRepository.save(new User(UUID.randomUUID(), "user9@rnp.rw", UUID.randomUUID(), "0784562328", "HBJY47@nfs8", rank2, new HashSet<>(roles2)));
//
//        List<User> users = this.userRepository.findAllByRole(role2);
//
//        assertEquals("user8@rnp.rw", users.get(0).getEmailAddress());
//        assertEquals(2, users.size());
//    }
//
//    @Test
//    public void itShouldFindByPhoneNumber() {
//        String phoneNumber = "0784562329";
//        Role role = this.roleRepository.save(new Role("Role 6", "Role 6 Description"));
//        Rank rank = this.rankRepository.save(new Rank("Rank 5", "R4", 6));
//
//        List<Role> roles = new ArrayList<>();
//        roles.add(role);
//
//        this.userRepository.save(new User(UUID.randomUUID(), "user9@rnp.rw", UUID.randomUUID(), phoneNumber, "HBJY47@nfs8", rank, new HashSet<>(roles)));
//
//        Optional<User> user1 = this.userRepository.findByPhoneNumber(phoneNumber);
//        Optional<User> user2 = this.userRepository.findByPhoneNumber("0724562329");
//
//        assertTrue(user1.isPresent());
//        assertEquals(phoneNumber, user1.get().getPhoneNumber());
//        assertTrue(user2.isEmpty());
//    }
//
//    @Test
//    public void itShouldFindByEmailAddress() {
//        String emailAddress = "user10@rnp.rw";
//        Role role = this.roleRepository.save(new Role("Role 7", "Role 7 Description"));
//        Rank rank = this.rankRepository.save(new Rank("Rank 6", "R6", 6));
//
//        List<Role> roles = new ArrayList<>();
//        roles.add(role);
//
//        this.userRepository.save(new User(UUID.randomUUID(), emailAddress, UUID.randomUUID(), "0784562319", "HBJY47@nfs8", rank, new HashSet<>(roles)));
//
//        Optional<User> user1 = this.userRepository.findByEmailAddress(emailAddress);
//        Optional<User> user2 = this.userRepository.findByEmailAddress("user214@rnp.rw");
//
//        assertTrue(user1.isPresent());
//        assertEquals(emailAddress, user1.get().getEmailAddress());
//        assertTrue(user2.isEmpty());
//    }
//
//
//    @Test
//    public void itShouldFindByCitizenId() {
//        UUID citizenId = UUID.randomUUID();
//        Role role = this.roleRepository.save(new Role("Role 8", "Role 8 Description"));
//        Rank rank = this.rankRepository.save(new Rank("Rank 9", "R9", 7));
//
//        List<Role> roles = new ArrayList<>();
//        roles.add(role);
//
//        this.userRepository.save(new User(citizenId, "user13@rnp.rw", UUID.randomUUID(), "0784562329", "HBJY47@nfs8", rank, new HashSet<>(roles)));
//
//        Optional<User> user1 = this.userRepository.findByCitizenId(citizenId);
//        Optional<User> user2 = this.userRepository.findByCitizenId(UUID.randomUUID());
//
//        assertTrue(user1.isPresent());
//        assertEquals(citizenId.toString(), user1.get().getCitizenId().toString());
//        assertTrue(user2.isEmpty());
//    }
//
//}
