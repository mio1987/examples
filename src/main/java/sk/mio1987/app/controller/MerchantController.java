/**
 * REST controller implementation to provide REST API for merchants management
 * @author Michal Bosiak
 */
package sk.mio1987.app.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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

	/**
	 * Implementation of POST operation
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
	 * @param merchantId
	 * @return merchant by merchantId
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{merchantId}")
	public Merchant getMerchantInfo(@PathVariable("merchantId") String merchantId) {
		return merchantRepository.findOne(merchantId);
	}
	
	/**
	 * Implementation of GET operation without given ID
	 * @return all merchants
	 */
	@RequestMapping(method = RequestMethod.GET)
	public Map<String, Object> getAllMerchants(){
		Iterable<Merchant> merchants = merchantRepository.findAll();
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("merchants", merchants);
		return response;
	}
	
	/**
	 * Deletes merchant by merchantId
	 * @param merchantId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/{merchantId}")
	public Map<String, Object> deleteMerchant(@PathVariable("merchantId") String merchantId){
		merchantRepository.delete(merchantId);
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("msg", "Merchant was deleted successfully.");
		return response;
	}

}
