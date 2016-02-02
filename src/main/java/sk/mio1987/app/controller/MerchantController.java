/**
 * REST controller implementation to provide REST API for merchants management
 * @author Michal Bosiak
 */
package sk.mio1987.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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

	//first attemp to GET merchant
	@RequestMapping(method = RequestMethod.GET)
	public Merchant getMerchantFirstInfo() {
		merchantRepository.deleteAll();
		return merchantRepository.save(new Merchant("Miso", "developer", "Popradska 2, 04001 KE", 48.7257854, 21.2353658));
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{merchantId}")
	public Merchant getMerchantInfo(@PathVariable("merchantId") String merchantId) {
		return merchantRepository.findOne(merchantId);
	}	

}
