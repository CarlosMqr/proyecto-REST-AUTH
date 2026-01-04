package com.course.REST.service;

import com.course.REST.model.Student;
import com.course.REST.repository.StudentRepository;
import com.course.REST.service.StudentServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class TestStudent {

	@InjectMocks
	private StudentServiceImpl studentService;
	@Mock
	private StudentRepository studentRepository;

	@Test
	@DisplayName("Busca por id de estudiante")
	void studentByIdFound(){
		Long id = 1L;
		Student student = new Student();
		student.setId(id);
		student.setName("Carlos");
		student.setLastName("Mendoza");
		student.setRegistered(true);

		when(studentRepository.findById(id)).thenReturn(Optional.of(student));

		Student result = studentService.findById(id);

		assertNotNull(result);
		assertEquals(1L, result.getId());
		assertEquals("Carlos",result.getName());
		assertEquals("Mendoza", result.getLastName());
		assertEquals(true, result.isRegistered());

	}

	@Test
	@DisplayName("Busca por id de estudiante no encontrado")
	void testStudentByIdNotFound(){
		Long id = 1L;

		when(studentRepository.findById(id)).thenReturn(Optional.empty());

		Student result = studentService.findById(id);
  		assertNull(result, "El estudiante debe ser nulo");
	}

	@Test
	@DisplayName("Obtiene todos los estudiantes")
	void testGetAllStudents(){
		Student carlos = new Student();
		carlos.setName("Carlos");
		carlos.setLastName("Mendoza");
		carlos.setRegistered(true);

		Student emily = new Student();
		emily.setName("Emily");
		emily.setLastName("Martinez");
		emily.setRegistered(true);

		when(studentRepository.findAll()).thenReturn(List.of(carlos, emily));
		List<Student> result = studentService.getAll();

		assertNotNull( result);
		assertEquals(2, result.size());
		assertEquals("Carlos", result.get(0).getName());
		assertEquals("Mendoza", result.get(0).getLastName());
		assertEquals(true, result.get(0).isRegistered());

		assertEquals("Emily", result.get(1).getName());
		assertEquals("Martinez", result.get(1).getLastName());
		assertEquals(true, result.get(1).isRegistered());
	}

	@Test
	@DisplayName("Guarda un estudiante")
	void testSaveStudent(){
		Student student = new Student();
		student.setName("Emily");
		student.setLastName("Martinez");
		student.setRegistered(true);

		Mockito.when(studentRepository.save(student)).thenReturn(student);

		Student result = studentService.save(student);

		assertNotNull(result);
		assertEquals("Emily", result.getName());
		assertEquals("Martinez", result.getLastName());
		assertEquals(true, result.isRegistered());
	}

	@Test
	@DisplayName("Actualiza un estudiante")
	void testUpdateStudent(){
		Long id = 1L;
		Student student = new Student();
		student.setId(id);
		student.setName("Emily");
		student.setLastName("Martinez");
		student.setRegistered(false);

		Student updateStudent = new Student();
		updateStudent.setId(id);
		updateStudent.setName("Emilyyyyy");
		updateStudent.setLastName("Martinezzzz");
		updateStudent.setRegistered(true);


		Mockito.when(studentRepository.findById(id)).thenReturn(Optional.of(student));
		Mockito.when(studentRepository.save(Mockito.any(Student.class))).thenReturn(updateStudent);


		Student result = studentService.update(updateStudent, id);

		assertNotNull(result);
		assertEquals(id, result.getId());
		assertEquals("Emilyyyyy", result.getName());
		assertEquals("Martinezzzz", result.getLastName());
		assertEquals(true, result.isRegistered());
	}

	@Test
	@DisplayName("Elimina un estudiante")
	void testDeleteStudent(){
		Student student = new Student();
		student.setId(1L);
		student.setName("Diana");
		student.setLastName("Rios");
		student.setRegistered(true);

		Mockito.when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));
		studentService.delete(student.getId());

		verify(studentRepository, times(1)).deleteById(student.getId());
	}
}
