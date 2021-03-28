package com.pedro.auth.controller;

import com.pedro.auth.entity.User;
import com.pedro.auth.jwt.JWTTokenProvider;
import com.pedro.auth.repository.UserRepository;
import com.pedro.auth.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/login")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JWTTokenProvider jwtTokenProvider;

    private final UserRepository userRepository;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JWTTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @RequestMapping("/testeSecurity")
    public String teste() {
        return "Testado";
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody UserVO userVO) {

        try {
            String userName = userVO.getUserName();
            String password = userVO.getPassword();

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));

            User user = userRepository.findByUserName(userName);

            String token = "";

            if (!Objects.isNull(user)) {
                token = jwtTokenProvider.createToken(userName, user.getRoles());
            } else {
                throw new UsernameNotFoundException("User not found");
            }

            Map<Object, Object> model = new HashMap<>();
            model.put("username", userName);
            model.put("token", token);

            return ok(model);
        } catch (AuthenticationException authenticationException) {
            authenticationException.printStackTrace();
            throw new BadCredentialsException("Invalid Username or Password");
        }
    }
}
