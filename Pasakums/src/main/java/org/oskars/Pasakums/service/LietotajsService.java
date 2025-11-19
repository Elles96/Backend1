package org.oskars.Pasakums.service;

import org.oskars.Pasakums.entity.Lietotajs;
import org.oskars.Pasakums.repository.LietotajsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LietotajsService {

    @Autowired
    private LietotajsRepository lietotajsRepository;

    public List<Lietotajs> getAllLietotaji() {
        return lietotajsRepository.findAll();
    }

    public Optional<Lietotajs> getLietotajsById(Long id) {
        return lietotajsRepository.findById(id);
    }

    public Long createLietotajs(Lietotajs lietotajs) {

        if (lietotajsRepository.existsByLietotajvards(lietotajs.getLietotajvards())) {

            throw new RuntimeException("Lietotājvārds jau pastāv: " + lietotajs.getLietotajvards());
        }

        Lietotajs savedUser = lietotajsRepository.save(lietotajs);
        return savedUser.getId();
    }

    public Optional<Lietotajs> findByLietotajvards(String lietotajvards) {
        return lietotajsRepository.findByLietotajvards(lietotajvards);
    }

    public boolean existsByLietotajvards(String lietotajvards) {
        return lietotajsRepository.existsByLietotajvards(lietotajvards);
    }

    public boolean authenticate(String lietotajvards, String parole) {

        Optional<Lietotajs> user = lietotajsRepository.findByLietotajvards(lietotajvards);
        if (user.isPresent()) {

            return user.get().getParole().equals(parole);
        }
        return false;
    }

    public void deleteLietotajs(Long id) {
        lietotajsRepository.deleteById(id);
    }
}