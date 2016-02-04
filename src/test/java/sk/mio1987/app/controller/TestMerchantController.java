package sk.mio1987.app.controller;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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

	// Delta to compare lon/lat
	private static final double DELTA = 1e-15;

	// To generate JSON content from Java objects
	public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	// To delete data added for test
	@Autowired
	private MerchantRepository merchantRepository;

	// Test RestTemplate to invoke the APIs
	private RestTemplate restTemplate = new TestRestTemplate();

	@Test
	public void testCreateMerchant() throws JsonProcessingException {

		// request body data
		Map<String, Object> requestBody = new HashMap<String, Object>();
		requestBody.put("name", "Lenovo");
		requestBody.put("description", "laptops");
		requestBody.put("address", "Moldavska 10, Kosice");
		requestBody.put("latitude", 48.7257854);
		requestBody.put("longitude", 21.2353658);
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);

		// Creating http entity object with request body and headers
		HttpEntity<String> httpEntity = new HttpEntity<String>(OBJECT_MAPPER.writeValueAsString(requestBody),
				requestHeaders);

		// Invoking the API
		Map<String, Object> apiResponse = restTemplate.postForObject("http://localhost:8080/merchant", httpEntity,
				Map.class, Collections.EMPTY_MAP);

		assertNotNull(apiResponse);

		// Asserting the response of the API
		String message = apiResponse.get("msg").toString();
		assertEquals("Merchant created successfully", message);
		String merchantId = ((Map<String, Object>) apiResponse.get("merchant")).get("id").toString();

		assertNotNull(merchantId);

		// Fetching Merchant also from ES repostitory
		Merchant merchantFromEs = merchantRepository.findOne(merchantId);
		assertEquals("Lenovo", merchantFromEs.getName());
		assertEquals("laptops", merchantFromEs.getDescription());
		assertEquals("Moldavska 10, Kosice", merchantFromEs.getAddress());
		assertEquals(48.7257854, merchantFromEs.getLatitude(), DELTA);
		assertEquals(21.2353658, merchantFromEs.getLongitude(), DELTA);

		// Delete the data added for testing
		merchantRepository.delete(merchantId);

	}

	@Test
	public void testGetMerchantInfo() throws JsonProcessingException {
		// Create new Merchant and save it
		Merchant tMerchant3 = new Merchant("Samsung", "TV, phones, laptops, tablets", "Poprad", 49.0588712, 20.2274078);
		merchantRepository.save(tMerchant3);
		
		String merchantId = tMerchant3.getId();

		// Create request body with update Merchant info
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("name", "Samsung");
		requestBody.put("description", "nothing");
		requestBody.put("address", "Poprad");
		requestBody.put("latitude", 49.0588712);
		requestBody.put("longitude", 20.2274078);
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);

		// Create HTTP entity
		HttpEntity<String> httpEntity = new HttpEntity<>(OBJECT_MAPPER.writeValueAsString(requestBody), requestHeaders);

		// Invoking the API
		Map<String, Object> apiResponse = (Map) restTemplate
				.exchange("http://localhost:8080/merchant/" + merchantId, HttpMethod.PUT, httpEntity,
						Map.class, Collections.EMPTY_MAP)
				.getBody();

		// Comparing saved Merchant with response
		assertNotNull(apiResponse);
		assertEquals("Merchant updated successfully.", apiResponse.get("msg"));
		
		Merchant merchantFromES = merchantRepository.findOne(merchantId);  
		assertEquals(requestBody.get("name"), merchantFromES.getName());
		assertEquals(requestBody.get("description"), merchantFromES.getDescription());
		assertEquals(requestBody.get("address"), merchantFromES.getAddress());
		assertEquals(requestBody.get("latitude"), merchantFromES.getLatitude());
		assertEquals(requestBody.get("longitude"), merchantFromES.getLongitude());

		// Delete tested object from ES repository
		merchantRepository.delete(tMerchant3);
	}

	@Test
	public void testUpdateMerchant() {
		// Create new Merchant and save it
		Merchant tMerchant3 = new Merchant("Samsung", "TV, phones, laptops, tablets", "Poprad", 49.0588712, 20.2274078);
		merchantRepository.save(tMerchant3);

		// Invoking the API
		Merchant apiResponse = restTemplate.getForObject("http://localhost:8080/merchant/" + tMerchant3.getId(),
				Merchant.class);

		// Comparing saved Merchant with response
		assertNotNull(apiResponse);
		assertEquals(tMerchant3.getId(), apiResponse.getId());
		assertEquals(tMerchant3.getName(), apiResponse.getName());
		assertEquals(tMerchant3.getDescription(), apiResponse.getDescription());
		assertEquals(tMerchant3.getLatitude(), apiResponse.getLatitude(), DELTA);
		assertEquals(tMerchant3.getLongitude(), apiResponse.getLongitude(), DELTA);

		// Delete tested object from ES repository
		merchantRepository.delete(tMerchant3);
	}
	
	@Test
	public void testDeleteMerchant(){
		// Create new Merchant and save it
		Merchant tMerchant3 = new Merchant("Samsung", "TV, phones, laptops, tablets", "Poprad", 49.0588712, 20.2274078);
		merchantRepository.save(tMerchant3);
		
		String merchantId = tMerchant3.getId();

		// Invoking the API
		restTemplate.delete("http://localhost:8080/merchant/" + merchantId, Collections.EMPTY_MAP);
		
		// Find deleted merchant in ES repository
		assertNull(merchantRepository.findOne(merchantId));

	}

	@Test
	public void testSearchByName() throws JsonProcessingException {
		// Create new Merchant and save it
		Merchant tMerchant2 = new Merchant("Apple", "macbooks, iphones, ipads", "Secovce", 48.7034167, 21.5904493);
		merchantRepository.save(tMerchant2);

		// Invoking the API
		Map<String, Object> apiResponse = restTemplate
				.getForObject("http://localhost:8080/merchant/searchByName/" + tMerchant2.getName(), Map.class);

		List<Map<String, Object>> merchants = (List<Map<String, Object>>) apiResponse.get("merchantsResult");

		assertEquals(1, merchants.size());
		assertEquals("Apple", merchants.get(0).get("name"));

		// Delete the data added for testing
		merchantRepository.delete(tMerchant2.getId());
	}

}
