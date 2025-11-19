package org.oskars.Pasakums.service;

import org.oskars.Pasakums.entity.Pasakums;
import org.oskars.Pasakums.repository.PasakumsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PasakumsService {

    @Autowired
    private PasakumsRepository pasakumsRepository;

    public List<Pasakums> getAllPasakumi() {
        return pasakumsRepository.findAll();
    }

    public Optional<Pasakums> getPasakumsById(Long id) {
        return pasakumsRepository.findById(id);
    }

    public Long createPasakums(Pasakums pasakums) {
        Pasakums savedPasakums = pasakumsRepository.save(pasakums);
        return savedPasakums.getId();
    }

    public Pasakums updatePasakums(Pasakums pasakums) {
        return pasakumsRepository.save(pasakums);
    }

    public void deletePasakums(Long id) {
        pasakumsRepository.deleteById(id);
    }

    public List<Pasakums> searchByNosaukums(String nosaukums) {
        return pasakumsRepository.findByNosaukumsContainingIgnoreCase(nosaukums);
    }

    public List<Pasakums> searchByVieta(String vieta) {
        return pasakumsRepository.findByVietaContainingIgnoreCase(vieta);
    }

    public List<Pasakums> getPasakumiByDate(LocalDate datums) {
        return pasakumsRepository.findByDatums(datums);
    }

    public boolean registerForEvent(Long id) {

        Optional<Pasakums> optionalPasakums = pasakumsRepository.findById(id);
        if (optionalPasakums.isPresent()) {
            Pasakums pasakums = optionalPasakums.get();

            if (!pasakums.isFull()) {

                pasakums.setCurrentDalibnieki(pasakums.getCurrentDalibnieki() + 1);

                pasakumsRepository.save(pasakums);
                return true;
            }
        }

        return false;
    }
}