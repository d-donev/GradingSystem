package mk.ukim.finki.gradingsystem.repositoryJPA;

import mk.ukim.finki.gradingsystem.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepositoryJPA extends JpaRepository<Student, Integer> {
}
