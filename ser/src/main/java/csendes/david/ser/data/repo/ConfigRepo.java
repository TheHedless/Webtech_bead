package csendes.david.ser.data.repo;

import csendes.david.ser.data.model.ConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigRepo
        extends JpaRepository<ConfigEntity, Long> {
}
