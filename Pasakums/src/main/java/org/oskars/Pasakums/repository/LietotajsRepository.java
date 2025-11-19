package org.oskars.Pasakums.repository;

import org.oskars.Pasakums.entity.Lietotajs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Step 1: Tell Spring this is a data access layer
@Repository
// Step 2: Extend JpaRepository for free CRUD methods
public interface LietotajsRepository extends JpaRepository<Lietotajs, Long> {

    // Step 3: Find user by username (returns Optional - might not exist)
    // Spring creates: SELECT * FROM lietotaji WHERE lietotajvards = ?
    Optional<Lietotajs> findByLietotajvards(String lietotajvards);

    // Step 4: Check if username exists (returns true/false)
    // Spring creates: SELECT COUNT(*) > 0 FROM lietotaji WHERE lietotajvards = ?
    boolean existsByLietotajvards(String lietotajvards);
}