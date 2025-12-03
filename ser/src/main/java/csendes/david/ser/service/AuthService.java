package csendes.david.ser.service;

import csendes.david.ser.service.dto.LoginDto;
import csendes.david.ser.service.dto.RegisterDto;

public interface AuthService {

    void register(RegisterDto dto);
    String login(LoginDto dto);
}
