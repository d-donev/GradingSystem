package mk.ukim.finki.gradingsystem.web;

import mk.ukim.finki.gradingsystem.enumerations.Role;
import mk.ukim.finki.gradingsystem.model.Course;
import mk.ukim.finki.gradingsystem.model.Professor;
import mk.ukim.finki.gradingsystem.service.CourseService;
import mk.ukim.finki.gradingsystem.service.ProfessorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

}
