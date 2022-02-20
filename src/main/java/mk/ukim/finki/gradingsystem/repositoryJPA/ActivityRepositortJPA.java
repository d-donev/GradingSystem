package mk.ukim.finki.gradingsystem.repositoryJPA;

import mk.ukim.finki.gradingsystem.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepositortJPA extends JpaRepository<Activity, Long> {
}
