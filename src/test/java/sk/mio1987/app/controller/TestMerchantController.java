package sk.mio1987.app.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import sk.mio1987.app.Application;
import sk.mio1987.app.model.Merchant;
import sk.mio1987.app.repository.MerchantRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class TestMerchantController {
	
	  //Delta to compare lon/lat 
	  private static final double DELTA = 1e-15;
	
	  //To generate JSON content from Java objects
	  public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	  
	  //To delete data added for test
	  @Autowired
	  private MerchantRepository merchantRepository;
	  
	  //Test RestTemplate to invoke the APIs
	  private RestTemplate restTemplate = new TestRestTemplate();
	  
	  @Test
	  public void testCreateMerchant() throws JsonProcessingException{
	    
	    //request body data
	    Map<String, Object> requestBody = new HashMap<String, Object>();
	    requestBody.put("name", "Lenovo");
	    requestBody.put("description", "laptops");
	    requestBody.put("address", "Moldavska 10, Kosice");
	    requestBody.put("latitude", 48.7257854);
	    requestBody.put("longitude", 21.2353658);
	    HttpHeaders requestHeaders = new HttpHeaders();
	    requestHeaders.setContentType(MediaType.APPLICATION_JSON);

	    //Creating http entity object with request body and headers
	    HttpEntity<String> httpEntity = 
	        new HttpEntity<String>(OBJECT_MAPPER.writeValueAsString(requestBody), requestHeaders);
	    
	    //Invoking the API
	    Map<String, Object> apiResponse = 
	        restTemplate.postForObject("http://localhost:8080/merchant", httpEntity, Map.class, Collections.EMPTY_MAP);

	    assertNotNull(apiResponse);
	    
	    //Asserting the response of the API
	    String message = apiResponse.get("msg").toString();
	    assertEquals("Merchant created successfully", message);
	    String merchantId = ((Map<String, Object>)apiResponse.get("merchant")).get("id").toString();
	    
	    assertNotNull(merchantId);
	    
	    //Fetching Merchant also from ES repostitory
	    Merchant merchantFromEs = merchantRepository.findOne(merchantId);
	    assertEquals("CBA", merchantFromEs.getName());
	    assertEquals("fresh food", merchantFromEs.getDescription());
	    assertEquals("Moldavska 10, Kosice", merchantFromEs.getAddress());
	    assertEquals(48.7257854, merchantFromEs.getLatitude(), DELTA);
	    assertEquals(21.2353658, merchantFromEs.getLongitude(), DELTA);
	    
	    //Delete the data added for testing
	    merchantRepository.delete(merchantId);

	  }

}
