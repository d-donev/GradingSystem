package mk.ukim.finki.gradingsystem.repositoryJPA;

import mk.ukim.finki.gradingsystem.model.StudentActivityPoints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentActivityPointsRepositoryJPA extends JpaRepository<StudentActivityPoints, Long> {
}
