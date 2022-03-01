package mk.ukim.finki.gradingsystem.service.impl;

import mk.ukim.finki.gradingsystem.exceptions.ActivityNotFoundException;
import mk.ukim.finki.gradingsystem.exceptions.CourseNotFoundException;
import mk.ukim.finki.gradingsystem.model.Activity;
import mk.ukim.finki.gradingsystem.model.Course;
import mk.ukim.finki.gradingsystem.repositoryJPA.ActivityRepositoryJPA;
import mk.ukim.finki.gradingsystem.repositoryJPA.CourseRepositoryJPA;
import mk.ukim.finki.gradingsystem.service.ActivityService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepositoryJPA activityRepositoryJPA;
    private final CourseRepositoryJPA courseRepositoryJPA;

    public ActivityServiceImpl(ActivityRepositoryJPA activityRepositoryJPA, CourseRepositoryJPA courseRepositoryJPA) {
        this.activityRepositoryJPA = activityRepositoryJPA;
        this.courseRepositoryJPA = courseRepositoryJPA;
    }

    @Override
    public Activity findById(Long id) {
        return this.activityRepositoryJPA.findById(id).orElseThrow(() -> new ActivityNotFoundException(id));
    }

    @Override
    public List<Activity> listAll() {
        return this.activityRepositoryJPA.findAll();
    }

    @Override
    @Transactional
    public Activity create(String name, Course course, Double percentage, Double minimum) {
        Activity activity = new Activity(name, course,percentage,minimum);
        course.getActivityList().add(activity);
        return this.activityRepositoryJPA.save(activity);
    }

    @Override
    public Activity edit(Long code, String name, Long CourseId, Double percentage, Double min) {
        Activity activity = this.findById(code);
        activity.setName(name);
        activity.setMinimum(min);
        Course course = courseRepositoryJPA.findById(CourseId).orElseThrow(() -> new CourseNotFoundException(CourseId));
        activity.setCourse(course);
        activity.setPercentage(percentage);
        return this.activityRepositoryJPA.save(activity);
    }

    @Override
    public Activity delete(Long code) {
        Activity activity = this.findById(code);
        this.activityRepositoryJPA.delete(activity);
        return activity;
    }
}
