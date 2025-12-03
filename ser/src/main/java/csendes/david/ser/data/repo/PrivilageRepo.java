package csendes.david.ser.data.repo;

import csendes.david.ser.data.model.PrivilageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilageRepo
        extends JpaRepository<PrivilageEntity,Long> {

    PrivilageEntity findByName(String name);
}
