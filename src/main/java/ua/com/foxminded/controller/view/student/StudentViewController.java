package ua.com.foxminded.controller.view.student;

import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.entity.Student;
import ua.com.foxminded.enums.ClassHour;
import ua.com.foxminded.enums.WeekDay;
import ua.com.foxminded.mapper.StudentMapper;
import ua.com.foxminded.security.CustomUserDetails;
import ua.com.foxminded.service.LessonService;
import ua.com.foxminded.service.StudentService;

@Controller
@RequestMapping("/student")
public class StudentViewController {

    private final StudentService service;
    private final LessonService lessonService;
    private final StudentMapper mapper;

    public StudentViewController(StudentService service, LessonService lessonService, StudentMapper mapper) {
        this.service = service;
        this.lessonService = lessonService;
        this.mapper = mapper;
    }

    @GetMapping
    public String listStudents(Model model) {
        model.addAttribute("students",
                service.findAll().stream().map(mapper::toResponseDto).collect(Collectors.toList()));
        return "student/list";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Student user = userDetails.as(Student.class);
        model.addAttribute("currentUser", user);
        return "student/dashboard";
    }

    @GetMapping("/schedule")
    public String schedule(Model model, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Student user = userDetails.as(Student.class);
        
        if (user.getGroup() == null) {
            model.addAttribute("error", "You are not yet assigned to any group.");
            model.addAttribute("weekDays", WeekDay.values());
            model.addAttribute("classHours", ClassHour.values());
            return "student/schedule-st";
        }
        
        Integer studentGroupId = user.getGroup().getId();

        model.addAttribute("schedule", lessonService.getWeeklyScheduleForGroup(studentGroupId));
        model.addAttribute("weekDays", WeekDay.values());
        model.addAttribute("classHours", ClassHour.values());

        return "student/schedule-st";
    }
}
