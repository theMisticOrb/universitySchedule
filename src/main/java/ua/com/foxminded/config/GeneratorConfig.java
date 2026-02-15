package ua.com.foxminded.config;

import java.util.Locale;
import java.util.Random;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.github.javafaker.Faker;

@Configuration
@Profile("dev")
public class GeneratorConfig {

    private int facultyCount = 2;
    private int departmentsPerFaculty = 2;
    private int specializationsPerDepartment = 3;
    private int groupsPerSpecialization = 5;
    private int studentsPerGroup = 5;
    private int teachersPerDepartment = 5;
    private int subjectsPerTeacher = 3;
    private int classroomsPerTeacher = 3;
    private int lessonsPerSubject = 3;

    @Bean
    public Faker faker() {
        return new Faker(new Locale("en"), new Random(42L));
    }

    public int getFacultyCount() {
        return facultyCount;
    }

    public int getDepartmentsPerFaculty() {
        return departmentsPerFaculty;
    }

    public int getSpecializationsPerDepartment() {
        return specializationsPerDepartment;
    }

    public int getGroupsPerSpecialization() {
        return groupsPerSpecialization;
    }

    public int getStudentsPerGroup() {
        return studentsPerGroup;
    }

    public int getTeachersPerDepartment() {
        return teachersPerDepartment;
    }

    public int getSubjectsPerTeacher() {
        return subjectsPerTeacher;
    }

    public int getClassroomsPerTeacher() {
        return classroomsPerTeacher;
    }

    public int getLessonsPerSubject() {
        return lessonsPerSubject;
    }
    
    

}
