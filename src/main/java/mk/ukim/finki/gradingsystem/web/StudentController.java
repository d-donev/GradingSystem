package mk.ukim.finki.gradingsystem.web;

import mk.ukim.finki.gradingsystem.enumerations.Role;
import mk.ukim.finki.gradingsystem.model.Course;
import mk.ukim.finki.gradingsystem.model.Student;
import mk.ukim.finki.gradingsystem.service.CourseService;
import mk.ukim.finki.gradingsystem.service.StudentService;
import mk.ukim.finki.gradingsystem.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final UserService userService;
    private final CourseService courseService;

    public StudentController(StudentService studentService, UserService userService, CourseService courseService) {
        this.studentService = studentService;
        this.userService = userService;
        this.courseService = courseService;
    }

    @GetMapping
    public String getStudentsPage(Model model) {
        List<Student> students = this.studentService.listAll();
        model.addAttribute("students", students);
        return "students";
    }

    @GetMapping("/create")
    public String getCreateStudentsPage(Model model) {
        List<Course> courses = this.courseService.listAll();
        model.addAttribute("courses", courses);
        return "createStudent";
    }

    @PostMapping("/create")
    public String createStudent(@RequestParam Integer index,
                                @RequestParam String name,
                                @RequestParam String surname,
                                @RequestParam Role role,
                                @RequestParam List<Long> courseIdList) {
        this.studentService.createManual(index, name, surname, role, courseIdList);
        return "redirect:/students";
    }

    @GetMapping("/{id}/edit")
    public String getEditStudentPage(@PathVariable Integer id, Model model) {
        Student student = this.studentService.findById(id);
        List<Course> courses = this.courseService.listAll();

        model.addAttribute("student", student);
        model.addAttribute("courses", courses);

        return "createStudent";
    }

    @PostMapping("/create/{id}")
    public String editStudent(@PathVariable Integer id,
                              @RequestParam String name,
                              @RequestParam String surname,
                              @RequestParam List<Long> courseIdList) {
        this.studentService.edit(id, name, surname, courseIdList);
        return "redirect:/students";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteStudent(@PathVariable Integer id) {
        this.studentService.deleteStudent(id);
        return "redirect:/students";
    }
}
