package rw.qt.userms.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.qt.userms.models.UserAccount;
import rw.qt.userms.models.enums.EUserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserRepository extends JpaRepository<UserAccount, UUID> {

    Page<UserAccount> findAllByStatusNot(EUserStatus status, Pageable pageable);
    Optional<UserAccount> findByEmailAddress(String emailAddress);
    Optional<UserAccount> findByPhoneNumber(String phoneNumber);

}
