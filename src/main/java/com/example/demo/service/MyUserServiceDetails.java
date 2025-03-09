package com.example.demo.service;

import com.example.demo.entity.PrincipalUser;
import com.example.demo.entity.Users;
import com.example.demo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserServiceDetails implements UserDetailsService {

    private final UserRepo userRepo;

    public MyUserServiceDetails(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            Users user =  userRepo.findByUsername(username);
            if(user == null){
                throw new UsernameNotFoundException("User not found with username: " + username);
            }
            return new PrincipalUser(user);
    }
}
