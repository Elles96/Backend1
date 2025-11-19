package org.oskars.Pasakums.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "pasakumi")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pasakums {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nosaukums;

    @Column(length = 250)
    private String apraksts;

    @Column(nullable = false)
    private LocalDate datums;

    @Column(nullable = false)
    private LocalTime laiks;

    @Column(nullable = false)
    private String vieta;

    @Column(nullable = false)
    private Integer maxDalibnieki;

    @Column(nullable = false)
    private Integer currentDalibnieki;

    public boolean isFull() {
        return currentDalibnieki >= maxDalibnieki;
    }

    public boolean isPast() {
        return datums.isBefore(LocalDate.now()) ||
                (datums.equals(LocalDate.now()) && laiks.isBefore(LocalTime.now()));
    }
}