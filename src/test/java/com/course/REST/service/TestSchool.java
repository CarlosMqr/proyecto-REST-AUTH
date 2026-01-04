package com.course.REST.service;

import com.course.REST.model.School;
import com.course.REST.repository.ISchoolRepository;
import com.course.REST.service.SchoolServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;


@SpringBootTest
public class TestSchool {

    @InjectMocks
    private SchoolServiceImpl schoolService;

    @Mock
    private ISchoolRepository schoolRepository;

    @Test
    @DisplayName("Busca por id de escuela")
    void testFoundSchollById(){
        Long id = 1L;
        School school = new School();
        school.setId(id);
        school.setName("UAEM");
        school.setState("Mexico");

        when(schoolRepository.findById(id)).thenReturn(Optional.of(school));
        School result = schoolService.findById(id);

        assertNotNull(result, "La escuela no debe ser nula");
        assertEquals(id, result.getId());
        assertEquals("UAEM", result.getName());
        assertEquals("Mexico", result.getState());
    }

    @Test
    @DisplayName("Busca por id de escuela no encontrada")
    void testNotFoundById(){
        Long id = 1L;

        when(schoolRepository.findById(id)).thenReturn(Optional.empty());

        School result = schoolService.findById(id);

        assertNull(result, "El estudiante debe ser nulo");
    }

    @Test
    @DisplayName("Listar escuelas")
    void testListSchools(){
        School uaem = new School();
        uaem.setId(1L);
        uaem.setName("UAEM");
        uaem.setState("Mexico");

        School lms = new School();
        lms.setId(2L);
        lms.setName("LMS");
        lms.setState("Mexico");

        when(schoolRepository.findAll()).thenReturn(List.of(uaem, lms));

        List<School> result = schoolService.getAll();

        assertNotNull(result);
        assertEquals(1L, result.get(0).getId());
        assertEquals("UAEM", result.get(0).getName());
        assertEquals("Mexico", result.get(0).getState());

        assertEquals(2L, result.get(1).getId());
        assertEquals("LMS", result.get(1).getName());
        assertEquals("Mexico", result.get(1).getState());
    }

    @Test
    @DisplayName("Guardar escuela")
    void testSaveSchool(){
       School saveSchool = new School();
       saveSchool.setId(1L);
       saveSchool.setName("UAEM");
       saveSchool.setState("Mexico");

       when(schoolRepository.save(saveSchool)).thenReturn(saveSchool);

       School result = schoolService.save(saveSchool);

       assertNotNull(result);
       assertEquals(1L, result.getId());
       assertEquals("UAEM", result.getName());
       assertEquals("Mexico", result.getState());
    }

    @Test
    @DisplayName("Actualizar escuela")
    void testUpdateSchool(){
        Long id = 1L;
        School actualSchool = new School();
        actualSchool.setId(id);
        actualSchool.setName("UAEM");
        actualSchool.setState("Mexico");

        School updateSchool = new School();
        updateSchool.setId(id);
        updateSchool.setName("TM");
        updateSchool.setState("Canada");
        when(schoolRepository.findById(id)).thenReturn(Optional.of(actualSchool));
        when(schoolRepository.save(any(School.class))).thenReturn(updateSchool);

        School result = schoolService.update(updateSchool, id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("TM", result.getName());
        assertEquals("Canada", result.getState());
    }

    @Test
    @DisplayName("Eliminar escuela")
    void testDeleteSchool(){
        School school = new School();
        school.setId(100L);
        school.setName("Mause");
        school.setState("Oaxaca");

        when(schoolRepository.findById(school.getId())).thenReturn(Optional.of(school));
        schoolService.delete(school.getId());

        verify(schoolRepository, times(1)).deleteById(school.getId());
    }
}
