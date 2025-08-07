package com.abc.hr.app.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import com.abc.hr.app.exception.HrEmployeeDataExtractorException;
import com.abc.hr.app.service.EmployeeDataService;

 @Service
 public class EmployeeDataServiceImpl implements EmployeeDataService {
    
	private static final Logger logger = LoggerFactory.getLogger(EmployeeDataServiceImpl.class);
	
	@Autowired
	private  RestTemplate restTemplate;
	private static final String API_URL = "https://api.first.org/data/v1/countrie?q=";
    
	public String getRegionByCountry(String countryName) throws Exception {
	    String url = API_URL + countryName;

	    try {
	        ResponseExtractor<String> responseExtractor = response -> {
	            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody()));
	            return reader.lines().collect(Collectors.joining());
	        };

	        String response = restTemplate.execute(url, HttpMethod.GET, null, responseExtractor);

	        JSONObject json = new JSONObject(response);
	        JSONObject data = json.getJSONObject("data");

	        for (String key : data.keySet()) {
	            JSONObject countryData = data.getJSONObject(key);
	            if (countryData.getString("country").toLowerCase().contains(countryName.toLowerCase())) {
	                return countryData.getString("region");
	            }
	        }
	        return "null";
	    } catch (Exception e) {
	        logger.error("Error fetching region for country: {}", countryName, e);
	        throw new HrEmployeeDataExtractorException("Failed to fetch region for country: " + countryName, e);
	    }
	}
}
