package org.oskars.Pasakums.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

// Step 1: Tell JPA this is a database table
@Entity
@Table(name = "pasakumi")
// Step 2: Lombok magic - auto-generates getters, setters, toString, equals,
// hashCode
@Data
// Step 3: Default constructor for JPA
@NoArgsConstructor
// Step 4: Constructor with all parameters
@AllArgsConstructor
public class Pasakums {

    // Step 5: Primary key that auto-increments
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Step 6: Required field for event name
    @Column(nullable = false)
    private String nosaukums;

    // Step 7: Optional long description (max 250 chars)
    @Column(length = 250)
    private String apraksts;

    // Step 8: Required event date
    @Column(nullable = false)
    private LocalDate datums;

    // Step 9: Required event time
    @Column(nullable = false)
    private LocalTime laiks;

    // Step 10: Required event location
    @Column(nullable = false)
    private String vieta;

    // Step 11: Maximum number of participants
    @Column(nullable = false)
    private Integer maxDalibnieki;

    // Step 12: Current number of participants
    @Column(nullable = false)
    private Integer currentDalibnieki;

    // Step 13: Custom constructor with default currentDalibnieki = 0
    public Pasakums(String nosaukums, String apraksts, LocalDate datums, LocalTime laiks, String vieta,
            Integer maxDalibnieki) {
        this.nosaukums = nosaukums;
        this.apraksts = apraksts;
        this.datums = datums;
        this.laiks = laiks;
        this.vieta = vieta;
        this.maxDalibnieki = maxDalibnieki;
        this.currentDalibnieki = 0;
    }

    // Step 14: Business logic methods
    public boolean isFull() {
        return currentDalibnieki >= maxDalibnieki;
    }

    public boolean isPast() {
        return datums.isBefore(LocalDate.now()) ||
                (datums.equals(LocalDate.now()) && laiks.isBefore(LocalTime.now()));
    }
}