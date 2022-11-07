package com.hermes.hermesapi.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.hermes.hermesapi.entity.User;
import com.hermes.hermesapi.models.AuthModel;
import com.hermes.hermesapi.models.AuthResponseModel;
import com.hermes.hermesapi.models.ExceptionModel;
import com.hermes.hermesapi.service.ConfirmationTokenService;
import com.hermes.hermesapi.service.UserService;
import com.hermes.hermesapi.config.JwtTokenUtil;
import com.hermes.hermesapi.entity.ConfirmationToken;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;
    private final JwtTokenUtil jwtTokenUtil;
    private AuthenticationManager authenticationManager;

    @PostMapping(path = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> post(@Valid @RequestBody User user) {

        User createdUser = userService.signUpUser(user);

        if (createdUser == null) {
            return new ResponseEntity<>(new ExceptionModel("User already registered", 409), null, 409);
        }

        return ResponseEntity.ok(createdUser);
    }

    @GetMapping("/register/confirm")
    public String confirmToken(@RequestParam("token") String token) {

        Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokenService
                .findConfirmationTokenByToken(token);

        optionalConfirmationToken.ifPresent(userService::confirmUser);

        return "Token confirmed with success!";
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthModel request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        final UserDetails userDetails = userService.loadUserByUsername(request.getUsername());
        final String jwtToken = jwtTokenUtil.generateToken(userDetails);

        if(!userDetails.isEnabled()){
            return new ResponseEntity<>(new ExceptionModel("User not found", 400), null, 400);
        }
        else{
            return ResponseEntity.ok(new AuthResponseModel(jwtToken));
        }
    }

}
