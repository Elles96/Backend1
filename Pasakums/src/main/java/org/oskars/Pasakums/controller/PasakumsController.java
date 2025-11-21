package org.oskars.Pasakums.controller;

import org.oskars.Pasakums.entity.Pasakums;
import org.oskars.Pasakums.service.PasakumsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/pasakumi")
@CrossOrigin(origins = "*")
public class PasakumsController {

    private final PasakumsService service;

    public PasakumsController(PasakumsService service) {
        this.service = service;
    }

    @GetMapping
    public List<Pasakums> getAll() {
        return service.getAllPasakumi();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pasakums> getById(@PathVariable Long id) {
        Pasakums pasakums = service.getPasakumsById(id);
        if (pasakums != null) {
            return ResponseEntity.ok(pasakums);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody Pasakums pasakums) {
        try {
            Long eventId = service.createPasakums(pasakums);
            return ResponseEntity.ok(eventId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pasakums> update(@PathVariable Long id, @RequestBody Pasakums pasakums) {
        Pasakums existingPasakums = service.getPasakumsById(id);
        if (existingPasakums != null) {
            return ResponseEntity.ok(service.updatePasakums(pasakums));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Pasakums pasakums = service.getPasakumsById(id);
        if (pasakums != null) {
            service.deletePasakums(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search/nosaukums")
    public List<Pasakums> searchByName(@RequestParam String q) {
        return service.searchByNosaukums(q);
    }

    @GetMapping("/search/vieta")
    public List<Pasakums> searchByLocation(@RequestParam String q) {
        return service.searchByVieta(q);
    }

    @GetMapping("/date/{date}")
    public List<Pasakums> getByDate(@PathVariable LocalDate date) {
        return service.getPasakumiByDate(date);
    }

    @PostMapping("/{id}/register")
    public ResponseEntity<String> register(@PathVariable Long id) {
        return service.registerForEvent(id)
                ? ResponseEntity.ok("Veiksmīgi piereģistrējāties pasākumam")
                : ResponseEntity.badRequest().body("Reģistrācija neizdevās - pasākums var būt pilns vai nav atrasts");
    }

    @DeleteMapping("/{id}/unregister")
    public ResponseEntity<String> unregister(@PathVariable Long id) {
        return service.unregisterFromEvent(id)
                ? ResponseEntity.ok("Veiksmīgi atcēlāt dalību pasākumā")
                : ResponseEntity.badRequest()
                        .body("Dalības atcelšana neizdevās - pasākums nav atrasts vai neesat reģistrējies");
    }
}