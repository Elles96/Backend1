package org.oskars.Pasakums.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "lietotaji")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lietotajs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String lietotajvards;

    @Column(nullable = false)
    private String parole;

}
