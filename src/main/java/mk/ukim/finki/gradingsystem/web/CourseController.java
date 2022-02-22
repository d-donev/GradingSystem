package mk.ukim.finki.gradingsystem.web;

import mk.ukim.finki.gradingsystem.model.Activity;
import mk.ukim.finki.gradingsystem.model.Course;
import mk.ukim.finki.gradingsystem.model.Student;
import mk.ukim.finki.gradingsystem.service.ActivityService;
import mk.ukim.finki.gradingsystem.service.CourseService;
import mk.ukim.finki.gradingsystem.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final ActivityService activityService;
    private final StudentService studentService;

    public CourseController(CourseService courseService, ActivityService activityService, StudentService studentService) {
        this.courseService = courseService;
        this.activityService = activityService;
        this.studentService = studentService;
    }

    @GetMapping
    public String getCoursesPage(Model model) {
        List<Course> courses = courseService.listAll();
        model.addAttribute("courses", courses);
        return "courses";
    }

    @GetMapping("/{id}")
    public String getCoursePage(@PathVariable Long id, Model model) {
        Course course = courseService.findById(id);
        List<Activity> activityList = course.getActivityList();
        List<Student> studentList = course.getStudentList();
        model.addAttribute("course", course);
        model.addAttribute("activities", activityList);
        model.addAttribute("students", studentList);
        return "currentCourse";
    }

    @GetMapping("/create")
    public String getCreateCoursePage(Model model) {
        List<String> recentYears = new ArrayList<>();
        List<Activity> activityList = activityService.listAll();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String yearFinal;
        for(int i=0; i<5; i++) {
            yearFinal = year + "/" + ++year;
            recentYears.add(yearFinal);
        }
        model.addAttribute("recentYears", recentYears);
        model.addAttribute("activities", activityList);
        model.addAttribute("students", studentService.listAll());
        return "createCourse";
    }

    @PostMapping("/create")
    public String createCourse(@RequestParam String name,
                               @RequestParam String year,
                               @RequestParam List<Long> activityIdList,
                               @RequestParam List<Long> studentIdList) {
        courseService.create(name, year, activityIdList, studentIdList);
        return "redirect:/courses";
    }

    @GetMapping("/{id}/edit")
    public String getEditCoursePage(@PathVariable Long id, Model model) {
        Course course = courseService.findById(id);
        List<String> recentYears = new ArrayList<>();
        List<Activity> activityList = activityService.listAll();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String yearFinal;
        for(int i=0; i<5; i++) {
            yearFinal = year + "/" + ++year;
            recentYears.add(yearFinal);
        }
        model.addAttribute("recentYears", recentYears);
        model.addAttribute("activities", activityList);
        model.addAttribute("students", studentService.listAll());
        model.addAttribute("course", course);
        return "createCourse";
    }

    @PostMapping("/create/{id}")
    public String editCourse(@PathVariable Long id,
                             @RequestParam String name,
                             @RequestParam String year,
                             @RequestParam List<Long> activityIdList,
                             @RequestParam List<Long> studentIdList) {
        courseService.edit(id,name, year, activityIdList, studentIdList);
        return "redirect:/courses";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteCourse(@PathVariable Long id) {
        courseService.delete(id);
        return "redirect:/courses";
    }

    @GetMapping("/addStudentsManual/{id}")
    public String addStudentsManual(@PathVariable Long id, Model model) {
        List<Student> studentList = studentService.listAll();
        List<Student> students = courseService.filterStudentsInCourse(id, studentList);
        model.addAttribute("course", courseService.findById(id));
        model.addAttribute("students", students);
        return "addStudentsManual";
    }

    @PostMapping("/addStudentsManual/{id}")
    public String addStudentsManual(@PathVariable Long id,
                                    @RequestParam List<Integer> studentsId) {
        courseService.addStudentsToCourseManual(id, studentsId);
        return "redirect:/courses/{id}";
    }

    @DeleteMapping("/removeStudentFromCourse/{id}")
    public String removeStudentFromCourse(@PathVariable Integer id,
                                          @RequestParam Long courseId) {
        courseService.removeStudentFromCourse(courseId, id);
        return "redirect:/courses/"+courseId;
    }
}
