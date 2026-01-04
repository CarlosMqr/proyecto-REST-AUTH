package com.course.REST.controller;

import com.course.REST.controller.StudentController;
import com.course.REST.model.Student;
import com.course.REST.service.IStudentService;
import com.course.REST.service.StudentServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private IStudentService studentService;

    @Test
    @DisplayName("Busca por id de estudiante")
    void testGetStudentById()throws Exception{
        Long id = 1L;
        Student student = new Student();
        student.setId(id);
        student.setName("Carlos");
        student.setLastName("Mendoza");
        student.setRegistered(true);


        Mockito.when(studentService.findById(id)).thenReturn(student);

        mockMvc.perform(get("/student/students/{id}",id))
                .andExpect(status().isOk());

    }
    @Test
    @DisplayName("Simula catch al buscar por id de estudiante")
    void testCatchStudentById()throws Exception{
        Long id = 1L;

        Mockito.when(studentService.findById(id)).thenThrow(new DataAccessException("Error al realizar la consulta") {
        });

        mockMvc.perform(get("/student/students/{id}",id))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.Mensaje").value("Error al realizar la consulta"));
    }

    @Test
    @DisplayName("id de estudiante no encontrado")
    void testNotFountStudentById()throws Exception{
        Long id = 1L;

        Mockito.when(studentService.findById(id)).thenReturn(null);

        mockMvc.perform(get("/student/students/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Obtiene todos los estudiantes")
    void testGetAllStudents()throws Exception{
        Student carlos = new Student();
        carlos.setId(1L);
        carlos.setName("Carlos");
        carlos.setLastName("Mendoza");
        carlos.setRegistered(true);

        Student emily = new Student();
        emily.setId(2L);
        emily.setName("Emily");
        emily.setLastName("Martinez");
        emily.setRegistered(true);

        List<Student> studentList = new ArrayList<>();
        studentList.add(carlos);
        studentList.add(emily);

        Mockito.when(studentService.getAll()).thenReturn(studentList);

        mockMvc.perform(get("/student/students"))
                .andExpect(status().isOk());

        //se usa para verificar que se obtenga la lista de estudiantes
        //cuando en el response se espera un json con los datos de los estudiantes
         /*.andExpect(jsonPath("$.size()").value(studentList.size()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Carlos"))
                .andExpect(jsonPath("$[0].lastName").value("Mendoza"))
                .andExpect(jsonPath("$[0].registered").value(true))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Emily"))
                .andExpect(jsonPath("$[1].lastName").value("Martinez"))
                .andExpect(jsonPath("$[1].registered").value(true));*/


    }

    @Test
    @DisplayName("Crea un nuevo estudiante")
    void testCreateStudent()throws Exception{
        Student student = new Student();
        student.setName("Danna");
        student.setLastName("Lopez");
        student.setRegistered(true);
        student.setSchool(null);

        Mockito.when(studentService.save(Mockito.any(Student.class))).thenReturn(student);

        mockMvc.perform(post("/student/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"name\": \"Danna\",\n" +
                                "    \"lastName\": \"Lopez\",\n" +
                                "    \"registered\": true,\n" +
                                "    \"school\": null\n" +
                                "}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.Mensaje").value("El estudiante se guardo con exito!!"))
                .andExpect(jsonPath("$.Estudiante.name").value("Danna"))
                .andExpect(jsonPath("$.Estudiante.lastName").value("Lopez"))
                .andExpect(jsonPath("$.Estudiante.registered").value(true))
                .andExpect(jsonPath("$.Estudiante.school").value((Object) null))
                .andExpect(status().isCreated());

    }

    @Test
    @DisplayName("Erro al crear un nuevo estudiante")
    void testErrorCreateStudent()throws Exception{

        Mockito.when(studentService.save(Mockito.any(Student.class))).thenThrow(new DataAccessException("Error al guardar el estudiante") {
        });

        mockMvc.perform(post("/student/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"name\": \"Kat\",\n" +
                                "    \"lastName\": \"Diaz\",\n" +
                                "    \"registered\": true,\n" +
                                "    \"school\": null\n" +
                                "}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.Mensaje").value("Error al guardar el estudiante"))
                .andExpect(status().isInternalServerError());

    }

    @Test
    @DisplayName("Actualiza un estudiante")
    void testUpdateStudent()throws Exception{

        Long id = 1L;
        Student actStudent = new Student();
        actStudent.setId(id);
        actStudent.setName("Carlos");
        actStudent.setLastName("Mendoza");
        actStudent.setRegistered(true);
        actStudent.setSchool(null);

        Student updStudent = new Student();
        updStudent.setId(id);
        updStudent.setName("Emily");
        updStudent.setLastName("Martinez");
        updStudent.setRegistered(true);
        updStudent.setSchool(null);

        Mockito.when(studentService.findById(id)).thenReturn(actStudent);
        Mockito.when(studentService.update(Mockito.any(Student.class), Mockito.eq(id))).thenReturn(updStudent);

        mockMvc.perform(put("/student/students/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"name\": \"Emily\",\n" +
                        "    \"lastName\": \"Martinez\",\n" +
                        "    \"registered\": true,\n" +
                        "    \"school\": null\n" +
                        "}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.Mensaje").value(" El cliente se actualizo con éxito!"))
                .andExpect(status().isCreated());

    }

    @Test
    @DisplayName("Catch al actualizar un estudiante")
    void testErrorUpdateStudent()throws Exception {
        Long id = 1L;

        Student updStudent = new Student();
        updStudent.setId(id);
        updStudent.setName("Emily");
        updStudent.setLastName("Martinez");
        updStudent.setRegistered(true);
        updStudent.setSchool(null);

        Mockito.when(studentService.findById(id)).thenReturn(updStudent);

        Mockito.when(studentService.update(Mockito.any(Student.class), Mockito.eq(id))).
                thenThrow(new DataAccessException("Error al realizar el update en la BD"){});

        mockMvc.perform(put("/student/students/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"name\": \"Emily\",\n" +
                                "    \"lastName\": \"Martinez\",\n" +
                                "    \"registered\": true,\n" +
                                "    \"school\": null\n" +
                                "}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.Mensaje").value("Error al realizar el update en la BD"))
                .andExpect(jsonPath("$.Error").value("Error al realizar el update en la BD"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Error al encontrar un estudiante para actualizar")
    void testNotFoundUpdateStudent()throws Exception{
        Long id = 1L;

        Mockito.when(studentService.findById(id)).thenReturn(null);

        mockMvc.perform(put("/student/students/{id}", id))
                .andExpect(status().isBadRequest());
    }



   /* @Test
    @DisplayName("Errores de validación al actualizar un estudiante")
    void testValidationErrorsUpdateStudent() throws Exception {
        Long id = 1L;
        Student student = new Student();
        student.setName("");
        student.setLastName("");
        student.setRegistered(true);
        student.setSchool(null);

        mockMvc.perform(put("/student/students/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"name\": \"\",\n" +
                                "    \"lastName\": \"\",\n" +
                                "    \"registered\": true,\n" +
                                "    \"school\": null\n" +
                                "}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.Mensaje").value("Error:, no se puede editar, el cliente con ID: 1 no existe en la BD"));

    }*/

    @Test
    @DisplayName("Elimina un estudiante")
    void testDeleteStudent()throws Exception{
        Long id = 1L;
        Student student = new Student();
        student.setId(id);
        student.setName("Carlos");
        student.setLastName("Mendoza");
        student.setRegistered(true);
        student.setSchool(null);

        Mockito.when(studentService.findById(id)).thenReturn(student);
        Mockito.doNothing().when(studentService).delete(id);

        mockMvc.perform(delete("/student/students/{id}", id))
                .andExpect(jsonPath("$.Mensaje").value("El estudiante se elimino con exito!!"))
                .andExpect(status().isOk());
    }

     @Test
     @DisplayName("Error catch al eliminar un estudiante")
     void testErroDeleteStudent()throws Exception{
        Long id = 1L;

        Mockito.doThrow(new DataAccessException("Error al eliminar el estudiante") {})
                .when(studentService).delete(id);

        mockMvc.perform(delete("/student/students/{id}", id))
                .andExpect(jsonPath("$.Mensaje").value("Error al eliminar el estudiante"))
                .andExpect(status().isInternalServerError());
     }


}