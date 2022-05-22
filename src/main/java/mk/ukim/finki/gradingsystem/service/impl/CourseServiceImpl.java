package mk.ukim.finki.gradingsystem.service.impl;

import mk.ukim.finki.gradingsystem.excel.UsersExcelImporter;
import mk.ukim.finki.gradingsystem.exceptions.ActivityNotFoundException;
import mk.ukim.finki.gradingsystem.exceptions.CourseNotFoundException;
import mk.ukim.finki.gradingsystem.model.*;
import mk.ukim.finki.gradingsystem.repositoryJPA.*;
import mk.ukim.finki.gradingsystem.service.CourseService;
import mk.ukim.finki.gradingsystem.service.GradesService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepositoryJPA courseRepositoryJPA;
    private final ActivityRepositoryJPA activityRepositoryJPA;
    private final StudentRepositoryJPA studentRepositoryJPA;
    private final StudentActivityPointsRepositoryJPA studentActivityPointsRepositoryJPA;
    private final GradesRepositoryJPA gradesRepositoryJPA;
    private final GradesService gradesService;

    private final UserRepositoryJPA userRepositoryJPA;

    public CourseServiceImpl(CourseRepositoryJPA courseRepositoryJPA, ActivityRepositoryJPA activityRepositoryJPA, StudentRepositoryJPA studentRepositoryJPA, StudentActivityPointsRepositoryJPA studentActivityPointsRepositoryJPA, GradesRepositoryJPA gradesRepositoryJPA, GradesService gradesService, UserRepositoryJPA userRepositoryJPA) {
        this.courseRepositoryJPA = courseRepositoryJPA;
        this.activityRepositoryJPA = activityRepositoryJPA;
        this.studentRepositoryJPA = studentRepositoryJPA;
        this.studentActivityPointsRepositoryJPA = studentActivityPointsRepositoryJPA;
        this.gradesRepositoryJPA = gradesRepositoryJPA;
        this.gradesService = gradesService;
        this.userRepositoryJPA = userRepositoryJPA;
    }

    @Override
    public List<StudentActivityPoints> getPoints(Long activityId) {
        Long courseId = activityRepositoryJPA.findById(activityId).get().getCourse().getId();
        Course course = courseRepositoryJPA.findById(courseId).orElse(null);
        return null;
    }

    @Override
    public Course findById(Long id) {
        return this.courseRepositoryJPA.findById(id).orElseThrow(() -> new CourseNotFoundException(id));
    }

    @Override
    public List<Course> listAll() {
        List<Course> courses = courseRepositoryJPA.findAll();
        List<Course> reversedList = new ArrayList<>();

        for (int i = courses.size() - 1; i > -1; i--) {
            reversedList.add(courses.get(i));
        }
        return reversedList;
    }

    @Override
    public List<Course> filterCoursesByYear(String year) {
        return this.courseRepositoryJPA.findAll().stream().filter(x -> x.getYear().equals(year)).collect(Collectors.toList());
    }

    @Override
    public List<Course> filterCoursesBySearch(String searchText) {
        return this.courseRepositoryJPA.findAll().stream().filter(x -> x.getName().toLowerCase().contains(searchText.toLowerCase())).collect(Collectors.toList());
    }

    @Override
    public Course create(String name, String year) {
        List<Activity> activityList = new ArrayList<>();
        Course course = new Course(name, year, activityList);
        return this.courseRepositoryJPA.save(course);
    }

    @Override
    public Course edit(Long courseId, String name, String year) {
        Course course = this.findById(courseId);
        course.setName(name);
        course.setYear(year);
        return this.courseRepositoryJPA.save(course);
    }

    @Override
    public Course delete(Long courseId) {
        Course course = this.findById(courseId);
        this.courseRepositoryJPA.delete(course);
        return course;
    }

    @Override
    public Course addActivityToCourse(Long courseId, Long activityId) {
        Course course = this.findById(courseId);
        Activity activity = this.activityRepositoryJPA.findById(activityId).
                orElseThrow(() -> new ActivityNotFoundException(activityId));
        course.getActivityList().add(activity);
        return this.courseRepositoryJPA.save(course);
    }

    @Override
    public List<Student> filterStudentsInCourse(Long courseId, List<Student> studentList) {
        Course c = this.findById(courseId);
        List<Student> students = new ArrayList<>();
        boolean flag = false;
        for (int i = 0; i < studentList.size(); i++) {
            for (int j = 0; j < c.getStudentList().size(); j++) {
                if (studentList.get(i).getIndex().equals(c.getStudentList().get(j).getIndex())) {
                    flag = true;
                }
            }
            if (flag == false)
                students.add(studentList.get(i));
            flag = false;
        }
        return students;
    }

    @Override
    public Course addStudentsToCourseManual(Long courseId, List<Integer> studentsId) {
        Course c = this.findById(courseId);
        List<Student> studentList = studentRepositoryJPA.findAllById(studentsId);
        for (int i = 0; i < studentList.size(); i++) {
            c.getStudentList().add(studentList.get(i));
            Grades g = new Grades(studentList.get(i).getIndex(), courseId, 0.0, 5);
            studentList.get(i).getGrades().add(g);
            gradesRepositoryJPA.save(g);
        }
        courseRepositoryJPA.save(c);
        gradesService.updatePoints(studentList, courseId);
        return c;
    }

    @Override
    public Course removeStudentFromCourse(Long courseId, Integer studentId) {
        Course c = this.findById(courseId);
        Student s = this.studentRepositoryJPA.getById(studentId);
        Grades g = gradesRepositoryJPA.findAll().stream().filter(x -> x.getCourseId().equals(courseId) && x.getIndex().equals(studentId)).findFirst().orElse(null);
        c.getStudentList().removeIf(x -> x.getIndex().equals(studentId));
        s.getGrades().remove(g);
        gradesRepositoryJPA.delete(g);
        return courseRepositoryJPA.save(c);
    }

    @Override
    public List<Student> filterStudents(String searchText, List<Student> studentsInCourse) {
        return studentsInCourse.stream()
                .filter(x -> x.getIndex().toString().contains(searchText) || x.getUser().getName().toLowerCase().contains(searchText.toLowerCase()) || x.getUser().getSurname().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void importStudents(Long courseId, String path) {
        List<Student> allStudents = studentRepositoryJPA.findAll();
        UsersExcelImporter usersExcelImporter = new UsersExcelImporter();
        List<Student> studentsImport = usersExcelImporter.excelStudentsImport(path);
        Course course = courseRepositoryJPA.findById(courseId).orElse(null);
        List<Student> studentsInCourse = course.getStudentList();
        boolean flag = false;
        for (int i = 0; i < studentsImport.size(); i++) {
            flag = false;
            for (int j = 0; j < allStudents.size(); j++) {
                if (studentsImport.get(i).getIndex().equals(allStudents.get(j).getIndex())) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                User user = studentsImport.get(i).getUser();
                Student student = studentsImport.get(i);
                userRepositoryJPA.save(user);
                studentRepositoryJPA.save(student);
            }
        }


        for (int i = 0; i < studentsImport.size(); i++) {
            flag = false;
            for (int j = 0; j < course.getStudentList().size(); j++) {
                if (studentsImport.get(i).getIndex().equals(studentsInCourse.get(j).getIndex())) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                course.getStudentList().add(studentsImport.get(i));
                Grades g = new Grades(studentsImport.get(i).getIndex(), courseId, 0.0, 5);
                Student s = studentRepositoryJPA.findById(studentsImport.get(i).getIndex()).orElse(null);
                s.getGrades().add(g);
                gradesRepositoryJPA.save(g);
            }
        }
        courseRepositoryJPA.save(course);
        gradesService.updatePoints(course.getStudentList(), courseId);
    }
}
