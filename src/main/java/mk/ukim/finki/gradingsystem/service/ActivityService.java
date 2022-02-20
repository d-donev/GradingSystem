package mk.ukim.finki.gradingsystem.service;

import mk.ukim.finki.gradingsystem.model.Activity;

import java.util.List;

public interface ActivityService {
    Activity findById(Long id);

    List<Activity> listAll();

    Activity create(String name, Double min);

    Activity edit(Long code, String name, Double min);

    Activity delete(Long code);
}
