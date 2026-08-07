package ua.com.foxminded.controller.view.admin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ua.com.foxminded.dto.user.UserCreateUpdateDto;
import ua.com.foxminded.dto.user.UserCreateUpdateListDto;
import ua.com.foxminded.enums.UserRole;
import ua.com.foxminded.repository.AcademicDepartmentRepository;
import ua.com.foxminded.repository.GroupRepository;
import ua.com.foxminded.service.UserService;

@Controller
@RequestMapping("/admin/users")
public class AdminUsersController {

    private final UserService userService;
    private final GroupRepository groupRepository;
    private final AcademicDepartmentRepository departmentRepository;

    public AdminUsersController(UserService userService, GroupRepository groupRepository,
            AcademicDepartmentRepository departmentRepository) {
        this.userService = userService;
        this.groupRepository = groupRepository;
        this.departmentRepository = departmentRepository;
    }

    @GetMapping
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("roles", UserRole.values());
        return "admin/user-edit";
    }

    @PostMapping("/{id}/edit/roles")
    public String changeUserRole(@PathVariable Integer id, @RequestParam Set<UserRole> roles) {
        userService.changeRole(id, roles);
        return "redirect:/admin/users";
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Integer id) {
        userService.deleteById(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/add-user")
    public String showAddUserForm(Model model) {
        model.addAttribute("addUserForm", new UserCreateUpdateDto());
        model.addAttribute("groups", groupRepository.findAll());
        model.addAttribute("departments", departmentRepository.findAll());
        return "admin/add-user";
    }

    @PostMapping("/add-user/batch")
    @ResponseBody
    public ResponseEntity<Map<String, String>> saveBatch(@RequestBody UserCreateUpdateListDto userListRequest) {
        try {
            userService.createUsers(userListRequest.getUsers());
            Map<String, String> success = new HashMap<>();
            success.put("redirect", "/admin/users");
            return ResponseEntity.ok(success);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }

    }

}
