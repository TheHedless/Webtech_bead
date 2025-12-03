package csendes.david.ser.controller;

import csendes.david.ser.service.AuthService;
import csendes.david.ser.service.dto.LoginDto;
import csendes.david.ser.service.dto.RegisterDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@Validated
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public void register(@RequestBody @Valid RegisterDto dto) {
        authService.register(dto);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDto dto) {
        return authService.login(dto);
    }

    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> options(){
        return ResponseEntity.ok().build();
    }
}
