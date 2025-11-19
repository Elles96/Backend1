package org.oskars.Pasakums.repository;

import org.oskars.Pasakums.entity.Pasakums;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

// Step 1: Tell Spring this is a data access layer
@Repository
// Step 2: Extend JpaRepository for free CRUD methods (save, find, delete, etc.)
public interface PasakumsRepository extends JpaRepository<Pasakums, Long> {

    // Step 3: Spring creates this method automatically from the name
    // "findByDatums" = SELECT * FROM pasakumi WHERE datums = ?
    List<Pasakums> findByDatums(LocalDate datums);

    // Step 4: Search events by name (case-insensitive)
    // "ContainingIgnoreCase" = WHERE nosaukums LIKE '%?%' (case-insensitive)
    List<Pasakums> findByNosaukumsContainingIgnoreCase(String nosaukums);

    // Step 5: Search events by location (case-insensitive)
    List<Pasakums> findByVietaContainingIgnoreCase(String vieta);
}
