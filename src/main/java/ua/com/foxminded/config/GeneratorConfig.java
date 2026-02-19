package ua.com.foxminded.config;

import java.util.Locale;
import java.util.Random;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.javafaker.Faker;

@Configuration
@ConfigurationProperties(prefix = "dev")
public class GeneratorConfig {

    private int facultyCount;
    private int departmentsPerFaculty;
    private int specializationsPerDepartment;
    private int groupsPerSpecialization;
    private int studentsPerGroup;
    private int teachersPerDepartment;
    private int subjectsPerTeacher;
    private int classroomsPerTeacher;
    private int lessonsPerSubject;

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
