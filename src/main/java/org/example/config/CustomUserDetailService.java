package org.example.config;

import org.example.entity.ProfileEntity;
import org.example.repository.ProfileRepository;
import org.example.entity.ProfileEntity;
import org.example.repository.ProfileRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final ProfileRepository repository;

    public CustomUserDetailService(ProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<ProfileEntity> optional = repository.findByEmailAndVisibleTrue(username);
        if (optional.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        ProfileEntity employee = optional.get();
        return new CustomUserDetail(employee);
    }
}
