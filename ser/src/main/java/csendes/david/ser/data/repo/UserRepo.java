package csendes.david.ser.data.repo;

import csendes.david.ser.data.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo
        extends JpaRepository<UserEntity, Long> {

    UserEntity findByUsername(String username);
}
