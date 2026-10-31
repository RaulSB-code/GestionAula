
package es.raulsanchez.gestionaula.repository;

import es.raulsanchez.gestionaula.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    boolean existsByAulaIdAndFechaAndFranjaHorariaId(
            Long aulaId, LocalDate fecha, Long franjaId);
}
