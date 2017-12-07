import Banking.Bank;
import junit.framework.TestCase;
//import static org.junit.jupiter.api


public class TestBank extends TestCase {

	private Bank bank = new Bank();
	public void testOpenAccount() {
		String name = "Hamza";
		Double balance = 900d;
//		assertTrue(bank.openAccount(name, balance));
	}
	
	public void testCreditAccount() {
		String name = "Hamza";
		Double balance = 900d;
//		bank.openAccount(name, balance);
		assertEquals(true, bank.creditAccount(name, 50d));
	}
}
