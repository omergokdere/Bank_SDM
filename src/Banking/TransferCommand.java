package Banking;

public class TransferCommand implements Operations{

	private Account toAccount;
	private Account fromAccount;
	private Double amount;

	public TransferCommand(Account toAccount, Account fromAccount, Double amount) {
		super();
		this.toAccount = toAccount;
		this.fromAccount = fromAccount;
		this.amount = amount;
	}

	@Override
	public void execute() {
		
		if (fromAccount.getBalance()<amount) {
			System.out.println("Insufficient sender balance");
			return;
		}
		fromAccount.setBalance(fromAccount.getBalance() - amount);
		toAccount.setBalance(toAccount.getBalance() + amount);
		
	}
}
