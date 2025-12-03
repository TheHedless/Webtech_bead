package csendes.david.ser.service.impl;

import csendes.david.ser.data.model.SaveEntity;
import csendes.david.ser.data.model.SerEntity;
import csendes.david.ser.data.model.UserEntity;
import csendes.david.ser.data.repo.SaveRepo;
import csendes.david.ser.data.repo.SerRepo;
import csendes.david.ser.data.repo.UserRepo;
import csendes.david.ser.service.SerService;
import csendes.david.ser.service.dto.SerDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class SerServiceImpl implements SerService {

    final SerRepo repo;
    final SaveRepo saveRepo;
    final UserRepo userRepo;
    final ModelMapper mapper;
    private final ModelMapper modelMapper;

    SerServiceImpl(SerRepo repo, SaveRepo saveRepo, UserRepo userRepo,
            ModelMapper mapper, ModelMapper modelMapper) {
        this.repo = repo;
        this.saveRepo = saveRepo;
        this.userRepo = userRepo;
        this.mapper = mapper;
        this.modelMapper = modelMapper;
    }

    @Override
    public SerDto save(SerDto dto) {
        SerEntity entity = modelMapper.map(dto, SerEntity.class);
        entity = repo.save(entity);
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            String username = auth.getName();
            UserEntity user = userRepo.findByUsername(username);
            if (user != null) {
                SaveEntity save = new SaveEntity();
                save.setUser(user);
                save.setSer(entity);
                saveRepo.save(save);
            }
        }

        return mapper.map(entity, SerDto.class);
    }

    @Override
    public boolean exists(SerDto dto) {
        List<SerEntity> all = repo.findAll();
        return all.stream().anyMatch(entity ->
                entity.getName().equals(dto.getName()) &&
                entity.getManufacturer().equals(dto.getManufacturer()) &&
                entity.getSize().equals(dto.getSize()) &&
                entity.getType().equals(dto.getType())
        );
    }

    @Override
    public SerDto getById(Long id) {
        SerEntity entity = repo.getReferenceById(id);
        return mapper.map(entity, SerDto.class);
    }

    @Override
    public SerDto getByName(String name) {
        List<SerEntity> sers = repo.findAll();
        for (SerEntity entity : sers) {
            if (entity.getName().equals(name)) {
                return mapper.map(entity, SerDto.class);
            }
        }
        return null;
    }

    @Override
    public List<SerDto> getAll() {
        List<SerEntity> entities = repo.findAll();
        return entities.stream()
                .map(entity -> mapper.map(entity, SerDto.class))
                .toList();
    }

    @Override
    public List<SerDto> getAllByUser(String username) {
        UserEntity user = userRepo.findByUsername(username);
        if (user == null) return List.of();

        List<SaveEntity> saves = saveRepo.findByUser(user);
        return saves.stream()
                .map(save -> mapper.map(save.getSer(), SerDto.class))
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    @Override
    public boolean deleteByIdAndUser(Long id, String username) {
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> {
                        String role = a.getAuthority();
                        return role != null && role.equalsIgnoreCase("ADMIN");
                    });

            if (isAdmin) {
                saveRepo.deleteById(id);
            }
            else {
                UserEntity user = userRepo.findByUsername(username);
                if (user == null) return false;

                SaveEntity save = saveRepo.findByUserAndSer_Id(user, id);
                if (save == null) return false;

                saveRepo.deleteById(save.getId());
            }

            repo.deleteById(id);
        }


        return true;
    }
}
