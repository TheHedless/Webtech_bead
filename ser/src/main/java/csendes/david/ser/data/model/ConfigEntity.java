package csendes.david.ser.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "configuration")
public class ConfigEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "configuration_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> user;

    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "configuration_id"),
            inverseJoinColumns = @JoinColumn(name = "ser_id")
    )
    private List<SerEntity> ser;
}
