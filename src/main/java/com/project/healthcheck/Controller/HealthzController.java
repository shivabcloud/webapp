package com.project.healthcheck.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.healthcheck.Service.IHealthzService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class HealthzController {
	@Autowired
	IHealthzService healthzService;
	
	
	
	private ResponseEntity<Void> responseHandling(HttpStatusCode code){
		return ResponseEntity.status(code)
				.cacheControl(CacheControl.noCache())
				.build();
	}
	
	@GetMapping("/healthz")
	public ResponseEntity<Void> getHealthz(HttpServletRequest request) {
		try {
			if(request.getContentLength()>0 || request.getQueryString()!=null)
			{
				return responseHandling(HttpStatus.BAD_REQUEST);
			}
			if(healthzService.getHealthz()) {
				return responseHandling(HttpStatus.OK);
			}
			return responseHandling(HttpStatus.SERVICE_UNAVAILABLE);
			
		}
		catch(Exception ex) {
			System.err.print(ex.getMessage());
			return responseHandling(HttpStatus.SERVICE_UNAVAILABLE);
		}
	}
	
	@PostMapping("/healthz")
	public ResponseEntity<Void> postHealthz(){
		return responseHandling(HttpStatus.METHOD_NOT_ALLOWED);
	}
	
	@PutMapping("/healthz")
	public ResponseEntity<Void> putHealthz(){
		return responseHandling(HttpStatus.METHOD_NOT_ALLOWED);
	}
	
	@DeleteMapping("/healthz")
	public ResponseEntity<Void> deleteHealthz(){
		return responseHandling(HttpStatus.METHOD_NOT_ALLOWED);
	}
	
	@PatchMapping("/healthz")
	public ResponseEntity<Void> patchHealthz(){
		return responseHandling(HttpStatus.METHOD_NOT_ALLOWED);
	}
}
