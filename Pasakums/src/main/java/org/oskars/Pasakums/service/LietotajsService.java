package org.oskars.Pasakums.service;

import org.oskars.Pasakums.entity.Lietotajs;
import org.oskars.Pasakums.repository.LietotajsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Step 1: Tell Spring this is a business logic layer
@Service
public class LietotajsService {

    // Step 2: Inject the repository to access database
    @Autowired
    private LietotajsRepository lietotajsRepository;

    // Step 3: Get all users from database
    public List<Lietotajs> getAllLietotaji() {
        return lietotajsRepository.findAll();
    }

    // Step 4: Get one user by ID (might not exist, so Optional)
    public Optional<Lietotajs> getLietotajsById(Long id) {
        return lietotajsRepository.findById(id);
    }

    // Step 5: Register new user (with validation)
    public Long createLietotajs(Lietotajs lietotajs) {
        // Step 5a: Check if username already exists
        if (lietotajsRepository.existsByLietotajvards(lietotajs.getLietotajvards())) {
            // Step 5b: Throw error if username taken (in Latvian)
            throw new RuntimeException("Lietotājvārds jau pastāv: " + lietotajs.getLietotajvards());
        }
        // Step 5c: Save user and return just the ID (avoids Record setter issue)
        Lietotajs savedUser = lietotajsRepository.save(lietotajs);
        return savedUser.getId();
    }

    // Step 6: Find user by username (for login)
    public Optional<Lietotajs> findByLietotajvards(String lietotajvards) {
        return lietotajsRepository.findByLietotajvards(lietotajvards);
    }

    // Step 7: Check if username exists (returns true/false)
    public boolean existsByLietotajvards(String lietotajvards) {
        return lietotajsRepository.existsByLietotajvards(lietotajvards);
    }

    // Step 8: Login authentication (check username + password)
    public boolean authenticate(String lietotajvards, String parole) {
        // Step 8a: Try to find user by username
        Optional<Lietotajs> user = lietotajsRepository.findByLietotajvards(lietotajvards);
        if (user.isPresent()) {
            // Step 8b: Compare passwords (NOTE: In real app, use hashed passwords!)
            // For exam purposes, simple string comparison
            return user.get().getParole().equals(parole);
        }
        // Step 8c: Return false if user not found
        return false;
    }

    // Step 9: Delete user from database
    public void deleteLietotajs(Long id) {
        lietotajsRepository.deleteById(id);
    }
}