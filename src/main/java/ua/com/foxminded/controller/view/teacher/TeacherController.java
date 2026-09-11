package ua.com.foxminded.controller.view.teacher;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ua.com.foxminded.dto.lesson.LessonCreateUpdateDto;
import ua.com.foxminded.dto.lesson.LessonResponseDto;
import ua.com.foxminded.entity.Classroom;
import ua.com.foxminded.entity.Lesson;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.enums.ClassHour;
import ua.com.foxminded.enums.WeekDay;
import ua.com.foxminded.mapper.LessonMapper;
import ua.com.foxminded.mapper.TeacherMapper;
import ua.com.foxminded.repository.ClassroomRepository;
import ua.com.foxminded.repository.GroupRepository;
import ua.com.foxminded.repository.SubjectRepository;
import ua.com.foxminded.security.CustomUserDetails;
import ua.com.foxminded.service.LessonService;
import ua.com.foxminded.service.TeacherService;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    private final LessonService lessonService;
    private final SubjectRepository subjectRepository;
    private final ClassroomRepository classroomRepository;
    private final GroupRepository groupRepository;
    private final TeacherService service;
    private final TeacherMapper mapper;
    private final LessonMapper lessonMapper;

    public TeacherController(LessonService lessonService, SubjectRepository subjectRepository,
            ClassroomRepository classroomRepository, GroupRepository groupRepository, TeacherService service,
            TeacherMapper mapper, LessonMapper lessonMapper) {
        this.lessonService = lessonService;
        this.subjectRepository = subjectRepository;
        this.classroomRepository = classroomRepository;
        this.groupRepository = groupRepository;
        this.service = service;
        this.mapper = mapper;
        this.lessonMapper = lessonMapper;
    }

    @GetMapping
    public String listTeachers(Model model) {
        model.addAttribute("teachers",
                service.findAll().stream().map(mapper::toResponseDto).collect(Collectors.toList()));
        return "teacher/list";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Teacher user = userDetails.as(Teacher.class);
        model.addAttribute("currentUser", user);
        return "teacher/dashboard";
    }

    @GetMapping("/schedule")
    public String schedule(Model model, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Teacher user = userDetails.as(Teacher.class);
        Integer teacherId = user.getId();

        model.addAttribute("schedule", lessonService.getWeeklyScheduleForTeacher(teacherId));
        model.addAttribute("weekDays", WeekDay.values());
        model.addAttribute("classHours", ClassHour.values());

        return "teacher/schedule";
    }

    @GetMapping("/schedule/group")
    @ResponseBody
    public Map<String, List<LessonResponseDto>> getGroupSchedule(@RequestParam Integer id) {
        Map<WeekDay, List<Lesson>> schedule = lessonService.getWeeklyScheduleForGroup(id);

        Map<String, List<LessonResponseDto>> result = new LinkedHashMap<>();
        schedule.forEach((day, lessons) -> result.put(day.name(),
                lessons.stream().map(lessonMapper::toResponseDto).collect(Collectors.toList())));
        return result;
    }

    @GetMapping("/schedule/add")
    public String showAddLessonForm(Model model, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        model.addAttribute("subjects", subjectRepository.findByTeacherId(userDetails.getUser().getId()));
        model.addAttribute("classrooms", classroomRepository.findAll());
        model.addAttribute("groups", groupRepository.findAll());
        model.addAttribute("weekDays", WeekDay.values());
        model.addAttribute("classHours", ClassHour.values());
        return "teacher/add-lesson";
    }

    @PostMapping("/schedule/add")
    public String addLesson(@ModelAttribute LessonCreateUpdateDto form, Authentication authentication, Model model) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        try {
            lessonService.addOne(form, userDetails.getUser().getId());
            return "redirect:/teacher/schedule";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("subjects", subjectRepository.findByTeacherId(userDetails.getUser().getId()));
            model.addAttribute("classrooms", classroomRepository.findAll());
            model.addAttribute("groups", groupRepository.findAll());
            model.addAttribute("weekDays", WeekDay.values());
            model.addAttribute("classHours", ClassHour.values());
            return "teacher/add-lesson";
        }
    }

    @GetMapping("/schedule/classrooms/free")
    @ResponseBody
    public List<String> getFreeClassrooms(@RequestParam WeekDay weekDay, @RequestParam ClassHour classHour) {
        Set<Integer> occupiedIds = lessonService.findOccupiedClassroomIds(weekDay, classHour);

        return classroomRepository.findAll().stream().filter(classroom -> !occupiedIds.contains(classroom.getId()))
                .map(Classroom::getName).collect(Collectors.toList());
    }
}
