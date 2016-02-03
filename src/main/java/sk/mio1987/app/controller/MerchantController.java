/**
 * REST controller implementation to provide REST API for merchants management
 * @author Michal Bosiak
 */
package sk.mio1987.app.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.GeoDistanceFilterBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import sk.mio1987.app.model.Merchant;
import sk.mio1987.app.repository.MerchantRepository;

@RestController
@RequestMapping("/merchant")
public class MerchantController {

	@Autowired
	private MerchantRepository merchantRepository;
	
	@RequestMapping(method = RequestMethod.GET, value = "/findByLocationAndDistance/{latitude}/{longitude}/{distance}")
	public Map<String, Object> searchByLocationAndDistance(@PathVariable("latitude") double latitude, @PathVariable("longitude") double longitude, @PathVariable("distance") double distance){
				
		GeoDistanceFilterBuilder filter = FilterBuilders.geoDistanceFilter("location").point(latitude, longitude).distance(distance, DistanceUnit.KILOMETERS);

		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withFilter(filter)
				.withSort(SortBuilders.geoDistanceSort("location").point(latitude, longitude).order(SortOrder.ASC)).build();
		
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("msg", "Merchant found");
		response.put("merchant", merchantRepository.search(searchQuery));
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/searchByNameOrDesc/{nameOrDesc}")
	public Map<String, Object> findByNameOrDesc(@PathVariable("nameOrDesc") String nameOrDesc){
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("msg", "Search result");
		response.put("merchantsResult", merchantRepository.findByNameContainingOrDescriptionContaining(nameOrDesc, nameOrDesc));
		return response;
		
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/findByName/{nameReq}")
	public Map<String, Object> search(@PathVariable("nameReq") String nameReq){
			
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("msg", "Merchant found");
		response.put("merchantsResult", merchantRepository.findByName(nameReq));
		return response;
	}


	/**
	 * Implementation of POST operation
	 * 
	 * @param merchantMap
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public Map<String, Object> createMerchant(@RequestBody Map<String, Object> merchantMap) {
		Merchant merchant = new Merchant(merchantMap.get("name").toString(), merchantMap.get("description").toString(),
				merchantMap.get("address").toString(), Double.parseDouble(merchantMap.get("latitude").toString()),
				Double.parseDouble(merchantMap.get("longitude").toString()));

		Map<String, Object> response = new LinkedHashMap<>();
		response.put("msg", "Merchant created successfully");
		response.put("merchant", merchantRepository.save(merchant));
		return response;
	}

	/**
	 * Implementation of GET operation for specific ID
	 * 
	 * @param merchantId
	 * @return merchant by merchantId
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{merchantId}")
	public Merchant getMerchantInfo(@PathVariable("merchantId") String merchantId) {
		return merchantRepository.findOne(merchantId);
	}

	/**
	 * Implementation of GET operation without given ID
	 * 
	 * @return all merchants
	 */
	@RequestMapping(method = RequestMethod.GET)
	public Map<String, Object> getAllMerchants() {
		Iterable<Merchant> merchants = merchantRepository.findAll();
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("merchants", merchants);
		return response;
	}

	/**
	 * Implementation of DELETE merchant by merchantId
	 * 
	 * @param merchantId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/{merchantId}")
	public Map<String, Object> deleteMerchant(@PathVariable("merchantId") String merchantId) {
		merchantRepository.delete(merchantId);
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("msg", "Merchant was deleted successfully.");
		return response;
	}

	/**
	 * Implementation of update merchant by Id by PUT operation
	 * @param merchantId
	 * @param merchantMap
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/{merchantId}")
	public Map<String, Object> updateMerchant(@PathVariable("merchantId") String merchantId,
			@RequestBody Map<String, Object> merchantMap) {
		Merchant merchant = new Merchant(merchantMap.get("name").toString(), merchantMap.get("description").toString(),
				merchantMap.get("address").toString(), Double.parseDouble(merchantMap.get("latitude").toString()),
				Double.parseDouble(merchantMap.get("longitude").toString()));
		merchant.setId(merchantId);

		Map<String, Object> response = new LinkedHashMap<String, Object>();
		response.put("msg", "Merchant updated successfully.");
		response.put("merchant", merchantRepository.save(merchant));
		return response;
	}

}
