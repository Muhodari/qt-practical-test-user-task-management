//package rw.tekana.v2.userms.repositories;
//
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import rw.tekana.v2.userms.models.Rank;
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
//public class RankRepositoryTest {
//    @Autowired
//    private IRankRepository rankRepository;
//
//    @Test
//    public void itShouldFindAllByStatus() {
//
//        this.rankRepository.save(new Rank("Rank 1", "R1", 1, EStatus.INACTIVE));
//        this.rankRepository.save(new Rank("Rank 2", "R2", 2, EStatus.INACTIVE));
//        this.rankRepository.save(new Rank("Rank 3", "R3", 3, EStatus.ACTIVE));
//        this.rankRepository.save(new Rank("Rank 4", "R4", 4, EStatus.ACTIVE));
//
//        List<Rank> inactiveRanks = this.rankRepository.findAllByStatusOrderByPriorityAsc(EStatus.INACTIVE);
//
//
//        assertEquals(2, inactiveRanks.size());
//        assertEquals("R1", inactiveRanks.get(0).getAcronym());
//        assertEquals(EStatus.INACTIVE, inactiveRanks.get(0).getStatus());
//    }
//
//
//    @Test
//    public void itShouldSearchAll() {
//
//        this.rankRepository.save(new Rank("Rank 5", "R5", 1));
//        this.rankRepository.save(new Rank("Rank 6", "R6", 2));
//        this.rankRepository.save(new Rank("Rank 7", "R7", 3));
//        this.rankRepository.save(new Rank("Rank 8", "R8", 4));
//
//        List<Rank> search2 = this.rankRepository.searchAll("R5");
//
//
//        assertEquals(1, search2.size());
//        assertEquals("R5", search2.get(0).getAcronym());
//    }
//
//    @Test
//    public void itShouldFindByName() {
//        String name = "Rank 8";
//
//        this.rankRepository.save(new Rank(name, "R8", 8));
//
//        Optional<Rank> rank1 = this.rankRepository.findByName(name);
//        Optional<Rank> rank2 = this.rankRepository.findByName("Rank 9");
//
//        assertTrue(rank1.isPresent());
//        assertEquals(name, rank1.get().getName());
//        assertTrue(rank2.isEmpty());
//    }
//
//    @Test
//    public void itShouldFindByAcronym() {
//        String acronym = "R9";
//
//        this.rankRepository.save(new Rank("Rank 9", acronym, 9));
//
//        Optional<Rank> rank1 = this.rankRepository.findByAcronym(acronym);
//        Optional<Rank> rank2 = this.rankRepository.findByAcronym("R14");
//
//        assertTrue(rank1.isPresent());
//        assertEquals(acronym, rank1.get().getAcronym());
//        assertTrue(rank2.isEmpty());
//    }
//
//    @Test
//    public void itShouldFindByPriority() {
//        Integer priority = 10;
//
//        this.rankRepository.save(new Rank("Rank 10", "R10", priority));
//
//        Optional<Rank> rank1 = this.rankRepository.findByPriority(priority);
//        Optional<Rank> rank2 = this.rankRepository.findByPriority(11);
//
//        assertTrue(rank1.isPresent());
//        assertEquals(priority, rank1.get().getPriority());
//        assertTrue(rank2.isEmpty());
//    }
//}
