package org.oskars.Pasakums.repository;

import org.oskars.Pasakums.entity.Pasakums;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository

public interface PasakumsRepository extends JpaRepository<Pasakums, Long> {

    List<Pasakums> findByDatums(LocalDate datums);

    List<Pasakums> findByNosaukumsContainingIgnoreCase(String nosaukums);

    List<Pasakums> findByVietaContainingIgnoreCase(String vieta);
}
