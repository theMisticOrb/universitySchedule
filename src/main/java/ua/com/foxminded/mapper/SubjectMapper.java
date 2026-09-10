package ua.com.foxminded.mapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import ua.com.foxminded.dto.subject.SubjectCreateUpdateDto;
import ua.com.foxminded.dto.subject.SubjectResponseDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Subject;
import ua.com.foxminded.entity.Teacher;

@Component
public class SubjectMapper {

    public Subject toEntity(SubjectCreateUpdateDto dto, List<Teacher> teachers, AcademicDepartment department) {
        Objects.requireNonNull(dto, "SubjectCreateUpdateDto must not be null!");
        Objects.requireNonNull(teachers, "Teacher must not be null!");
        Objects.requireNonNull(department, "AcademicDepartment must not be null!");

        Subject entity = new Subject();

        entity.setTeachers(teachers);
        entity.setTitle(dto.getTitle());
        entity.setDepartment(department);
        teachers.forEach(teacher -> teacher.getSubjects().add(entity));

        return entity;

    }

    public SubjectResponseDto toResponseDto(Subject entity) {
        Objects.requireNonNull(entity, "Subject must not be null!");

        SubjectResponseDto dto = new SubjectResponseDto();
        dto.setId(entity.getId());

        List<Teacher> teacherList = entity.getTeachers();

        dto.setTeacherEmails(teacherList.stream().map(Teacher::getEmail).collect(Collectors.toList()));
        dto.setTeacherFullNames(
                teacherList.stream().map(t -> t.getFirstName() + " " + t.getMiddleName() + " " + t.getLastName())
                        .collect(Collectors.toList()));
        dto.setTitle(entity.getTitle());
        dto.setDepartmentId(entity.getDepartment().getId());
        return dto;
    }
}
