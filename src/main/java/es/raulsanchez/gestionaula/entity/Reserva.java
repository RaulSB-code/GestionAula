package es.raulsanchez.gestionaula.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate fecha;
    private LocalDateTime fechaInsercion = LocalDateTime.now();

    @ManyToOne
    private Aula aula;

    @ManyToOne
    private Profesor profesor;

    @ManyToOne
    private FranjaHoraria franjaHoraria;

}
