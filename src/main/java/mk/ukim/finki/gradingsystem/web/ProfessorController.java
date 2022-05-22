package mk.ukim.finki.gradingsystem.web;

import mk.ukim.finki.gradingsystem.enumerations.Role;
import mk.ukim.finki.gradingsystem.model.Course;
import mk.ukim.finki.gradingsystem.model.Professor;
import mk.ukim.finki.gradingsystem.model.Student;
import mk.ukim.finki.gradingsystem.service.CourseService;
import mk.ukim.finki.gradingsystem.service.ProfessorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/teachers")
public class ProfessorController {

    private final ProfessorService professorService;
    private final CourseService courseService;

    public ProfessorController(ProfessorService professorService, CourseService courseService) {
        this.professorService = professorService;
        this.courseService = courseService;
    }

    @GetMapping
    public String getProfessorsPage(Model model) {

        List<Professor> professors = professorService.listAll();
        model.addAttribute("professors", professors);

        return "professors";
    }

    @GetMapping("/create")
    public String getCreateProfessorPage(Model model) {

        List<Course> courses = courseService.listAll();
        model.addAttribute("courses",courses);

        return "createProfessor";
    }

    @PostMapping("/create")
    public String createPtofessor(@RequestParam String email,
                                  @RequestParam String name,
                                  @RequestParam String surname,
                                  @RequestParam Role role,
                                  @RequestParam List<Long> courseIdList)
    {
        professorService.create(email, name, surname, role, courseIdList);

        return "redirect:/teachers";
    }

    @GetMapping("/create/{id}")
    public String getEditProfessorPage(@PathVariable String id, Model model) {
        Professor professor = this.professorService.findByEmail(id);

        model.addAttribute("professor", professor);
        model.addAttribute("courses",professor.getUser().getCourseList());
        return "createProfessor";
    }

    @PostMapping("/create/{id}")
    public String editProfessor(@PathVariable String id,
                              @RequestParam String name,
                              @RequestParam String surname) {
        professorService.edit(id,name,surname);

        return "redirect:/teachers";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteStudent(@PathVariable String id) {
        professorService.delete(id);
        return "redirect:/teachers";
    }

}
