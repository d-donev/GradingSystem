package mk.ukim.finki.gradingsystem.web;

import mk.ukim.finki.gradingsystem.excel.PointsExcelImporter;
import mk.ukim.finki.gradingsystem.model.Activity;
import mk.ukim.finki.gradingsystem.model.StudentActivityPoints;
import mk.ukim.finki.gradingsystem.service.ActivityService;
import mk.ukim.finki.gradingsystem.service.CourseService;
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

    public StudentActivityPointsController(StudentActivityPointsService studentActivityPointsService, ActivityService activityService, CourseService courseService) {
        this.studentActivityPointsService = studentActivityPointsService;
        this.activityService = activityService;
        this.courseService = courseService;
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
        studentActivityPointsService.saveAll(listStudent);
        return "redirect:/points/{id}";
    }
}
