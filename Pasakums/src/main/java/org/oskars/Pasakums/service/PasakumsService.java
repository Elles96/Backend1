package org.oskars.Pasakums.service;

import org.oskars.Pasakums.entity.Pasakums;
import org.oskars.Pasakums.repository.PasakumsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// Step 1: Tell Spring this is a business logic layer
@Service
public class PasakumsService {

    // Step 2: Inject the repository to access database
    @Autowired
    private PasakumsRepository pasakumsRepository;

    // Step 3: Get all events from database
    public List<Pasakums> getAllPasakumi() {
        return pasakumsRepository.findAll();
    }

    // Step 4: Get one event by ID (might not exist, so Optional)
    public Optional<Pasakums> getPasakumsById(Long id) {
        return pasakumsRepository.findById(id);
    }

    // Step 5: Save new event to database and return ID only
    public Long createPasakums(Pasakums pasakums) {
        Pasakums savedPasakums = pasakumsRepository.save(pasakums);
        return savedPasakums.getId();
    }

    // Step 6: Update existing event in database
    public Pasakums updatePasakums(Pasakums pasakums) {
        return pasakumsRepository.save(pasakums);
    }

    // Step 7: Delete event from database
    public void deletePasakums(Long id) {
        pasakumsRepository.deleteById(id);
    }

    // Step 8: Search events by name (partial match)
    public List<Pasakums> searchByNosaukums(String nosaukums) {
        return pasakumsRepository.findByNosaukumsContainingIgnoreCase(nosaukums);
    }

    // Step 9: Search events by location (partial match)
    public List<Pasakums> searchByVieta(String vieta) {
        return pasakumsRepository.findByVietaContainingIgnoreCase(vieta);
    }

    // Step 10: Get events happening on specific date
    public List<Pasakums> getPasakumiByDate(LocalDate datums) {
        return pasakumsRepository.findByDatums(datums);
    }

    // Step 11: Register for event (business logic with validation)
    public boolean registerForEvent(Long id) {
        // Step 11a: Try to find the event
        Optional<Pasakums> optionalPasakums = pasakumsRepository.findById(id);
        if (optionalPasakums.isPresent()) {
            Pasakums pasakums = optionalPasakums.get();
            // Step 11b: Check if event is not full
            if (!pasakums.isFull()) {
                // Step 11c: Update participant count
                pasakums.setCurrentDalibnieki(pasakums.getCurrentDalibnieki() + 1);
                // Step 11d: Save updated event
                pasakumsRepository.save(pasakums);
                return true;
            }
        }
        // Step 11e: Return false if event not found or full
        return false;
    }
}