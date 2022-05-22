package mk.ukim.finki.gradingsystem.service;

import mk.ukim.finki.gradingsystem.model.Activity;
import mk.ukim.finki.gradingsystem.model.Course;

import java.util.List;

public interface ActivityService {
    Activity findById(Long id);

    List<Activity> listAll();

    Activity create(String name, Course course, Double percentage, Double min);

    Activity edit(Long code, String name,Long courseId, Double percentage, Double min);

    Activity delete(Long code);

    Activity save (Activity activity);
}
