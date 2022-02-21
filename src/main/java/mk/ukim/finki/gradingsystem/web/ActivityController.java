package mk.ukim.finki.gradingsystem.web;

import mk.ukim.finki.gradingsystem.model.Activity;
import mk.ukim.finki.gradingsystem.service.ActivityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/activity")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping
    public String getActivityPage(Model model) {

        List<Activity> activities = activityService.listAll();
        model.addAttribute("activities", activities);
        return "activities";
    }

    @GetMapping("/create")
    public String getCreateActivityPage(Model model) {

        List<Activity> activities = activityService.listAll();
        model.addAttribute("activities", activities);

        return "createActivity";
    }

    @PostMapping("/create")
    public String createActivity(@RequestParam String name,
                                 @RequestParam Double min) {

        activityService.create(name,min);
        return "redirect:/activity";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteActivity(@PathVariable Long id) {

        activityService.delete(id);
        return "redirect:/activity";
    }

}
