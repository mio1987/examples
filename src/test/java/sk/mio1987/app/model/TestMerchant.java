package sk.mio1987.app.model;
import static org.junit.Assert.*;

import org.junit.Test;

import sk.mio1987.app.model.Merchant;

public class TestMerchant {

	@Test
	public void testMerchant() {
		Merchant tMerchant1 = new Merchant("Miso", "developer", "Popradska 2, 04001 KE", 48.7257854, 21.2353658);
		assertEquals("Miso",tMerchant1.getName());
		assertEquals("developer",tMerchant1.getDescription());
		assertEquals("Popradska 2, 04001 KE",tMerchant1.getAddress());
		assertEquals(48.7257854,tMerchant1.getLatitude(),0.0);
		assertEquals(21.2353658,tMerchant1.getLongitude(),0.0);
		tMerchant1.setId("A01B");
		assertEquals("A01B",tMerchant1.getId());
	}

}
