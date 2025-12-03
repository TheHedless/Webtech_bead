package csendes.david.ser.controller;

import csendes.david.ser.service.SerService;
import csendes.david.ser.service.dto.SerDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/ser")
public class SerController {

    final SerService service;

    public SerController(SerService service){
        this.service = service;
    }

    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : null;
    }

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public Object save(@RequestBody SerDto dto){
        if (service.exists(dto)) {
            return new java.util.HashMap<String, String>() {{
                put("message", "Beer already in database");
            }};
        }
        return service.save(dto);
    }

    @GetMapping("/byId")
    @PreAuthorize("hasAuthority('ADMIN')")
    public SerDto getById(@RequestParam Long id){
        return service.getById(id);
    }

    @GetMapping("/byName/{name}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public Object getByName(@PathVariable String name){
        SerDto result = service.getByName(name);
        if (result == null) {
            return new java.util.HashMap<String, String>() {{
                put("message", "There is no beer by that name in the database");
            }};
        }
        return result;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public java.util.List<SerDto> getAll(@RequestParam(required = false, defaultValue = "10") int limit) {
        java.util.List<SerDto> all = service.getAll();
        if (all == null) return java.util.Collections.emptyList();
        return all.subList(0, Math.min(limit, all.size()));
    }

    @GetMapping("/byUser")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public java.util.List<SerDto> getAllByUser(@RequestParam(required = false, defaultValue = "10") int limit) {
        String username = getCurrentUsername();
        java.util.List<SerDto> userBeers = service.getAllByUser(username);
        if (userBeers == null) return java.util.Collections.emptyList();
        return userBeers.subList(0, Math.min(limit, userBeers.size()));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Object deleteById(@RequestParam Long id) {
        service.deleteById(id);
        return new java.util.HashMap<String, String>() {{
            put("message", "Beer deleted successfully");
        }};
    }

    @DeleteMapping("/deleteByUser")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public Object deleteByUser(@RequestParam Long id) {
        String username = getCurrentUsername();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED)
                    .body(java.util.Map.of("message", "Authentication required"));
        }

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> {
                    String role = a.getAuthority();
                    return role != null && role.equalsIgnoreCase("ADMIN");
                });
        boolean deleted = false;
        if (isAdmin){
            service.deleteById(id);
        }
        else{
            if (service.deleteByIdAndUser(id, username)) {
                return new java.util.HashMap<String, String>() {{
                    put("message", "You can only delete beers you registered");
                }};
            }
        }
        return new java.util.HashMap<String, String>() {{
            put("message", "Beer deleted successfully");
        }};
    }
}
