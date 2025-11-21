package org.oskars.Pasakums.controller;

import org.oskars.Pasakums.entity.Lietotajs;
import org.oskars.Pasakums.service.LietotajsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/api/lietotaji")

@CrossOrigin(origins = "*")
public class LietotajsController {

    private final LietotajsService service;

    public LietotajsController(LietotajsService service) {
        this.service = service;
    }

    @GetMapping
    public List<Lietotajs> getAll() {
        return service.getAllLietotaji();
    }

    @PostMapping("/register")
    public ResponseEntity<Long> register(@RequestBody Lietotajs lietotajs) {
        try {
            Long userId = service.createLietotajs(lietotajs);
            return ResponseEntity.ok(userId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest req) {
        return service.authenticate(req.lietotajvards(), req.parole())
                ? ResponseEntity.ok("Pieteikšanās veiksmīga")
                : ResponseEntity.status(401).body("Nepareizi pieteikšanās dati");
    }

    @GetMapping("/check/{username}")
    public boolean checkUsername(@PathVariable String username) {
        return service.existsByLietotajvards(username);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lietotajs> getById(@PathVariable Long id) {
        Lietotajs lietotajs = service.getLietotajsById(id);
        if (lietotajs != null) {
            return ResponseEntity.ok(lietotajs);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Lietotajs lietotajs = service.getLietotajsById(id);
        if (lietotajs != null) {
            service.deleteLietotajs(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    record LoginRequest(String lietotajvards, String parole) {
    }
}