package ua.com.foxminded.data.structure;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

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
import ua.com.foxminded.dto.user.TeacherCreateUpdateDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Classroom;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Specialization;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.enums.UserRole;
import ua.com.foxminded.repository.AcademicDepartmentRepository;
import ua.com.foxminded.repository.UserRepository;
import ua.com.foxminded.service.FacultyService;
import ua.com.foxminded.service.TeacherService;

@Component
@Profile("dev")
public class DataStructureGenerator {

    @Value("${dev.admin.email}")
    private String adminEmail;

    @Value("${dev.admin.password}")
    private String adminPassword;

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

    private final UserRepository userRepository;
    private final AcademicDepartmentRepository departmentRepository;
    private final TeacherService teacherService;

    private final FacultyService facultyService;
    private final Logger log = LoggerFactory.getLogger(DataStructureGenerator.class);

    private List<Classroom> classrooms;

    public DataStructureGenerator(String adminEmail, String adminPassword, FacultyGenerator facultyGen,
            AcademicDepartmentGenerator departmentGen, SpecializationGenerator specializationGen,
            GroupGenerator groupGen, StudentGenerator studentGen, TeacherGenerator teacherGen,
            SubjectGenerator subjectGen, LessonGenerator lessonGen, ClassroomGenerator classroomGen,
            GeneratorConfig config, UserRepository userRepository, AcademicDepartmentRepository departmentRepository,
            TeacherService teacherService, FacultyService facultyService, List<Classroom> classrooms) {
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
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
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.teacherService = teacherService;
        this.facultyService = facultyService;
        this.classrooms = classrooms;
    }

    @Transactional
    public void generate() {
        if (!isDatabaseEmpty()) {
            log.info("Database is not empty. Skipping data generation.");
            return;
        }
        log.info("Database empty. Starting data generation...");

        generateAll();
        addAdmin();
        log.info("Data generation completed successfully.");

    }

    public void generateAll() {

        classrooms = classroomGen.generateClassrooms(config.getClassroomsPerDepartment());

        List<Faculty> faculties = facultyGen.generateFaculties(config.getFacultyCount());
        faculties.forEach(this::generateFacultyStructure);

    }

    private void generateFacultyStructure(Faculty faculty) {
        List<AcademicDepartment> departments = departmentGen.generateDepartment(config.getDepartmentsPerFaculty(),
                faculty);
        departments.forEach(this::generateDepartmentStructure);
    }

    private void generateDepartmentStructure(AcademicDepartment department) {

        List<Teacher> teachers = generateTeachers(department);
        teachers.forEach(teacher -> subjectGen.generateSubjects(config.getSubjectsPerTeacher(), teacher, department));

        List<Specialization> specializations = generateSpecializations(department);
        specializations.forEach(specialization -> {

            List<Group> groups = groupGen.generateGroups(config.getGroupsPerSpecialization(), specialization);
            groups.forEach(group -> {
                studentGen.generateStudents(config.getStudentsPerGroup(), group);
                lessonGen.generateLessons(config.getLessonsPerWeek(), group, teachers, classrooms);
            });

        });

    }

    private List<Specialization> generateSpecializations(AcademicDepartment department) {
        return specializationGen.generateSpecialization(config.getSpecializationsPerDepartment(), department);
    }

    private List<Teacher> generateTeachers(AcademicDepartment department) {
        return teacherGen.generateTeachers(config.getTeachersPerDepartment(), department);
    }

    private boolean isDatabaseEmpty() {
        return facultyService.count() == 0;
    }

    private void addAdmin() {
        if (userRepository.findByEmail(adminEmail).isPresent()) {
            return;
        }
        TeacherCreateUpdateDto adminDto = new TeacherCreateUpdateDto();

        AcademicDepartment anyDepartment = departmentRepository.findAll().stream().findAny()
                .orElseThrow(() -> new IllegalArgumentException("Departments must be generated before admin!"));
        
        adminDto.setDepartmentCode(anyDepartment.getCode());
        adminDto.setEmail(adminEmail);
        adminDto.setPassword(adminPassword);
        adminDto.setFirstName("Admin");
        adminDto.setLastName("Admin");

        Set<UserRole> roles = new HashSet<>();
        roles.add(UserRole.ADMIN);
        roles.add(UserRole.TEACHER);

        adminDto.setUserRoles(roles);

        teacherService.addOne(adminDto);
    }
}
