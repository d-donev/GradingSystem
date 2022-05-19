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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String getPointsPage(@PathVariable Long id, Model model) {
        Activity activity = activityService.findById(id);
        List<StudentActivityPoints> studentActivityPoints = studentActivityPointsService.findAll();
        model.addAttribute("activity", activity);
        model.addAttribute("points", studentActivityPoints);
        return "points";
    }

    @GetMapping("/import/{id}")
    public String importFile(@PathVariable Long id, @RequestParam String path) {
        PointsExcelImporter excelImporter = new PointsExcelImporter();
        List<StudentActivityPoints> listStudent = excelImporter.excelImport(id, path);
        Activity activity = activityService.findById(id);
        Course c = activity.getCourse();
        List<Student> students = c.getStudentList();
        studentActivityPointsService.saveAll(listStudent);
        gradesService.updatePoints(students, c.getId());
        return "redirect:/points/{id}";
    }
}
