package ua.com.foxminded.controller.view.teacher;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.dto.lesson.LessonCreateUpdateDto;
import ua.com.foxminded.enums.ClassHour;
import ua.com.foxminded.enums.WeekDay;
import ua.com.foxminded.repository.ClassroomRepository;
import ua.com.foxminded.repository.GroupRepository;
import ua.com.foxminded.repository.SubjectRepository;
import ua.com.foxminded.security.CustomUserDetails;
import ua.com.foxminded.service.LessonService;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    private final LessonService lessonService;
    private final SubjectRepository subjectRepository;
    private final ClassroomRepository classroomRepository;
    private final GroupRepository groupRepository;

    public TeacherController(LessonService lessonService, SubjectRepository subjectRepository,
            ClassroomRepository classroomRepository, GroupRepository groupRepository) {
        this.lessonService = lessonService;
        this.subjectRepository = subjectRepository;
        this.classroomRepository = classroomRepository;
        this.groupRepository = groupRepository;
    }

    
    @GetMapping("/schedule")
    public String schedule(Model model, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer teacherId = userDetails.getUser().getId();

        model.addAttribute("schedule", lessonService.getWeeklyScheduleForTeacher(teacherId));
        model.addAttribute("weekDays", WeekDay.values());
        return "teacher/schedule";
    }
    
    @GetMapping("/schedule/add")
    public String showAddLessonForm(Model model, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        model.addAttribute("subjects", subjectRepository.findByTeacher_Id(userDetails.getUser().getId()));
        model.addAttribute("classrooms", classroomRepository.findAll());
        model.addAttribute("groups", groupRepository.findAll());
        model.addAttribute("weekDays", WeekDay.values());
        model.addAttribute("classHours", ClassHour.values());
        return "teacher/add-lesson";
    }
    
    
    @PostMapping("/schedule/add")
    public String addLesson(@ModelAttribute LessonCreateUpdateDto form,
                            Authentication authentication,
                            Model model) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        try {
            lessonService.addOne(form, userDetails.getUser().getId());
            return "redirect:/teacher/schedule";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("subjects", subjectRepository.findByTeacher_Id(userDetails.getUser().getId()));
            model.addAttribute("classrooms", classroomRepository.findAll());
            model.addAttribute("groups", groupRepository.findAll());
            model.addAttribute("weekDays", WeekDay.values());
            model.addAttribute("classHours", ClassHour.values());
            return "teacher/add-lesson";
        }
    }
    
}
