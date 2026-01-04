package com.course.REST.controller;

import com.course.REST.model.School;
import com.course.REST.service.SchoolServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

import static java.lang.System.err;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SchoolController.class)
class SchoolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SchoolServiceImpl schoolService;

    @Test
    @DisplayName("Obtener escuela por ID")
    void testShowById()throws Exception {
        Long id = 1L;
        School school = new School();
        school.setId(id);
        school.setName("UAEM");
        school.setState("Mexico");

        Mockito.when(schoolService.findById(id)).thenReturn(school);

        mockMvc.perform(get("/school/schools/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Error al obtener escuela por ID")
    void testErroGetById()throws Exception{
        Long id = 1L;

        Mockito.when(schoolService.findById(id)).thenReturn(null);

        mockMvc.perform(get("/school/schools/{id}",id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Catch error al obtener escuela por ID")
    void testCatchErrorGetById()throws  Exception{
        Long id = 1L;

        Mockito.when(schoolService.findById(id)).thenThrow(new DataAccessException("Error al realizar la consulta") {
        });

        mockMvc.perform(get("/school/schools/{id}", id))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.Error").value("Error al realizar la consulta"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Obtener todas las escuelas")
    void testGetAllSchools()throws Exception {
        School school = new School();
        school.setId(1L);
        school.setName("UAEM");
        school.setState("Mexico");

        School school2 = new School();
        school2.setId(2L);
        school2.setName("Lap");
        school2.setState("Mexico");

        List<School> schoolList = List.of(school, school2);

        Mockito.when(schoolService.getAll()).thenReturn(schoolList);

        mockMvc.perform(get("/school/schools"))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("Crear escuela")
    void testCreate()throws Exception {
        Long id = 1L;
        School school = new School();
        school.setId(id);
        school.setName("UAEM");
        school.setState("Mexico");

        Mockito.when(schoolService.save(Mockito.any(School.class))).thenReturn(school);

        mockMvc.perform(post("/school/schools")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"name\": \"UAEM\",\n" +
                                "    \"state\": \"Mexico\"\n" +
                                "}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.Mensaje").value("La escuela fue creada con éxito"))
                .andExpect(jsonPath("$.Escuela.name").value("UAEM"))
                .andExpect(jsonPath("$.Escuela.state").value("Mexico"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Catch erro al crear escuela")
    void testErrorCreateSchool()throws Exception{
        Mockito.when(schoolService.save(Mockito.any(School.class)))
                .thenThrow(new DataAccessException("Error al guardar la Escuela") {});

        mockMvc.perform(post("/school/schools")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"name\": \"UAEM\",\n" +
                        "    \"state\": \"Mexico\"\n" +
                        "}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.Error").value("Error al guardar la Escuela"))
                .andExpect(status().isInternalServerError());

    }


    @Test
    void testUpdate()throws Exception {
        Long id = 1L;
        School actSchool = new School();
        actSchool.setId(id);
        actSchool.setName("LD");
        actSchool.setState("Oaxaca");

        School updateSchool = new School();
        updateSchool.setId(id);
        updateSchool.setName("UAEM");
        updateSchool.setState("Mexico");

        Mockito.when(schoolService.findById(id)).thenReturn(actSchool);
        Mockito.when(schoolService.update(Mockito.any(School.class), Mockito.eq(id))).thenReturn(updateSchool);

        mockMvc.perform(put("/school/schools/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"name\": \"UAEM\",\n" +
                        "    \"state\": \"Mexico\"\n" +
                        "}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.Mensaje").value("Se actualizaron los datos de la escuela con éxito"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Catch error al actualizar escuela")
    void testCatchErrorUpdateSchool()throws Exception{
        Long id = 1L;
        School actSchool = new School();
        actSchool.setId(id);
        actSchool.setName("LD");
        actSchool.setState("Oaxaca");

        Mockito.when(schoolService.findById(id)).thenReturn(actSchool);
        Mockito.when(schoolService.update(Mockito.any(School.class), Mockito.eq(id)))
                .thenThrow(new DataAccessException("Error al actualizar los datos de la escuela") {});

        mockMvc.perform(put("/school/schools/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"name\": \"UAEM\",\n" +
                        "    \"state\": \"Mexico\"\n" +
                        "}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.Mensaje").value("Error al actualizar los datos de la escuela"))
                .andExpect(jsonPath("$.Error").value("Error al actualizar los datos de la escuela"))
                .andExpect(status().isInternalServerError());

    }

    @Test
    @DisplayName("Eliminar escuela")
    void testDelete()throws  Exception {
        Long id = 1L;

        Mockito.when(schoolService.findById(id)).thenReturn(new School());
        Mockito.doNothing().when(schoolService).delete(id);

        mockMvc.perform(delete("/school/schools/{id}",id))
                .andExpect(jsonPath("$.Mensaje").value("Se elimino la escuela"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Catch error al eliminar escuela")
    void testCatchErrorDeleteSchool()throws Exception{
        Long id = 1L;

        Mockito.doThrow(new DataAccessException("Error al eliminar la escuela") {})
                .when(schoolService).delete(id);

        mockMvc.perform(delete("/school/schools/{id}", id))
                .andExpect(jsonPath("$.Error").value("Error al eliminar la escuela"))
                .andExpect(status().isInternalServerError());
    }
}