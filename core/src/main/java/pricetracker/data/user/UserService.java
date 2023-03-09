package pricetracker.data.user;

import java.security.Principal;
import java.util.Optional;

import pricetracker.dto.user.UserDto;

public interface UserService {

    UserDto getUser(Principal principal);

    Optional<UserDto> getUser(String email);

    void updateUser(UserDto user);

    void saveNewUser(UserDto user);

    boolean isEmailUsed(String email);
}
