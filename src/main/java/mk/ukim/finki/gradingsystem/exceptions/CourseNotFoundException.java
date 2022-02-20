package mk.ukim.finki.gradingsystem.exceptions;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(Long courseId) {
        super(String.format("Course with id %d is not found!", courseId));
    }
}
