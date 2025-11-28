package FOS_CORE;

public class LoginService implements ILoginService {

    @Override
    public User login(String email, String password) {
        // TODO: Implementation
        return null;
    }

    @Override
    public void logout() {
        // TODO: Implementation
    }

    private boolean validateCredentials(String email, String password) {
        // TODO: Implementation
        return false;
    }

    private User getUserByEmail(String email) {
        // TODO: Implementation
        return null;
    }
}
