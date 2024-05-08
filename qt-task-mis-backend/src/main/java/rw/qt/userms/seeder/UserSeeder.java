package rw.qt.userms.seeder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rw.qt.userms.models.*;
import rw.qt.userms.models.dtos.CreateUserDTO;
import rw.qt.userms.models.enums.EGender;
import rw.qt.userms.repositories.*;


import java.time.LocalDateTime;
import java.time.ZoneId;
@Configuration
public class UserSeeder {
    @Bean
    CommandLineRunner commandLineRunner(
                                        IUserRepository userRepo


                                        ) {
        return args -> {

            if(userRepo.findByEmailAddress("admin@qt.rw").isEmpty()){

                CreateUserDTO userDto = new CreateUserDTO("QT", " Admin", EGender.MALE,"admin@qt.rw", "+250787161515","");

                UserAccount userAccount = new UserAccount(userDto);

                userAccount.setPassword("$2a$12$N9hr.Cw4ySeAxcVdTlmzF.nAFq41zST5YJRUhDs/N0Qcc4nxdGwUu");
                userAccount.setCredentialsExpired(false);
                userAccount.setCredentialsExpiryDate(LocalDateTime.now(ZoneId.of("Africa/Kigali")).plusMonths(12).toString());
                userAccount.setAccountEnabled(true);
                userAccount.setAccountExpired(false);
                userAccount.setAccountLocked(false);
                userAccount.setDeletedFlag(false);

                userRepo.save(userAccount);
            }

        };
    }
}