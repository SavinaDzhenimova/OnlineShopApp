package org.onlineshop.service;

import org.onlineshop.model.entity.Result;
import org.onlineshop.model.entity.User;
import org.onlineshop.model.user.UserDTO;
import org.onlineshop.repository.UserRepository;
import org.onlineshop.service.interfaces.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;

    public UserServiceImpl(UserRepository userRepository, UserDetailsServiceImpl userDetailsService) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void updateUserInfo(UserDTO userDTO) {

        User loggedUser = this.getLoggedUser();

        Optional<User> optionalUserByEmail = this.getUserByEmail(userDTO.getEmail());

        if (!loggedUser.getEmail().equals(userDTO.getEmail()) && optionalUserByEmail.isEmpty()) {
            loggedUser.setEmail(userDTO.getEmail());
        }

        if (!loggedUser.getFullName().equals(userDTO.getFullName())) {
            loggedUser.setFullName(userDTO.getFullName());
        }

        if (!loggedUser.getPhoneNumber().equals(userDTO.getPhoneNumber())) {
            loggedUser.setPhoneNumber(userDTO.getPhoneNumber());
        }

        this.userRepository.saveAndFlush(loggedUser);
    }

    @Override
    public Result sendEmailForForgottenPassword(String email) {

        Optional<User> optionalUser = this.getUserByEmail(email);

        if (optionalUser.isEmpty()) {
            return new Result(false, "Не открихме потребител с посочения от Вас имейл!");
        }

        // TODO: Да се добави логика за изпращане на имейл

        return new Result(true, "Моля проверете пощата си за имейл с временна парола!");
    }

    @Override
    public void refreshAuthentication(String newEmail) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(newEmail);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public UserDTO getLoggedUserInfoForProfilePage() {

        User loggedUser = this.getLoggedUser();

        UserDTO userDTO = new UserDTO();

        userDTO.setFullName(loggedUser.getFullName());
        userDTO.setEmail(loggedUser.getEmail());
        userDTO.setPhoneNumber(loggedUser.getPhoneNumber());

        return userDTO;
    }

    @Override
    public User getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Optional<User> optionalUser = this.getUserByEmail(email);

        if (optionalUser.isEmpty()) {
            return null;
        }

        return optionalUser.get();
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }
}
