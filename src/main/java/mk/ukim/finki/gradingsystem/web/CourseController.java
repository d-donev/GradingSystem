package mk.ukim.finki.gradingsystem.web;

import mk.ukim.finki.gradingsystem.model.Activity;
import mk.ukim.finki.gradingsystem.model.Course;
import mk.ukim.finki.gradingsystem.model.Student;
import mk.ukim.finki.gradingsystem.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final ActivityService activityService;
    private final StudentService studentService;
    private final StudentActivityPointsService studentActivityPointsService;


    public CourseController(CourseService courseService, ActivityService activityService, StudentService studentService, StudentActivityPointsService studentActivityPointsService) {
        this.courseService = courseService;
        this.activityService = activityService;
        this.studentService = studentService;
        this.studentActivityPointsService = studentActivityPointsService;
    }

    @GetMapping
    public String getCoursesPage(@RequestParam(required = false) String year,
                                 @RequestParam(required = false) String searchText,
                                 Model model) {
        List<Course> courses = new ArrayList<>();
        List<Course> allCourses = this.courseService.listAll();
        if (year != null && !year.equals("---")) {
            courses = this.courseService.filterCoursesByYear(year);
        } else if (searchText != null && searchText != "") {
            courses = this.courseService.filterCoursesBySearch(searchText);
        } else {
            courses = courseService.listAll();
        }

        List<String> years = new ArrayList<>();
        boolean flag = false;
        for (int i = 0; i < allCourses.size(); i++) {
            for (int j = 0; j < years.size(); j++) {
                if (allCourses.get(i).getYear().equals(years.get(j))) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                years.add(allCourses.get(i).getYear());
            }
            flag = false;
        }
        model.addAttribute("courses", courses);
        model.addAttribute("years", years);
        return "courses";
    }

    @GetMapping("/{id}")
    public String getCoursePage(@PathVariable Long id,
                                Model model,
                                @RequestParam(required = false) String searchText) {
        Course course = courseService.findById(id);
        List<Student> studentList = new ArrayList<>();

        if (searchText == null) {
            studentList = course.getStudentList();
        } else {
            studentList = courseService.filterStudents(searchText, course.getStudentList());
        }

        List<Activity> activityList = course.getActivityList();

        model.addAttribute("course", course);
        model.addAttribute("activities", activityList);
        model.addAttribute("students", studentList);
        model.addAttribute("studentPoints", studentActivityPointsService.findAll());

        return "currentCourse";
    }

    @GetMapping("/create")
    public String getCreateCoursePage(Model model) {
        List<String> recentYears = new ArrayList<>();
        List<Activity> activityList = activityService.listAll();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String yearFinal;
        for (int i = 0; i < 5; i++) {
            yearFinal = year + "/" + ++year;
            recentYears.add(yearFinal);
        }
        model.addAttribute("recentYears", recentYears);
        model.addAttribute("activities", activityList);
        return "createCourse";
    }

    @PostMapping("/create")
    public String createCourse(@RequestParam String name,
                               @RequestParam String year) {
        courseService.create(name, year);
        return "redirect:/courses";
    }

    @GetMapping("/{id}/edit")
    public String getEditCoursePage(@PathVariable Long id, Model model) {
        Course course = courseService.findById(id);
        List<String> recentYears = new ArrayList<>();
        List<Activity> activityList = activityService.listAll();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String yearFinal;
        for (int i = 0; i < 5; i++) {
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
                             @RequestParam String year) {
        courseService.edit(id, name, year);
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
        return "redirect:/courses/" + courseId;
    }
}
