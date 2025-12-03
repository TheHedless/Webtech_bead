package csendes.david.ser.data.repo;

import csendes.david.ser.data.model.SerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SerRepo
        extends JpaRepository<SerEntity, Long> {

    SerEntity getByName(String name);
}
