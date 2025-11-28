package FOS_CORE;

public interface ILoginService {
    User login(String email, String password);
    void logout();
}