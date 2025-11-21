package org.oskars.Pasakums.service;

import org.oskars.Pasakums.entity.Pasakums;
import org.oskars.Pasakums.repository.PasakumsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PasakumsService {

    @Autowired
    private PasakumsRepository pasakumsRepository;

    public List<Pasakums> getAllPasakumi() {
        return pasakumsRepository.findAll();
    }

    public Pasakums getPasakumsById(Long id) {
        return pasakumsRepository.findById(id).orElse(null);
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
        Pasakums pasakums = getPasakumsById(id);

        if (pasakums == null) {
            return false; // Event not found
        }

        if (pasakums.isFull()) {
            return false; // Event is full
        }

        pasakums.setCurrentDalibnieki(pasakums.getCurrentDalibnieki() + 1);
        pasakumsRepository.save(pasakums);
        return true; // Successfully registered
    }

    public boolean unregisterFromEvent(Long id) {
        Pasakums pasakums = getPasakumsById(id);

        if (pasakums == null) {
            return false; // Event not found
        }

        if (pasakums.getCurrentDalibnieki() <= 0) {
            return false; // No participants to remove
        }

        pasakums.setCurrentDalibnieki(pasakums.getCurrentDalibnieki() - 1);
        pasakumsRepository.save(pasakums);
        return true; // Successfully unregistered
    }
}