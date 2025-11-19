package org.oskars.Pasakums.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// Step 1: Tell JPA this is a database table
@Entity
@Table(name = "lietotaji")
// Step 2: Lombok magic - auto-generates getters, setters, toString, equals,
// hashCode
@Data
// Step 3: Default constructor for JPA
@NoArgsConstructor
// Step 4: Constructor with all parameters
@AllArgsConstructor
public class Lietotajs {

    // Step 5: Auto-increment primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Step 6: Username column (unique constraint)
    @Column(nullable = false, unique = true)
    private String lietotajvards;

    // Step 7: Password column (required field)
    @Column(nullable = false)
    private String parole;

    // Step 8: Custom constructor for new users (without ID)
    public Lietotajs(String lietotajvards, String parole) {
        this.lietotajvards = lietotajvards;
        this.parole = parole;
    }
}
