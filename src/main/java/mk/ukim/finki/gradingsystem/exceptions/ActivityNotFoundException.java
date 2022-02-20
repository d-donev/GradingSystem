package mk.ukim.finki.gradingsystem.exceptions;

public class ActivityNotFoundException extends RuntimeException {
    public ActivityNotFoundException(Long id) {
        super(String.format("Activity with this id %d is not found", id));
    }
}
