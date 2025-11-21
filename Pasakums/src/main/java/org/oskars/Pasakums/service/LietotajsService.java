package org.oskars.Pasakums.service;

import org.oskars.Pasakums.entity.Lietotajs;
import org.oskars.Pasakums.repository.LietotajsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LietotajsService {

    @Autowired
    private LietotajsRepository lietotajsRepository;

    public List<Lietotajs> getAllLietotaji() {
        return lietotajsRepository.findAll();
    }

    public Lietotajs getLietotajsById(Long id) {
        return lietotajsRepository.findById(id).orElse(null);
    }

    public Long createLietotajs(Lietotajs lietotajs) {
        if (lietotajsRepository.existsByLietotajvards(lietotajs.getLietotajvards())) {
            throw new RuntimeException("Lietotājvārds jau pastāv: " + lietotajs.getLietotajvards());
        }

        Lietotajs savedLietotajs = lietotajsRepository.save(lietotajs);
        return savedLietotajs.getId();
    }

    public Lietotajs findByLietotajvards(String lietotajvards) {
        return lietotajsRepository.findByLietotajvards(lietotajvards).orElse(null);
    }

    public boolean existsByLietotajvards(String lietotajvards) {
        return lietotajsRepository.existsByLietotajvards(lietotajvards);
    }

    public boolean authenticate(String lietotajvards, String parole) {
        Lietotajs lietotajs = findByLietotajvards(lietotajvards);

        if (lietotajs == null) {
            return false; // User not found
        }

        String lietotajaParole = lietotajs.getParole();
        if (lietotajaParole.equals(parole)) {
            return true; // Password matches
        } else {
            return false; // Password doesn't match
        }
    }

    public void deleteLietotajs(Long id) {
        lietotajsRepository.deleteById(id);
    }
}