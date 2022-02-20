package mk.ukim.finki.gradingsystem.exceptions;

public class ProfessorNotFoundException extends RuntimeException {
    public ProfessorNotFoundException(String email) {
        super(String.format("Professor with email %d not found!", email));
    }
}
