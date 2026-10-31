package es.raulsanchez.gestionaula.repository;

import es.raulsanchez.gestionaula.entity.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfesorRepository extends JpaRepository<Profesor, Long> {
}
