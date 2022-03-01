package mk.ukim.finki.gradingsystem.web;

import mk.ukim.finki.gradingsystem.model.Activity;
import mk.ukim.finki.gradingsystem.model.Course;
import mk.ukim.finki.gradingsystem.service.ActivityService;
import mk.ukim.finki.gradingsystem.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/activity")
public class ActivityController {

    private final ActivityService activityService;
    private final CourseService courseService;

    public ActivityController(ActivityService activityService, CourseService courseService) {
        this.activityService = activityService;
        this.courseService = courseService;
    }

    @GetMapping
    public String getActivityPage(Model model) {

        List<Activity> activities = activityService.listAll();
        model.addAttribute("activities", activities);
        return "activities";
    }

    @GetMapping("/create/{id}")
    public String getCreateActivityPage(Model model, @PathVariable Long id) {

        List<Activity> activities = activityService.listAll();
        Course course = courseService.findById(id);

        model.addAttribute("activities", activities);
        model.addAttribute("course",course);

        return "createActivity";
    }

    @PostMapping("/create/{id}")
    public String createActivity(@RequestParam String name,
                                 @PathVariable Long id,
                                 @RequestParam Double percentage,
                                 @RequestParam Double min) {

        Course course = courseService.findById(id);
        activityService.create(name,course,percentage,min);
        return "redirect:/courses/{id}";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteActivity(@PathVariable Long id) {
        activityService.delete(id);
        return "redirect:/activity";
    }

}
