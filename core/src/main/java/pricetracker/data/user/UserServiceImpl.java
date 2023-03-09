package pricetracker.data.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pricetracker.data.userrole.UserRole;
import pricetracker.data.userrole.UserRoleRepository;
import pricetracker.dto.user.UserDto;
import pricetracker.dto.user.UserDtoConverter;
import pricetracker.dto.userrole.UserRoleEnum;

import java.security.Principal;
import java.util.Optional;

@Component
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository roleRepository;
    private final UserDtoConverter userConverter;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserRoleRepository roleRepository, UserDtoConverter userConverter) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userConverter = userConverter;
    }

    public UserDto getUser(Principal principal) {
        return Optional.ofNullable(principal)
                .map(Principal::getName)
                .map(this::getUser)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .orElseThrow(SecurityException::new);
    }

    @Override
    public Optional<UserDto> getUser(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Null or blank email");
        }

        return userRepository.findByEmail(email).map(userConverter::convertToDto);
    }

    @Override
    @Transactional
    public void updateUser(UserDto user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }

        String email = user.getEmail();
        Optional<User> persistUserOpt = userRepository.findByEmail(email);
        if (persistUserOpt.isEmpty()) {
            throw new IllegalArgumentException(String.format("Can't find user by email = %s", email));
        }

        User persistUser = persistUserOpt.get();
        persistUser.setFirstName(user.getFirstName());
        persistUser.setLastName(user.getLastName());
        persistUser.setPassword(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(user.getPassword()));
        userRepository.save(persistUser);
    }

    @Override
    @Transactional
    public void saveNewUser(UserDto user) {
        if (user == null) {
            throw new IllegalArgumentException("Null user passed!");
        }

        byte roleId = UserRoleEnum.USER.getId();
        Optional<UserRole> role = roleRepository.findById(roleId);
        if (role.isEmpty()) {
            throw new IllegalArgumentException(String.format("Can't find role by id = %d", roleId));
        }

        User newUser = UserBuilder.anUser()
                .withEmail(user.getEmail())
                .withEncryptedPassword(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(user.getPassword()))
                .withFirstName(user.getFirstName())
                .withLastName(user.getLastName())
                .withUserRole(role.get())
                .build();
        userRepository.save(newUser);
    }

    @Override
    public boolean isEmailUsed(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Null or blank email");
        }

        return userRepository.countAllByEmail(email) > 0;
    }
}
