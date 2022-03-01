package mk.ukim.finki.gradingsystem.service.impl;

import mk.ukim.finki.gradingsystem.exceptions.ActivityNotFoundException;
import mk.ukim.finki.gradingsystem.exceptions.CourseNotFoundException;
import mk.ukim.finki.gradingsystem.model.Activity;
import mk.ukim.finki.gradingsystem.model.Course;
import mk.ukim.finki.gradingsystem.model.Student;
import mk.ukim.finki.gradingsystem.repositoryJPA.ActivityRepositoryJPA;
import mk.ukim.finki.gradingsystem.repositoryJPA.CourseRepositoryJPA;
import mk.ukim.finki.gradingsystem.repositoryJPA.StudentRepositoryJPA;
import mk.ukim.finki.gradingsystem.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepositoryJPA courseRepositoryJPA;
    private final ActivityRepositoryJPA activityRepositoryJPA;
    private final StudentRepositoryJPA studentRepositoryJPA;

    public CourseServiceImpl(CourseRepositoryJPA courseRepositoryJPA, ActivityRepositoryJPA activityRepositoryJPA, StudentRepositoryJPA studentRepositoryJPA) {
        this.courseRepositoryJPA = courseRepositoryJPA;
        this.activityRepositoryJPA = activityRepositoryJPA;
        this.studentRepositoryJPA = studentRepositoryJPA;
    }

    @Override
    public Course findById(Long id) {
        return this.courseRepositoryJPA.findById(id).orElseThrow(() -> new CourseNotFoundException(id));
    }

    @Override
    public List<Course> listAll() {
        return this.courseRepositoryJPA.findAll();
    }

    @Override
    public Course create(String name, String year, List<Long> studentList) {
        List<Activity> activityList = new ArrayList<>();
        Course course = new Course(name, year, activityList);
        return this.courseRepositoryJPA.save(course);
    }

    @Override
    public Course edit(Long courseId, String name, String year, List<Long> activities, List<Long> studentList) {
        Course course = this.findById(courseId);
        List<Activity> activityList = this.activityRepositoryJPA.findAllById(activities);

        course.setName(name);
        course.setYear(year);
        course.setActivityList(activityList);
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
        for(int i=0; i<studentList.size(); i++) {
            for(int j=0; j<c.getStudentList().size(); j++) {
                if(studentList.get(i).getIndex().equals(c.getStudentList().get(j).getIndex())) {
                    flag = true;
                }
            }
            if(flag == false)
                students.add(studentList.get(i));
            flag = false;
        }
        return students;
    }

    @Override
    public Course addStudentsToCourseManual(Long courseId, List<Integer> studentsId) {
        Course c = this.findById(courseId);
        List<Student> studentList = studentRepositoryJPA.findAllById(studentsId);
        for(int i=0; i<studentList.size(); i++) {
            c.getStudentList().add(studentList.get(i));
        }
        return courseRepositoryJPA.save(c);
    }

    @Override
    public Course removeStudentFromCourse(Long courseId, Integer studentId) {
        Course c = this.findById(courseId);
        c.getStudentList().removeIf(x -> x.getIndex().equals(studentId));
        return courseRepositoryJPA.save(c);
    }
}
