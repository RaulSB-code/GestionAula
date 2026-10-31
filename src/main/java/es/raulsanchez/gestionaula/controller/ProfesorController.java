package es.raulsanchez.gestionaula.controller;

import es.raulsanchez.gestionaula.entity.Profesor;
import es.raulsanchez.gestionaula.repository.ProfesorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profesores")
public class ProfesorController {

    private final ProfesorRepository profesorRepository;

    public ProfesorController(ProfesorRepository profesorRepository) {
        this.profesorRepository = profesorRepository;
    }

    @GetMapping
    public List<Profesor> listarProfesores() {
        return profesorRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profesor> obtenerProfesor(@PathVariable Long id) {
        return profesorRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Profesor> crearProfesor(@RequestBody Profesor profesor) {
        profesor.setId(null);
        Profesor nuevo = profesorRepository.save(profesor);
        return ResponseEntity.status(201).body(nuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Profesor> actualizarProfesor(@PathVariable Long id,
                                                       @RequestBody Profesor profesor) {
        if (!profesorRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        profesor.setId(id);
        return ResponseEntity.ok(profesorRepository.save(profesor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarProfesor(@PathVariable Long id) {
        if (!profesorRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        profesorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
