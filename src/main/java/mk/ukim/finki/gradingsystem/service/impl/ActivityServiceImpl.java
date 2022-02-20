package mk.ukim.finki.gradingsystem.service.impl;

import mk.ukim.finki.gradingsystem.exceptions.ActivityNotFoundException;
import mk.ukim.finki.gradingsystem.model.Activity;
import mk.ukim.finki.gradingsystem.repositoryJPA.ActivityRepositortJPA;
import mk.ukim.finki.gradingsystem.service.ActivityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepositortJPA activityRepositortJPA;

    public ActivityServiceImpl(ActivityRepositortJPA activityRepositortJPA) {
        this.activityRepositortJPA = activityRepositortJPA;
    }

    @Override
    public Activity findById(Long id) {
        return this.activityRepositortJPA.findById(id).orElseThrow(() -> new ActivityNotFoundException(id));
    }

    @Override
    public List<Activity> listAll() {
        return this.activityRepositortJPA.findAll();
    }

    @Override
    public Activity create(String name, Double min) {
        Activity activity = new Activity(name, min);
        return this.activityRepositortJPA.save(activity);
    }

    @Override
    public Activity edit(Long code, String name, Double min) {
        Activity activity = this.findById(code);
        activity.setName(name);
        activity.setMinimum(min);
        return this.activityRepositortJPA.save(activity);
    }

    @Override
    public Activity delete(Long code) {
        Activity activity = this.findById(code);
        this.activityRepositortJPA.delete(activity);
        return activity;
    }
}
