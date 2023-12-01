package com.emp.controller.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import com.emp.controller.EmployeeController;
import com.emp.dao.EmployeeDao;
import com.emp.dto.EmployeeDto;
import com.emp.dto.EmployeeLoginDto;
import com.emp.dto.EmployeeLoginResponseDto;
import com.emp.dto.OTPRequestDto;
import com.emp.model.EmployeeModel;
import com.emp.service.EmployeeService;
import com.emp.util.CommonValidation;
import com.emp.util.ResponseCodes;
import com.emp.util.SendEmail;

@RestController
public class EmployeeControllerImpl implements EmployeeController {
	@Autowired
	EmployeeService service;
	
	@Autowired
	EmployeeDao employeeDao;
	@RequestMapping(value = "/test", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String test() {
		return "Tested Okay";
	}
	


	@RequestMapping(value = "/addEmployee", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<?> addEmployee(@RequestBody EmployeeDto employeeDto) {
		Map<String, String> map = null;
		EmployeeModel employee = null;
		
		CommonValidation validation = new CommonValidation();
		if (!validation.validationString(employeeDto.getFirstName())) {
			
			map = new HashMap<String, String>();
			map.put("responseCode", ResponseCodes.ER102.getCode());
			map.put("status", "Fail");
			map.put("responseMsg", ResponseCodes.ER102.getMessage());
			
		} else if (!validation.validationString(employeeDto.getLastName())) {
			map = new HashMap<String, String>();
			map.put("responseCode", ResponseCodes.ER103.getCode());
			map.put("status", "Fail");
			map.put("responseMsg", ResponseCodes.ER103.getMessage());
			
		}else if (validation.validateAmountValues(employeeDto.getSalary())) {
			map = new HashMap<String, String>();
			map.put("responseCode", ResponseCodes.ER102.getCode());
			map.put("status", "Fail");
			map.put("responseMsg", ResponseCodes.ER102.getMessage());
			
		}else if (!validation.validatePassword(employeeDto.getPassword())) {
			map = new HashMap<String, String>();
			map.put("responseCode", ResponseCodes.ER106.getCode());
			map.put("status", "Fail");
			map.put("responseMsg", ResponseCodes.ER106.getMessage());
			
		} else if (!validation.validateEmailValues1(employeeDto.getEmailId())){
			map = new HashMap<String, String>();
			map.put("responseCode", ResponseCodes.ER105.getCode());
			map.put("status", "Fail");
			map.put("responseMsg", ResponseCodes.ER105.getMessage());
			
		} else if (validation.validatePhoneNumber(employeeDto.getMobile())) {
			map = new HashMap<String, String>();
			map.put("responseCode", ResponseCodes.ER104.getCode());
			map.put("status", "Fail");
			map.put("responseMsg", ResponseCodes.ER104.getMessage());
			
		}
		if (employeeDao.getEmployeeByEmail(employeeDto.getEmailId()) != null) {
			map = new HashMap<String, String>();
			map.put("responseCode", ResponseCodes.ER101.getCode());
			map.put("status", "Fail");
			map.put("responseMsg", ResponseCodes.ER101.getMessage());
		} else {
		 service.addEmployee(employeeDto);
		 map = new HashMap<String, String>();
			map.put("responseCode", ResponseCodes.S200.getCode());
			map.put("status", "Success");
			map.put("responseMsg", ResponseCodes.S200.getMessage());
		
		}
		 
		 return ResponseEntity.ok(map);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	@RequestMapping(value = "/updateEmployee", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateEmployee(@RequestBody EmployeeDto employeeDto) {
		Map<String, String> map = null;
		
	
				return service.updateEmployee(employeeDto);
				
	}
	
	
	
	@RequestMapping(value = "/login", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public EmployeeLoginResponseDto employeeLogin(@RequestBody EmployeeLoginDto request){
		Map<String, String> map = null;

		EmployeeModel emp=employeeDao.getEmailAndPassword(request.getEmail(),request. getPassword());
		EmployeeLoginResponseDto Empresponse=new EmployeeLoginResponseDto();
		
		if(emp!=null){
//			SendEmail.sendMails(request.getEmail(), request.getMsg());
			
			Empresponse.setCode( ResponseCodes.S210.getCode());
			
			Empresponse.setStatus("sucess");
			Empresponse.setMessage( ResponseCodes.S210.getMessage());
			
			Empresponse.setEmployeeId(emp.getEmpIdPk());
			Empresponse.setFirstName(emp.getFirstName());
			
			if (emp != null) {

				SimpleMailMessage mailMessage = new SimpleMailMessage();
				mailMessage.setTo(emp.getEmailId());
				mailMessage.setSubject(" login Successful!!");
				mailMessage.setText("Welcome To Beauto Syastem, please click here To generate Password: "
						+ "http://localhost:8080/Employee/update/" + emp.getEmpIdPk());

			
				SendEmail.sendMails(mailMessage);
			
			
		}}else{
	Empresponse.setCode( ResponseCodes.S211.getCode());
			
			Empresponse.setStatus("fail");
			Empresponse.setMessage( ResponseCodes.S211.getMessage());
		}
		return Empresponse;
		
	}
	
	@RequestMapping(value = "/getBYEmployeeID/{empIdPk}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public EmployeeDto getByEmployeeId(@PathVariable("empIdPk") long empIdPk) {
	
		return service.getByEmployee(empIdPk);
	}
	@RequestMapping(value = "/deleteEmployee/{empIdPk}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteEmployee(@PathVariable("empIdPk") long empIdPk) {
		Map<String, String> map = null;
		EmployeeModel emp = null;
	
try{
		 emp = employeeDao.getEmployeeById(empIdPk);
		
		if (emp != null && emp.isStatus() == false) {
			service.deleteEmployee(empIdPk);
			map = new HashMap<String, String>();
			map.put("responseCode", ResponseCodes.S202.getCode());
			map.put("status", "Success");
			map.put("responseMsg", ResponseCodes.S202.getMessage());
		

		} else {
			map = new HashMap<String, String>();
			map.put("responseCode", ResponseCodes.S303.getCode());
			map.put("status", "fail");
			map.put("responseMsg", ResponseCodes.S303.getMessage());

		} }catch (Exception e) {

			map = new HashMap<String, String>();
			map.put("responseCode", ResponseCodes.Ex201.getCode());
			map.put("status", "fail");
			map.put("responseMsg", ResponseCodes.Ex201.getMessage());

		}
		
		
		
		
		return ResponseEntity.ok(map);
	}
	@RequestMapping(value = "/getAllActiveEmployees", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<EmployeeDto> getAllActivateEmployees() {
		 List<EmployeeDto> list=service.getAllEmployeesByStatus();
	
		return list;
	}
	@RequestMapping(value = "/getAllSpacificEmployees", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<EmployeeDto> getAllSpecificEmployees() {
		List<EmployeeDto> list=service.getAllSpacificEmployees(30, (double) 60000, "03");
		
		return list;
	}


//	@RequestMapping(value = "/otpsend", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//	public @ResponseBody ResponseEntity<?> otpsender(@RequestBody OTPRequestDto otpDto) {
//		System.out.println("Before mail :"+otpDto.toString());
//		service.sendOTPEmail(otpDto);
//		System.out.println("After mail :"+otpDto.toString());
//		return null;
//	}
	
	@RequestMapping(value = "/updatepassword", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<?> otpsender(@RequestBody OTPRequestDto otpDto ) {
		Map<String, String> map = null;
		System.out.println("Before mail :"+otpDto.toString());
		service.sendOTPEmail(otpDto);
		System.out.println("After mail :"+otpDto.toString());
		if (employeeDao.getEmployeeByEmail(otpDto.getEmailId()) == null) 
	
		{
			map = new HashMap<String, String>();
			map.put("responseCode", ResponseCodes.Ex201.getCode());
			map.put("status", "Fail");
			map.put("responseMsg", ResponseCodes.Ex201.getMessage());
			ResponseEntity.ok(map);
		}else{
		if (otpDto!=null&&otpDto.getPassword().equalsIgnoreCase(otpDto.getConformpassword())) {
//			if(otpDto.getOtp()==(SendEmail.sendMail(otpDto.getEmailId(),otpDto.otp+("")))	){

			service.sendOTPEmail(otpDto);
			
			map = new HashMap<String, String>();
			map.put("responseCode", ResponseCodes.S205.getCode());
			map.put("status", "Success");
			map.put("responseMsg", ResponseCodes.S205.getMessage());
			ResponseEntity.ok(map);
		}
		else{
			map = new HashMap<String, String>();
			map.put("responseCode", ResponseCodes.S207.getCode());
			map.put("status", "Fail");
			map.put("responseMsg", ResponseCodes.S207.getMessage());
			ResponseEntity.ok(map);
		}}
		 return ResponseEntity.ok(map);
	}
}
