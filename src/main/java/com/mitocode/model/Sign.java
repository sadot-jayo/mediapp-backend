package com.mitocode.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Sign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer idSign;

    @ManyToOne
    @JoinColumn(name = "id_patient", nullable = false, foreignKey = @ForeignKey(name = "FK_SIGN_PATIENT"))
    private Patient patient;

    @Column(nullable = false)
    private LocalDateTime signDate;

    @Column(nullable = false)
    private String temperature;

    @Column(nullable = false)
    private String pulse;

    @Column(nullable = false)
    private String respiratoryRate;
}
