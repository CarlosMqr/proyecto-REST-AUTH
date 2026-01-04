package com.course.REST;

import com.course.REST.model.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class RestApplication {

	public static void main(String[] args) {

		try {
			ObjectMapper obj = new ObjectMapper();
			Student student = obj.readValue(new File("src\\main\\java\\com\\course\\REST\\data.json"), Student.class);
			System.out.println("id: " + student.getId());
			System.out.println("name: " + student.getName());
			System.out.println("lastName: " + student.getLastName());
			System.out.println("resgistered: " + student.isRegistered());
		}catch (Exception e){
			e.printStackTrace();
		}
		SpringApplication.run(RestApplication.class, args);


	}

}
