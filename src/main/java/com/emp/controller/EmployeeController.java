package com.emp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;


import com.emp.dto.EmployeeDto;
import com.emp.dto.EmployeeLoginDto;
import com.emp.dto.OTPRequestDto;



public interface EmployeeController<EmployeeLoginResponseDto> {
	String test();

	ResponseEntity<?> addEmployee(EmployeeDto employeeDto);
	ResponseEntity<?> updateEmployee(EmployeeDto employeeDto);
	public EmployeeLoginResponseDto employeeLogin( EmployeeLoginDto request);
	EmployeeDto getByEmployeeId(long empIdPk);
	public ResponseEntity<?> deleteEmployee(long empIdPk);
	
	public List<EmployeeDto> getAllActivateEmployees();
	public List<EmployeeDto> getAllSpecificEmployees();
	
	ResponseEntity<?> otpsender(OTPRequestDto otpDto);
	
	
	
	
}
