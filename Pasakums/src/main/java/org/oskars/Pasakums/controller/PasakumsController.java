package org.oskars.Pasakums.controller;

import org.oskars.Pasakums.entity.Pasakums;
import org.oskars.Pasakums.service.PasakumsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

// Step 1: Tell Spring this handles HTTP requests and returns JSON
@RestController
// Step 2: All endpoints start with /api/pasakumi
@RequestMapping("/api/pasakumi")
// Step 3: Allow requests from any website (for frontend integration)
@CrossOrigin(origins = "*")
public class PasakumsController {

    // Step 4: Inject service layer (business logic)
    private final PasakumsService service;

    // Step 5: Constructor injection (modern way)
    public PasakumsController(PasakumsService service) {
        this.service = service;
    }

    // Step 6: GET /api/pasakumi - Return all events as JSON
    @GetMapping
    public List<Pasakums> getAll() {
        return service.getAllPasakumi();
    }

    // Step 7: GET /api/pasakumi/{id} - Return one event by ID
    @GetMapping("/{id}")
    public ResponseEntity<Pasakums> getById(@PathVariable Long id) {
        // If found -> 200 OK + event data, if not found -> 404 Not Found
        return service.getPasakumsById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Step 8: POST /api/pasakumi - Create new event
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody Pasakums pasakums) {
        try {
            // Return 200 OK + created event ID
            Long eventId = service.createPasakums(pasakums);
            return ResponseEntity.ok(eventId);
        } catch (Exception e) {
            // Return 400 Bad Request if validation fails
            return ResponseEntity.badRequest().build();
        }
    }

    // Step 9: PUT /api/pasakumi/{id} - Update existing event
    @PutMapping("/{id}")
    public ResponseEntity<Pasakums> update(@PathVariable Long id, @RequestBody Pasakums pasakums) {
        // If event exists -> update and return, else -> 404 Not Found
        return service.getPasakumsById(id).isPresent()
                ? ResponseEntity.ok(service.updatePasakums(pasakums))
                : ResponseEntity.notFound().build();
    }

    // Step 10: DELETE /api/pasakumi/{id} - Delete event
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        // If event exists -> 204 No Content, else -> 404 Not Found
        return service.getPasakumsById(id).isPresent()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/search/nosaukums")
    public List<Pasakums> searchByName(@RequestParam String q) {
        // Step 11: Search events by name (partial match)
        return service.searchByNosaukums(q);
    }

    @GetMapping("/search/vieta")
    public List<Pasakums> searchByLocation(@RequestParam String q) {
        // Step 12: Search events by location (partial match)
        return service.searchByVieta(q);
    }

    @GetMapping("/date/{date}")
    public List<Pasakums> getByDate(@PathVariable LocalDate date) {
        // Step 13: Get events by specific date
        return service.getPasakumiByDate(date);
    }

    @PostMapping("/{id}/register")
    public ResponseEntity<String> register(@PathVariable Long id) {
        // Step 14: Register for event (with business logic validation)
        return service.registerForEvent(id)
                ? ResponseEntity.ok("Veiksmīgi piereģistrējāties pasākumam")
                : ResponseEntity.badRequest().body("Reģistrācija neizdevās - pasākums var būt pilns vai nav atrasts");
    }
}