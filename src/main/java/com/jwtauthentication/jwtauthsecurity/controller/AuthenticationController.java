package com.jwtauthentication.jwtauthsecurity.controller;

import com.jwtauthentication.jwtauthsecurity.dto.login.LoginUserDto;
import com.jwtauthentication.jwtauthsecurity.dto.register.RegisterUserDto;
import com.jwtauthentication.jwtauthsecurity.model.User;
import com.jwtauthentication.jwtauthsecurity.response.LoginResponse;
import com.jwtauthentication.jwtauthsecurity.service.authentication.AuthenticationService;
import com.jwtauthentication.jwtauthsecurity.service.jwt.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> register(@RequestBody @Valid RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signUp(registerUserDto, registerUserDto.getRole());
        Map<String, Object> response = createUserResponse(registeredUser);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody @Valid LoginUserDto loginUserDto){
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse()
                .setToken(jwtToken)
                .setExpiresIn(jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }

    private Map<String,Object> createUserResponse(User user){
        Map<String,Object> response = new LinkedHashMap<>();
        response.put("timestamp",LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        response.put("Code",HttpStatus.OK.value());
        response.put("Status",true);
        response.put("message","Signed up successfully");

        Map<String,Object> data = new LinkedHashMap<>();
        data.put("id",user.getUserId());
        data.put("name",user.getFullName());
        data.put("email",user.getEmail());

        response.put("data",data);
        return response;
    }
}
