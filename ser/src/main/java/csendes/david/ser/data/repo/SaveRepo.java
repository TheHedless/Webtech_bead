package csendes.david.ser.data.repo;

import csendes.david.ser.data.model.SaveEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaveRepo
        extends JpaRepository<SaveEntity, Long> {
}
