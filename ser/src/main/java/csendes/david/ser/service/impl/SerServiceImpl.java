package csendes.david.ser.service.impl;

import csendes.david.ser.data.model.SerEntity;
import csendes.david.ser.data.repo.SerRepo;
import csendes.david.ser.service.SerService;
import csendes.david.ser.service.dto.SerDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SerServiceImpl
        implements SerService {

    final SerRepo repo;
    final ModelMapper mapper;
    private final ModelMapper modelMapper;

    SerServiceImpl(SerRepo repo
            , ModelMapper mapper, ModelMapper modelMapper) {
        this.repo = repo;
        this.mapper = mapper;
        this.modelMapper = modelMapper;
    }

    @Override
    public SerDto save(SerDto dto) {
        SerEntity entity  = modelMapper
                .map(dto, SerEntity.class);

        entity = repo.save(entity);

        dto = mapper.map(entity, SerDto.class);

        return dto;
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
    public void deleteById(Long id) {
        repo.deleteById(id);
    }


}
