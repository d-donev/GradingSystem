package mk.ukim.finki.gradingsystem.web;

import mk.ukim.finki.gradingsystem.model.Activity;
import mk.ukim.finki.gradingsystem.model.Course;
import mk.ukim.finki.gradingsystem.service.ActivityService;
import mk.ukim.finki.gradingsystem.service.StudentActivityPointsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/points")
public class StudentActivityPointsController {

    private final StudentActivityPointsService studentActivityPointsService;
    private final ActivityService activityService;

    public StudentActivityPointsController(StudentActivityPointsService studentActivityPointsService, ActivityService activityService) {
        this.studentActivityPointsService = studentActivityPointsService;
        this.activityService = activityService;
    }

    @GetMapping("/{id}")
    public String getPointsPage(@PathVariable Long id, Model model) {
        Activity activity = activityService.findById(id);
        model.addAttribute("activity", activity);
        return "points";
    }
}
