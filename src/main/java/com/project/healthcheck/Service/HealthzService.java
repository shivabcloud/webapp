package com.project.healthcheck.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.healthcheck.Dao.IHealthzDao;

@Service
public class HealthzService implements IHealthzService{
	@Autowired 
	IHealthzDao healthzDao;
	
	public boolean getHealthz() {
		try {
			return healthzDao.getHealthz();
		}
		catch(Exception ex) {
			System.out.print(ex.getMessage());
			return false;
		}
	}
	
}
