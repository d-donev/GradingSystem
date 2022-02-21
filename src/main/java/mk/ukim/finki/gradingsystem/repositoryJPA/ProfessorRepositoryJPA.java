package mk.ukim.finki.gradingsystem.repositoryJPA;

import mk.ukim.finki.gradingsystem.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorRepositoryJPA extends JpaRepository<Professor, String> {
}
