package Hutechlibrary.Anu.Library.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;

import Hutechlibrary.Anu.Library.dto.RegisterRequest;
import Hutechlibrary.Anu.Library.entity.Role;
import Hutechlibrary.Anu.Library.entity.User;
import Hutechlibrary.Anu.Library.repository.RoleRepository;
import Hutechlibrary.Anu.Library.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    private static final Set<String> VALID_ROLES = Set.of("USER", "LIBRARIAN", "ADMIN");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!user.isActivated()) {
            throw new UsernameNotFoundException("Account not activated");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList())
        );
    }

    @Transactional
    public User registerUser(RegisterRequest registerRequest) throws MessagingException {
        String roleInput = registerRequest.getRole().toUpperCase();
        if (!VALID_ROLES.contains(roleInput)) {
            throw new IllegalArgumentException("Invalid role: " + roleInput);
        }

        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setActivated(false);
        user.setActivationToken(UUID.randomUUID().toString());

        String roleName = "ROLE_" + roleInput;
        Role role = roleRepository.findByName(roleName)
                .orElseThrow();

        user.setRoles(Set.of(role));
        user = userRepository.save(user);

        emailService.sendActivationEmail(user.getEmail(), user.getUsername(), user.getActivationToken());

        return user;
    }

    @Transactional
    public void activateUser(String token) {
        User user = userRepository.findByActivationToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid activation token"));
        user.setActivated(true);
        user.setActivationToken(null);
        userRepository.save(user);
    }

	public void deleteUser(String id) {
		// TODO Auto-generated method stub
		
	}

	public Page<User> getAllUsers(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}
}