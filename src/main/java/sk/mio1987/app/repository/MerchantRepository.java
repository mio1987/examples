/**
 * Represents ElasticSearch repository for Merchant
 * @author Michal Bosiak
 */
package sk.mio1987.app.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import sk.mio1987.app.model.Merchant;

public interface MerchantRepository extends ElasticsearchRepository<Merchant, String>{
	
	public List<Object> findByNameContainingOrDescriptionContaining(String name, String description);
	
	public List<Object> findByName(String name);
}
