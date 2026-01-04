package com.course.REST.controller;

import com.course.REST.model.Student;
import com.course.REST.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/student")
@CrossOrigin
public class StudentController {

    @Autowired
    private IStudentService studentService;

    @GetMapping("/students/{id}")// buscar por id
     public ResponseEntity<?> showByID(@PathVariable Long id){
        Map<String, Object> respose = new HashMap<>();
        Student student = null;

        try {
            student = studentService.findById(id);
        }catch (DataAccessException e){
            respose.put("Mensaje", "Error al realizar la consulta");
            respose.put("Error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(respose, INTERNAL_SERVER_ERROR);
        }
        if (student==null){
            respose.put("Mensaje", "El estudiante con ID: ".concat(id.toString(id).concat("NO existe en la BD")));
            return new ResponseEntity<>(respose, NOT_FOUND);
        }
        return new ResponseEntity<>(student,OK);
    }

    @GetMapping("/students")// obtiene todos los estudiantes
    public List<Student> getAllStudents() {
        return studentService.getAll();
    }

    @PostMapping("/students")// crea un nuevo estudiante
    public ResponseEntity<?> create(@RequestBody Student student, BindingResult result){
        Map<String,Object> response = new HashMap<>();
        Student studentSave = null;

        if (result.hasErrors()){
            List<String> errors =  result.getFieldErrors()
                    .stream()
                    .map(err -> " El campo '" + err.getField() + "' " + err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("Error", errors);
            return new ResponseEntity<>(response, BAD_REQUEST);
        }

        try {
            studentSave = studentService.save(student);
        }catch (DataAccessException e){
            response.put("Mensaje", "Error al guardar el estudiante");
            response.put("Error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, INTERNAL_SERVER_ERROR);
        }
           response.put("Mensaje", "El estudiante se guardo con exito!!");
           response.put("Estudiante", studentSave);
           return  new ResponseEntity<>(response, CREATED);
    }


    @PutMapping("/students/{id}")// actualiza un estudiante
    public ResponseEntity<?> update(@RequestBody Student student, @PathVariable Long id, BindingResult result){
        Map<String ,Object> response = new HashMap<>();

        Student student1 = studentService.findById(id);

        /*if (result.hasErrors()){
         List<String> errors = result.getFieldErrors()
                 .stream()
                 .map(err -> "El campo " + err.getField() + "' " + err.getDefaultMessage())
                 .collect(Collectors.toList());
         response.put("Errors" , errors);
         return  new ResponseEntity<>(response, BAD_REQUEST);
        }*/
        if (result.hasErrors()){
            return validation(result);
        }


        if (student1 == null){
           response.put("Mensaje", "Error:, no se puede editar, el cliente con ID: ".concat(id.toString().concat(" no existe en la BD")));
           return  new ResponseEntity<>(response, NOT_FOUND);
        }

        try {
            studentService.update(student, id);
        }catch (DataAccessException e){
            response.put("Mensaje", "Error al realizar el update en la BD");
            response.put("Error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response,INTERNAL_SERVER_ERROR);
        }

        response.put("Mensaje"," El cliente se actualizo con éxito!");
        return new ResponseEntity<>(response,CREATED);
    }


    @DeleteMapping("/students/{id}")// elimina un estudiante
    public ResponseEntity<?> delete(@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        try {
            studentService.delete(id);
        }catch (DataAccessException e){
            response.put("Mensaje", "Error al eliminar el estudiante");
            response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, INTERNAL_SERVER_ERROR);
        }
        response.put("Mensaje", "El estudiante se elimino con exito!!");
        return  new ResponseEntity<>(response, OK);
    }

    private ResponseEntity<?> validation(BindingResult result){
        Map<String, String> response = new HashMap<>();
        result.getFieldErrors().forEach(err->{
            response.put(err.getField(), "El campo '" + err.getField() + "' " + err.getDefaultMessage());
        });

        return new ResponseEntity<>(response, BAD_REQUEST);
    }





}
