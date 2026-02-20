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
    private int classroomsPerDepartment;
    private int lessonsPerSubject;

    @Bean
    public Faker faker() {
        return new Faker(new Locale("en"), new Random(42L));
    }

    public int getFacultyCount() {
        return facultyCount;
    }

    public void setFacultyCount(int facultyCount) {
        this.facultyCount = facultyCount;
    }

    public int getDepartmentsPerFaculty() {
        return departmentsPerFaculty;
    }

    public void setDepartmentsPerFaculty(int departmentsPerFaculty) {
        this.departmentsPerFaculty = departmentsPerFaculty;
    }

    public int getSpecializationsPerDepartment() {
        return specializationsPerDepartment;
    }

    public void setSpecializationsPerDepartment(int specializationsPerDepartment) {
        this.specializationsPerDepartment = specializationsPerDepartment;
    }

    public int getGroupsPerSpecialization() {
        return groupsPerSpecialization;
    }

    public void setGroupsPerSpecialization(int groupsPerSpecialization) {
        this.groupsPerSpecialization = groupsPerSpecialization;
    }

    public int getStudentsPerGroup() {
        return studentsPerGroup;
    }

    public void setStudentsPerGroup(int studentsPerGroup) {
        this.studentsPerGroup = studentsPerGroup;
    }

    public int getTeachersPerDepartment() {
        return teachersPerDepartment;
    }

    public void setTeachersPerDepartment(int teachersPerDepartment) {
        this.teachersPerDepartment = teachersPerDepartment;
    }

    public int getSubjectsPerTeacher() {
        return subjectsPerTeacher;
    }

    public void setSubjectsPerTeacher(int subjectsPerTeacher) {
        this.subjectsPerTeacher = subjectsPerTeacher;
    }

    public int getClassroomsPerDepartment() {
        return classroomsPerDepartment;
    }

    public void setClassroomsPerDepartment(int classroomsPerDepartment) {
        this.classroomsPerDepartment = classroomsPerDepartment;
    }

    public int getLessonsPerSubject() {
        return lessonsPerSubject;
    }

    public void setLessonsPerSubject(int lessonsPerSubject) {
        this.lessonsPerSubject = lessonsPerSubject;
    }


}
