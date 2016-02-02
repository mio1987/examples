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

		Map<String, Object> response = new LinkedHashMap<String, Object>();
		response.put("message", "Merchant created successfully");
		response.put("merchant", merchantRepository.save(merchant));
		return response;
	}
	
	/**
	 * Implementation of GET operation
	 * @param merchantId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{merchantId}")
	public Merchant getMerchantInfo(@PathVariable("merchantId") String merchantId) {
		return merchantRepository.findOne(merchantId);
	}	

}
