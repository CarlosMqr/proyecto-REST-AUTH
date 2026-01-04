package com.course.REST.controller;

import com.course.REST.model.School;
import com.course.REST.service.ISchoolService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/school")
@CrossOrigin
public class SchoolController {
    @Autowired
    private ISchoolService schoolService;

    @GetMapping("/schools/{id}")
    public ResponseEntity<?> showById(@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        School school = null;

        try {
            school = schoolService.findById(id);
        }catch (DataAccessException e){
            response.put("Error", "Error al realizar la consulta");
            response.put("Error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response,INTERNAL_SERVER_ERROR);
        }

        if (school == null){
            response.put("Mensaje", "La escuela cin ID: ".concat(id.toString().concat("No existe en la BD")));
            return  new ResponseEntity<>(response, NOT_FOUND);
        }

        return  new ResponseEntity<>(response, OK);
    }

    @GetMapping("/schools")
    public List<School> getAllSchools(){
        return schoolService.getAll();
    }

    @PostMapping("/schools")
    public ResponseEntity<?> create(@RequestBody @Valid School school, BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        School schoolSave = null;

        if (result.hasErrors()) {
            System.out.println("result = " + result);
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> " El campo '" + err.getField() + "' " + err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("error", errors);
            return new ResponseEntity<>(response, BAD_REQUEST);
        }

        try {
           schoolSave =  schoolService.save(school);
        }catch (DataAccessException e){
            response.put("Error", "Error al guardar la Escuela" );
            response.put("Error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, INTERNAL_SERVER_ERROR);
        }
        response.put("Mensaje",  "La escuela fue creada con éxito");
        response.put("Escuela", schoolSave);
        return  new ResponseEntity<>(response, CREATED);
    }

    @PutMapping("/schools/{id}")
    public ResponseEntity<?> update(@RequestBody School school, @PathVariable Long id, BindingResult result){
        Map<String, Object> response = new HashMap<>();
        School schoolUpdate = null;

        if (result.hasErrors()){
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "El campo " + err.getField() + "' " + err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("Error", errors);
            return new ResponseEntity<>(response, BAD_REQUEST);
        }

        try {
            schoolService.update(school,id);
        }catch (DataAccessException e){
            response.put("Mensaje", "Error al actualizar los datos de la escuela");
            response.put("Error", e.getMostSpecificCause().getMessage());
            return  new ResponseEntity<>(response, INTERNAL_SERVER_ERROR);
        }

        response.put("Mensaje", "Se actualizaron los datos de la escuela con éxito");
        return  new ResponseEntity<>(response,CREATED);
    }

    @DeleteMapping("/schools/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();

        try {
            schoolService.delete(id);
        }catch (DataAccessException e){
            response.put("Error", "Error al eliminar la escuela");
            response.put("Error", e.getMostSpecificCause().getMessage());
            return  new ResponseEntity<>(response,INTERNAL_SERVER_ERROR);
        }
        response.put("Mensaje", "Se elimino la escuela");
        return  new ResponseEntity<>(response, OK);
    }
}
