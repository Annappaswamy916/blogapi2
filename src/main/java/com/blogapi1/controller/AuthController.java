package com.blogapi1.controller;

import com.blogapi1.dto.JWTAuthResponse;
import com.blogapi1.dto.LoginDto;
import com.blogapi1.dto.SignUpDto;
import com.blogapi1.entity.Role;
import com.blogapi1.entity.User;
import com.blogapi1.repository.RoleRepository;
import com.blogapi1.repository.UserRepository;
import com.blogapi1.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    //http://localhost:8080/api/auth/signup


    //------------------------------------------------------
    //http://localhost:8080/api/auth/signin
    //signin
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // get token form tokenProvider
        String token = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTAuthResponse(token));
    }



    //---------------------------------------------------------

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto){

        if(userRepository.existsByUsername(signUpDto.getUsername())){
            return new ResponseEntity<>("username is already taken", HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(signUpDto.getName())){
            return new ResponseEntity<>("Email is already taken",HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setName(signUpDto.getName());
        user.setEmail(signUpDto.getEmail());
        user.setUsername(signUpDto.getUsername());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        Role roles = roleRepository.findByName("ROLE_ADMIN").get();//In summary, the code snippet retrieves the "ROLE_ADMIN" role from the roleRepository and assigns it as the only role for the user object.


        user.setRoles(Collections.singleton(roles));


        userRepository.save(user);

        return new ResponseEntity<>("User Registered successfully",HttpStatus.OK);
    }

}
