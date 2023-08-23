package com.example.Final_Project_9team.controller;

import com.example.Final_Project_9team.dto.*;
import com.example.Final_Project_9team.global.ResponseDto;
import com.example.Final_Project_9team.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.Final_Project_9team.dto.ScheduleListResponseDto;
import com.example.Final_Project_9team.service.ScheduleService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ScheduleService scheduleService;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register(@Valid @RequestBody UserSignupDto dto) {
        userService.registerUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.getMessage("회원가입이 완료되었습니다."));
    }

    // 로그인 및 jwt 발급
    // jwt가 응답 헤더와 body에 중복으로 전달됨 -> 차후 필요에 따라 선택
    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@RequestBody LoginDto dto, HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.login(dto, response));
    }

    // 로그인한 회원 정보 조회
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> readUser(Authentication auth) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.readUser(auth.getName()));
    }


    // 나의 일정 중 날짜 기준으로 목록 조회하기
    @GetMapping("/me/schedules/after-day")
    public List<ScheduleListResponseDto> readSchedulesAfterToday() {

        return scheduleService.readSchedulesAfterToday();

    }
}