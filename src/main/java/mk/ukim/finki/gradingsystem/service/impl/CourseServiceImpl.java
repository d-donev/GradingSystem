package mk.ukim.finki.gradingsystem.service.impl;

import mk.ukim.finki.gradingsystem.exceptions.ActivityNotFoundException;
import mk.ukim.finki.gradingsystem.exceptions.CourseNotFoundException;
import mk.ukim.finki.gradingsystem.model.Activity;
import mk.ukim.finki.gradingsystem.model.Course;
import mk.ukim.finki.gradingsystem.model.Student;
import mk.ukim.finki.gradingsystem.model.StudentActivityPoints;
import mk.ukim.finki.gradingsystem.repositoryJPA.ActivityRepositoryJPA;
import mk.ukim.finki.gradingsystem.repositoryJPA.CourseRepositoryJPA;
import mk.ukim.finki.gradingsystem.repositoryJPA.StudentActivityPointsRepositoryJPA;
import mk.ukim.finki.gradingsystem.repositoryJPA.StudentRepositoryJPA;
import mk.ukim.finki.gradingsystem.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepositoryJPA courseRepositoryJPA;
    private final ActivityRepositoryJPA activityRepositoryJPA;
    private final StudentRepositoryJPA studentRepositoryJPA;
    private final StudentActivityPointsRepositoryJPA studentActivityPointsRepositoryJPA;

    public CourseServiceImpl(CourseRepositoryJPA courseRepositoryJPA, ActivityRepositoryJPA activityRepositoryJPA, StudentRepositoryJPA studentRepositoryJPA, StudentActivityPointsRepositoryJPA studentActivityPointsRepositoryJPA) {
        this.courseRepositoryJPA = courseRepositoryJPA;
        this.activityRepositoryJPA = activityRepositoryJPA;
        this.studentRepositoryJPA = studentRepositoryJPA;
        this.studentActivityPointsRepositoryJPA = studentActivityPointsRepositoryJPA;
    }

    @Override
    public List<StudentActivityPoints> getPoints(Long activityId)
    {
        Long courseId = activityRepositoryJPA.findById(activityId).get().getCourse().getId();
        List<Activity> courseActivities = courseRepositoryJPA.findById(courseId).get().getActivityList();
        Activity courseActivity = courseActivities.stream().filter(x -> x.getCode().equals(activityId)).findFirst().orElse(null);
        Course course = courseRepositoryJPA.findById(courseId).orElse(null);
        Integer numCourses = course.getStudentList().size();
//        Integer numPoints = activityRepositoryJPA.findById(activityId)
        List<StudentActivityPoints> students = studentActivityPointsRepositoryJPA.findAll().stream()
                .filter(x -> x.getCode().equals(activityId)).collect(Collectors.toList());

//        for (int i=0;i<course.getStudentList().size();i++) {
//            for (int j=0;j<students.size();j++) {
//                if (course.getStudentList().get(i).getIndex().equals(students.get(j).getIndex())) {
//                     courseActivities.
//                }
//            }
//        }

        return null;
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
