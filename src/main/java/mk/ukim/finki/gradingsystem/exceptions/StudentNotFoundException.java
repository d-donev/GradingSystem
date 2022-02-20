package mk.ukim.finki.gradingsystem.exceptions;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(Integer index) {
        super(String.format("Student with index %d is not found!", index));
    }
}
