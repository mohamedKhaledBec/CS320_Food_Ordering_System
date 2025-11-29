package FOS_CORE;

import FOS_DATA.IUserData;
import FOS_DATA.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;

public class LoginService implements ILoginService {
    private final IUserData userData = new UserData();
    @Override
    public User login(String email, String password) {
        if (!validateCredentials(email, password)) {
            return null;
        }
        User user = getUserByEmail(email);
        if (user == null) {
            return null;
        }
        if (!verifyPassword(user, password)) {
            return null;
        }
        return user;
    }

    @Override
    public void logout() {
        // TODO: Implementation
    }

    private boolean validateCredentials(String email, String password) {
        return email != null && !email.isEmpty() && validateEmailFormat(email)
                && password != null && !password.isEmpty();
    }
    private boolean validateEmailFormat(String email) {
        return email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    private User getUserByEmail(String email) {
        return userData.getUserByEmail(email);
    }
    private boolean verifyPassword(User user, String password) {
        return PasswordUtils.verifyPassword(password, user.getPasswordHash());
    }
}
