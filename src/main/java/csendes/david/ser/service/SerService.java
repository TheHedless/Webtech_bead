package csendes.david.ser.service;

import csendes.david.ser.service.dto.SerDto;

import java.util.List;

public interface SerService {
    SerDto getById(Long id);
    SerDto getByName(String name);
    List<SerDto> getAll();
    void deleteById(Long id);
    SerDto save(SerDto dto);
    boolean exists(SerDto dto);
    List<SerDto> getAllByUser(String username);
    boolean deleteByIdAndUser(Long id, String username);
}
