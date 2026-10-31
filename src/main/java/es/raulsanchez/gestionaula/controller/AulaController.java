package es.raulsanchez.gestionaula.controller;

import es.raulsanchez.gestionaula.entity.Aula;
import es.raulsanchez.gestionaula.repository.AulaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/aulas")
public class AulaController {
    private final AulaRepository aulaRepository;

    public AulaController(AulaRepository aulaRepository) {
        this.aulaRepository = aulaRepository;
    }

    @GetMapping
    public List<Aula> listarAulas() {
        return aulaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aula> obtenerAula(@PathVariable Long id) {
        return aulaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Aula> crearAula(@RequestBody Aula aula) {
        Aula nueva = aulaRepository.save(aula);
        return ResponseEntity.status(201).body(nueva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aula> actualizarAula(@PathVariable Long id, @RequestBody Aula aula) {
        if (!aulaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        aula.setId(id);
        return ResponseEntity.ok(aulaRepository.save(aula));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarAula(@PathVariable Long id) {
        if (!aulaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        aulaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public List<Aula> buscarAulas(
            @RequestParam(required = false) Boolean ordenadores,
            @RequestParam(required = false) Integer capacidad) {

        if (ordenadores != null) {
            return aulaRepository.findByOrdenadores(ordenadores);
        }

        if (capacidad != null) {
            return aulaRepository.findByCapacidadGreaterThan(capacidad);
        }

        return aulaRepository.findAll();
    }
}
