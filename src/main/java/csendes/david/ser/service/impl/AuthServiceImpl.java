package csendes.david.ser.service.impl;

import csendes.david.ser.data.model.UserEntity;
import csendes.david.ser.data.model.PrivilageEntity;
import csendes.david.ser.data.repo.UserRepo;
import csendes.david.ser.data.repo.PrivilageRepo;
import csendes.david.ser.service.AuthService;
import csendes.david.ser.service.TokenService;
import csendes.david.ser.service.dto.LoginDto;
import csendes.david.ser.service.dto.RegisterDto;
import csendes.david.ser.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepo userRepo;
    private final PrivilageRepo privilageRepo;
    private final UserMapper userMapper;
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;


    @Override
    public void register(RegisterDto dto) {
        UserEntity e = userMapper.registrationToEntity(dto);
        e.setPassword(passwordEncoder.encode(e.getPassword()));
        PrivilageEntity jog = privilageRepo.findByName("USER");
        if(jog != null){
            e.setPrivilages(List.of(jog));
        } else {
            jog = new PrivilageEntity();
            jog.setName("USER");
            jog = privilageRepo.save(jog);

            e.setPrivilages(List.of(jog));
        }
        e = userRepo.save(e);
    }

    @Override
    public String login(LoginDto dto) {
        SecurityContext context =
                SecurityContextHolder.createEmptyContext();
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getUsername(),
                        dto.getPassword()
                )
        );
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        UserDetails user = userRepo
                .findByUsername(dto.getUsername());
        return tokenService.generateToken(user);

    }
}
