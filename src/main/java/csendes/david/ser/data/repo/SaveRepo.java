package csendes.david.ser.data.repo;

import csendes.david.ser.data.model.SaveEntity;
import csendes.david.ser.data.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaveRepo extends JpaRepository<SaveEntity, Long> {
    List<SaveEntity> findByUser(UserEntity user);
    SaveEntity findByUserAndSer_Id(UserEntity user, Long serId);

}
