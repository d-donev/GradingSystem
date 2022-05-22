package mk.ukim.finki.gradingsystem.web;

import mk.ukim.finki.gradingsystem.excel.PointsExcelImporter;
import mk.ukim.finki.gradingsystem.model.Activity;
import mk.ukim.finki.gradingsystem.model.Course;
import mk.ukim.finki.gradingsystem.model.Student;
import mk.ukim.finki.gradingsystem.model.StudentActivityPoints;
import mk.ukim.finki.gradingsystem.service.ActivityService;
import mk.ukim.finki.gradingsystem.service.CourseService;
import mk.ukim.finki.gradingsystem.service.GradesService;
import mk.ukim.finki.gradingsystem.service.StudentActivityPointsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/points")
public class StudentActivityPointsController {

    private final StudentActivityPointsService studentActivityPointsService;
    private final ActivityService activityService;
    private final CourseService courseService;
    private final GradesService gradesService;

    public StudentActivityPointsController(StudentActivityPointsService studentActivityPointsService, ActivityService activityService, CourseService courseService, GradesService gradesService) {
        this.studentActivityPointsService = studentActivityPointsService;
        this.activityService = activityService;
        this.courseService = courseService;
        this.gradesService = gradesService;
    }

    @GetMapping("/{id}")
    public String getPointsPage(@PathVariable Long id,
                                Model model,
                                @RequestParam(required = false) String searchText) {
        Activity activity = activityService.findById(id);
        List<StudentActivityPoints> studentActivityPoints = new ArrayList<>();
        if(searchText == null) {
            studentActivityPoints = studentActivityPointsService.findAll();
        } else{
                studentActivityPoints = studentActivityPointsService.filterStudentActivityPoints(searchText, activity.getCode());
        }
        model.addAttribute("activity", activity);
        model.addAttribute("points", studentActivityPoints);
        return "points";
    }

    @GetMapping("/import/{id}")
    public String importFile(@PathVariable Long id, @RequestParam String path) {
        PointsExcelImporter excelImporter = new PointsExcelImporter();
        List<StudentActivityPoints> listStudent = excelImporter.excelImport(id, path);
        listStudent = studentActivityPointsService.filterDuplicates(listStudent);
        Activity activity = activityService.findById(id);
        Course c = activity.getCourse();
        List<Student> students = c.getStudentList();
        studentActivityPointsService.saveAll(listStudent);
        gradesService.updatePoints(students, c.getId());
        return "redirect:/points/{id}";
    }

    @GetMapping("/{id}/editPoints")
    public String editPoints(@PathVariable Long id, Model model) {
        StudentActivityPoints student = studentActivityPointsService.findById(id);
        model.addAttribute("studentActivityPointsId", id);
        model.addAttribute("currentStudent", student);
        return "editActivityPoints";
    }

    @PostMapping("/{id}/editPoints")
    public String editPoints(@PathVariable Long id, @RequestParam Double points) {
        studentActivityPointsService.editStudentPoints(id, points);
        return "redirect:/points/" + studentActivityPointsService.findById(id).getCode();
    }

    @GetMapping("/activity/edit/{id}")
    public String editActivity(@PathVariable Long id, Model model) {
        Activity activity = activityService.findById(id);
        model.addAttribute("activity", activity);
        return "editActivity";
    }

    @PostMapping("/activity/edit/{id}")
    public String editActivity(@PathVariable Long id,
                               @RequestParam String name,
                               @RequestParam Double percentage,
                               @RequestParam Double min) {
        Activity activity = activityService.findById(id);
        activity.setName(name);
        activity.setPercentage(percentage);
        activity.setMinimum(min);
        activityService.save(activity);
        List<Student> students = activity.getCourse().getStudentList();
        Long courseId = activity.getCourse().getId();
        gradesService.updatePoints(students,courseId);
        return "redirect:/points/" + id;
    }
}
