package mk.ukim.finki.gradingsystem.service.impl;

import mk.ukim.finki.gradingsystem.exceptions.ActivityNotFoundException;
import mk.ukim.finki.gradingsystem.model.Activity;
import mk.ukim.finki.gradingsystem.repositoryJPA.ActivityRepositoryJPA;
import mk.ukim.finki.gradingsystem.service.ActivityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepositoryJPA activityRepositoryJPA;

    public ActivityServiceImpl(ActivityRepositoryJPA activityRepositoryJPA) {
        this.activityRepositoryJPA = activityRepositoryJPA;
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
    public Activity create(String name, Double min) {
        Activity activity = new Activity(name, min);
        return this.activityRepositoryJPA.save(activity);
    }

    @Override
    public Activity edit(Long code, String name, Double min) {
        Activity activity = this.findById(code);
        activity.setName(name);
        activity.setMinimum(min);
        return this.activityRepositoryJPA.save(activity);
    }

    @Override
    public Activity delete(Long code) {
        Activity activity = this.findById(code);
        this.activityRepositoryJPA.delete(activity);
        return activity;
    }
}
