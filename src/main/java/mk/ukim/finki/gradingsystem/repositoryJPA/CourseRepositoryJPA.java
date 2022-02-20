package mk.ukim.finki.gradingsystem.repositoryJPA;

import mk.ukim.finki.gradingsystem.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepositoryJPA extends JpaRepository<Course, Long> {
}
