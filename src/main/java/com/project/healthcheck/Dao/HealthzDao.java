package com.project.healthcheck.Dao;

import java.sql.Connection;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class HealthzDao implements IHealthzDao{
	@Autowired
	private DataSource dataSource;
	
	
	public boolean getHealthz() {
		try(Connection connection = dataSource.getConnection())
		{
			System.out.println(connection);
			System.out.println("Db connection looks fine");
			return true;
		}
		catch(Exception e)
		{
			System.out.print(e.getMessage());
			return false;
		}
	}
	
}
 