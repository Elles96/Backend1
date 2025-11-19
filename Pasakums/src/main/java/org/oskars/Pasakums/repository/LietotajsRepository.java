package org.oskars.Pasakums.repository;

import org.oskars.Pasakums.entity.Lietotajs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface LietotajsRepository extends JpaRepository<Lietotajs, Long> {

    Optional<Lietotajs> findByLietotajvards(String lietotajvards);

    boolean existsByLietotajvards(String lietotajvards);
}