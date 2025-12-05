package FOS_DATA;

import FOS_CORE.User;

public interface IUserData {
    public User getUserByEmail(String email);
    public boolean changeUserPassword(User user, String newHashedPassword);
}
