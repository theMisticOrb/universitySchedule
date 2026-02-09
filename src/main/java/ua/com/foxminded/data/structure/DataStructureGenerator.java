package ua.com.foxminded.data.structure;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ua.com.foxminded.config.GeneratorConfig;
import ua.com.foxminded.data.generator.AcademicDepartmentGenerator;
import ua.com.foxminded.data.generator.ClassroomGenerator;
import ua.com.foxminded.data.generator.FacultyGenerator;
import ua.com.foxminded.data.generator.GroupGenerator;
import ua.com.foxminded.data.generator.LessonGenerator;
import ua.com.foxminded.data.generator.SpecializationGenerator;
import ua.com.foxminded.data.generator.StudentGenerator;
import ua.com.foxminded.data.generator.SubjectGenerator;
import ua.com.foxminded.data.generator.TeacherGenerator;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Classroom;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Specialization;
import ua.com.foxminded.entity.Subject;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.service.FacultyService;

public class DataStructureGenerator {

    private final FacultyGenerator facultyGen;
    private final AcademicDepartmentGenerator departmentGen;
    private final SpecializationGenerator specializationGen;
    private final GroupGenerator groupGen;
    private final StudentGenerator studentGen;
    private final TeacherGenerator teacherGen;
    private final SubjectGenerator subjectGen;
    private final LessonGenerator lessonGen;
    private final ClassroomGenerator classroomGen;
    private final GeneratorConfig config;

    private final FacultyService facultyService;
    private final Logger log = LoggerFactory.getLogger(DataStructureGenerator.class);

    public DataStructureGenerator(FacultyGenerator facultyGen, AcademicDepartmentGenerator departmentGen,
            SpecializationGenerator specializationGen, GroupGenerator groupGen, StudentGenerator studentGen,
            TeacherGenerator teacherGen, SubjectGenerator subjectGen, LessonGenerator lessonGen,
            ClassroomGenerator classroomGen, GeneratorConfig config, FacultyService facultyService) {
        this.facultyGen = facultyGen;
        this.departmentGen = departmentGen;
        this.specializationGen = specializationGen;
        this.groupGen = groupGen;
        this.studentGen = studentGen;
        this.teacherGen = teacherGen;
        this.subjectGen = subjectGen;
        this.lessonGen = lessonGen;
        this.classroomGen = classroomGen;
        this.config = config;
        this.facultyService = facultyService;
    }

    public void generate() {
        if (!isDatabaseEmpty()) {
            log.info("Database is not empty. Skipping data generation.");
            return;
        }

        log.info("Database empty. Starting data generation...");
        generateAll();
        log.info("Data generation completed successfully.");
    }

    private boolean isDatabaseEmpty() {
        return facultyService.count() == 0;
    }

    public void generateAll() {

        GeneratorConfig count = config;

        List<Faculty> faculties = facultyGen.generateFaculty(count.getFacultyCount());
        faculties.forEach(faculty -> generateFacultyStructure(faculty, count));
    }

    private void generateFacultyStructure(Faculty faculty, GeneratorConfig count) {
        List<AcademicDepartment> departments = departmentGen.generateDepartment(count.getDepartmentsPerFaculty(),
                faculty);
        departments.forEach(department -> generateDepartmentStructure(department, count));
    }

    private void generateDepartmentStructure(AcademicDepartment department, GeneratorConfig count) {
        generateSpecializations(department, count);
        generateTeachers(department, count);
    }

    private void generateSpecializations(AcademicDepartment department, GeneratorConfig count) {
        List<Specialization> specs = specializationGen.generateSpecialization(count.getSpecializationsPerDepartment(),
                department);
        specs.forEach(spec -> generateGroups(spec, count));
    }

    private void generateGroups(Specialization spec, GeneratorConfig count) {
        List<Group> groups = groupGen.generateGroup(count.getGroupsPerSpecialization(), spec);
        groups.forEach(group -> studentGen.generateStudents(count.getStudentsPerGroup(), group));
    }

    private void generateTeachers(AcademicDepartment department, GeneratorConfig count) {
        List<Teacher> teachers = teacherGen.generateTeachers(count.getTeachersPerDepartment(), department);
        teachers.forEach(teacher -> generateTeacherLoad(teacher, count));
    }

    private void generateTeacherLoad(Teacher teacher, GeneratorConfig count) {
        List<Subject> subjects = subjectGen.generateSubjects(count.getSubjectsPerTeacher(), teacher);
        List<Classroom> classrooms = classroomGen.generateClassrooms(count.getClassroomsPerTeacher());
        subjects.forEach(subject -> lessonGen.generateLesson(count.getLessonsPerSubject(), subject, classrooms));
    }

}
