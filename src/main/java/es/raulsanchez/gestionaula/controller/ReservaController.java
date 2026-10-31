
package es.raulsanchez.gestionaula.controller;

import es.raulsanchez.gestionaula.entity.Reserva;
import es.raulsanchez.gestionaula.repository.AulaRepository;
import es.raulsanchez.gestionaula.repository.FranjaHorariaRepository;
import es.raulsanchez.gestionaula.repository.ProfesorRepository;
import es.raulsanchez.gestionaula.repository.ReservaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaRepository reservaRepository;
    private final AulaRepository aulaRepository;
    private final ProfesorRepository profesorRepository;
    private final FranjaHorariaRepository franjaHorariaRepository;

    public ReservaController(
            ReservaRepository reservaRepository,
            AulaRepository aulaRepository,
            ProfesorRepository profesorRepository,
            FranjaHorariaRepository franjaHorariaRepository) {

        this.reservaRepository = reservaRepository;
        this.aulaRepository = aulaRepository;
        this.profesorRepository = profesorRepository;
        this.franjaHorariaRepository = franjaHorariaRepository;
    }

    @PostMapping
    public ResponseEntity<Reserva> crearReserva(@RequestBody Reserva reserva) {

        // Comprobar fecha pasada
        if (reserva.getFecha() == null ||
                reserva.getFecha().isBefore(LocalDate.now())) {
            return ResponseEntity.badRequest().build();
        }

        // Comprobar que se han indicado las relaciones
        if (reserva.getAula() == null ||
                reserva.getProfesor() == null ||
                reserva.getFranjaHoraria() == null ||
                reserva.getAula().getId() == null ||
                reserva.getProfesor().getId() == null ||
                reserva.getFranjaHoraria().getId() == null) {
            return ResponseEntity.badRequest().build();
        }

        // Comprobar que existen el aula, profesor y franja
        if (!aulaRepository.existsById(reserva.getAula().getId()) ||
                !profesorRepository.existsById(reserva.getProfesor().getId()) ||
                !franjaHorariaRepository.existsById(reserva.getFranjaHoraria().getId())) {

            return ResponseEntity.badRequest().build();
        }

        // Comprobar si el aula está ocupada
        boolean ocupada = reservaRepository.existsByAulaIdAndFechaAndFranjaHorariaId(
                reserva.getAula().getId(),
                reserva.getFecha(),
                reserva.getFranjaHoraria().getId()
        );

        if (ocupada) {
            return ResponseEntity.badRequest().build();
        }

        reserva.setId(null);

        Reserva nueva = reservaRepository.save(reserva);
        return ResponseEntity.status(201).body(nueva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reserva> actualizarReserva(
            @PathVariable Long id,
            @RequestBody Reserva reserva) {

        Reserva existente = reservaRepository.findById(id).orElse(null);

        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        // No permitir fechas que ya pasaron
        if (reserva.getFecha() == null ||
                reserva.getFecha().isBefore(LocalDate.now())) {
            return ResponseEntity.badRequest().build();
        }

        reserva.setId(id);

        reserva.setFechaInsercion(existente.getFechaInsercion());

        return ResponseEntity.ok(reservaRepository.save(reserva));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarReserva(@PathVariable Long id) {
        if (!reservaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        reservaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping
    public List<Reserva> listarReservas() {
        return reservaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> obtenerReserva(@PathVariable Long id) {
        return reservaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
