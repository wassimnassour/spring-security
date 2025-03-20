package com.example.demo.service;

import com.example.demo.entity.PrincipalUser;
import com.example.demo.entity.Users;
import com.example.demo.repo.UserRepo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyUserServiceDetails implements UserDetailsService {

    private final UserRepo userRepo;

    public MyUserServiceDetails(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            Users user =  userRepo.findByUsername(username);
            if(user == null) {
                throw new UsernameNotFoundException(username);
            }

        // Add role-based authorities with proper ROLE_ prefix
        List<GrantedAuthority> grantedAuthorities = user.getRole().stream()
                .map(u -> {
                    String roleName = u.getName().name();
                    return new SimpleGrantedAuthority(roleName.startsWith("ROLE_") ? roleName : "ROLE_" + roleName);
                })
                .collect(Collectors.toList());

        // Add permission-based authorities
        List<GrantedAuthority> permissionAuthorities = user.getPermissions().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        
        grantedAuthorities.addAll(permissionAuthorities);
            return new PrincipalUser(user ,grantedAuthorities);
    }
}
