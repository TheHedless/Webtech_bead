package csendes.david.ser.controller;

import csendes.david.ser.data.model.PrivilageEntity;
import csendes.david.ser.data.model.UserEntity;
import csendes.david.ser.data.repo.PrivilageRepo;
import csendes.david.ser.data.repo.UserRepo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/felhasznalo")
public class UserController {

    final UserRepo userRepo;
    final PrivilageRepo privilageRepo;

    public UserController(UserRepo userRepo, PrivilageRepo privilageRepo) {
        this.userRepo = userRepo;
        this.privilageRepo = privilageRepo;
    }

    @PostMapping("/grant-admin")
    public UserEntity grantAdmin(@RequestParam String username) {
        UserEntity user = userRepo.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        PrivilageEntity adminRole = privilageRepo.findByName("ADMIN");
        if (adminRole == null) {
            adminRole = new PrivilageEntity();
            adminRole.setName("ADMIN");
            adminRole = privilageRepo.save(adminRole);
        }

        List<PrivilageEntity> privilages = user.getPrivilages();
        if (privilages == null) {
            privilages = new ArrayList<>();
        }

        if (!privilages.contains(adminRole)) {
            privilages.add(adminRole);
            user.setPrivilages(privilages);
            user = userRepo.save(user);
        }

        return user;
    }

    @PostMapping("/revoke-admin")
    public UserEntity revokeAdmin(@RequestParam String username) {
        UserEntity user = userRepo.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        PrivilageEntity adminRole = privilageRepo.findByName("ADMIN");
        if (adminRole != null) {
            List<PrivilageEntity> privilages = user.getPrivilages();
            if (privilages != null) {
                privilages.remove(adminRole);
                user.setPrivilages(privilages);
                user = userRepo.save(user);
            }
        }

        return user;
    }
}
