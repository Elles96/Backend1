package org.oskars.Pasakums.controller;

import org.oskars.Pasakums.entity.Lietotajs;
import org.oskars.Pasakums.service.LietotajsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Step 1: Tell Spring this handles HTTP requests and returns JSON
@RestController
// Step 2: All endpoints start with /api/lietotaji
@RequestMapping("/api/lietotaji")
// Step 3: Allow requests from any website (for frontend integration)
@CrossOrigin(origins = "*")
public class LietotajsController {

    // Step 4: Inject service layer (business logic)
    private final LietotajsService service;

    // Step 5: Constructor injection (modern way)
    public LietotajsController(LietotajsService service) {
        this.service = service;
    }

    // Step 6: GET /api/lietotaji - Return all users as JSON
    @GetMapping
    public List<Lietotajs> getAll() {
        return service.getAllLietotaji();
    }

    // Step 8: POST /api/lietotaji/register - Register new user (BEFORE /{id})
    @PostMapping("/register")
    public ResponseEntity<Long> register(@RequestBody Lietotajs lietotajs) {
        try {
            // Step 8a: Create user and get just the ID (avoids Record setter issue)
            Long userId = service.createLietotajs(lietotajs);
            // Step 8b: Return user ID directly
            return ResponseEntity.ok(userId);
        } catch (Exception e) {
            // Step 8c: Return error with 400 status
            return ResponseEntity.badRequest().build();
        }
    }

    // Step 9: POST /api/lietotaji/login - User login (BEFORE /{id})
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest req) {
        // Step 9a: Use ternary operator for compact if-else
        return service.authenticate(req.lietotajvards(), req.parole())
                ? ResponseEntity.ok("Pieteikšanās veiksmīga") // If login OK
                : ResponseEntity.status(401).body("Nepareizi pieteikšanās dati"); // If login failed
    }

    // Step 10: GET /api/lietotaji/check/{username} - Check if username exists
    // (BEFORE /{id})
    @GetMapping("/check/{username}")
    public boolean checkUsername(@PathVariable String username) {
        return service.existsByLietotajvards(username);
    }

    // Step 7: GET /api/lietotaji/{id} - Return one user by ID (AFTER specific
    // paths)
    @GetMapping("/{id}")
    public ResponseEntity<Lietotajs> getById(@PathVariable Long id) {
        // If found -> 200 OK + user data, if not found -> 404 Not Found
        return service.getLietotajsById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Step 11: DELETE /api/lietotaji/{id} - Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        // If user exists -> delete and return 204 No Content, else -> 404 Not Found
        return service.getLietotajsById(id).isPresent() ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    // Step 12: Simple record for login JSON data {"lietotajvards": "...", "parole":
    // "..."}
    record LoginRequest(String lietotajvards, String parole) {
    }
}