package FOS_UI;
// Being done by Umair Ahmad
public final class InputValidator {
    private InputValidator(){}

    public static boolean isNonEmpty(String s){
        return s != null && !s.trim().isEmpty();
    }

    public static boolean isValidEmail(String email){
        if (!isNonEmpty(email)){
            return false;
        }
        return email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
}
