package es.raulsanchez.gestionaula.repository;

import es.raulsanchez.gestionaula.entity.Aula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AulaRepository extends JpaRepository<Aula, Long> {
    List<Aula> findByOrdenadores(boolean ordenadores);

    List<Aula> findByCapacidadGreaterThan(int capacidad);

}
