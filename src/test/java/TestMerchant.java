import static org.junit.Assert.*;

import org.junit.Test;

import sk.mio1987.app.model.Merchant;

public class TestMerchant {

	@Test
	public void testMerchant() {
		Merchant tMerchant1 = new Merchant("Miso", "devloper", "Popradska 2, 04001 KE", 48.7257854, 21.2353658);
		assertEquals("Miso",tMerchant1.getName());
	}

}
