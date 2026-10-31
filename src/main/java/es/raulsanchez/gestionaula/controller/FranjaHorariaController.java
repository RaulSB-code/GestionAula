package es.raulsanchez.gestionaula.controller;

import es.raulsanchez.gestionaula.entity.FranjaHoraria;
import es.raulsanchez.gestionaula.repository.FranjaHorariaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/franjas")
public class FranjaHorariaController {

    private final FranjaHorariaRepository franjaHorariaRepository;

    public FranjaHorariaController(FranjaHorariaRepository franjaHorariaRepository) {
        this.franjaHorariaRepository = franjaHorariaRepository;
    }

    @GetMapping
    public List<FranjaHoraria> listarFranjas() {
        return franjaHorariaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FranjaHoraria> obtenerFranja(@PathVariable Long id) {
        return franjaHorariaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FranjaHoraria> crearFranja(@RequestBody FranjaHoraria franja) {
        franja.setId(null);
        FranjaHoraria nueva = franjaHorariaRepository.save(franja);
        return ResponseEntity.status(201).body(nueva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FranjaHoraria> actualizarFranja(@PathVariable Long id,
                                                          @RequestBody FranjaHoraria franja) {
        if (!franjaHorariaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        franja.setId(id);
        return ResponseEntity.ok(franjaHorariaRepository.save(franja));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarFranja(@PathVariable Long id) {
        if (!franjaHorariaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        franjaHorariaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
