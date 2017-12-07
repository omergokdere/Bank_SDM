package Banking;

import Reporting.Visitor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Bank {
	
	private int ID;
	private String name;
	private List<IAccount> accounts = new ArrayList<>();
	//private List<Banking.DebitAccount> debitAccounts=new ArrayList<>();


	public Bank() {
	}

	public Bank(int ID, String name) {
		this.ID = ID;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<IAccount> getAccounts() {
		return accounts;
	}

	public boolean openAccount(String name, Double balance, Boolean debit) {
			
		if(!accounts.isEmpty()) {
			ID++;
		}
		
		if(name.equals(null) || name.isEmpty()) 
			return false;
		
		int accId = accounts.get(accounts.size()-1).getID() + 1;
		Account newAccount = new Account(accId, name, new Date(), balance, debit);
		boolean accCreated=false;
		if (debit) 
		{
			DebitAccount debitAccount = new DebitAccount();
			newAccount.setDebitLimit(300.0);
			debitAccount.setAccount(newAccount);
			accCreated = accounts.add(debitAccount);
		}
		else {
			accCreated = accounts.add(newAccount);
		}
		return accCreated;
	}

	public boolean creditAccount(String name, Double credit) {
		boolean nameFound = false;
		if(credit == 0d)
			return false;
		for (int i=0; i< accounts.size();i++) {
			if(accounts.get(i).getOwner().equals(name)) {
				accounts.get(i).setBalance(accounts.get(i).getBalance() + credit);
				nameFound = true;
				break;
			}
		}
		return nameFound;
	}
	
	public void deposit(int accountNo, Double amount) throws Exception {
		IAccount account = findAccount(accountNo);

		account.deposit(amount, account);
	}

	public IAccount findAccount(int accountNo)throws Exception {
		for (IAccount account : accounts) {
			if (account.getID()==accountNo) {
				return account;
			}
		}

		throw new Exception("Account is not available with provided account no: " + accountNo);
	}

	public void withdraw(int accountNo, Double amount) throws Exception{
		IAccount account = findAccount(accountNo);

		account.withdraw(amount, account);
		/*for (Banking.IAccount account : accounts) {
			if(account.getID()== accountNo) {
				account.withdraw(amount, account);
				*//*if(account.getAccountType()) {
					Banking.DebitAccount dbAcc = new Banking.DebitAccount();
					account = dbAcc.withdraw(amount, account);
				}
				else {
					Banking.Account acc = new Banking.Account();
					account = acc.withdraw(amount, account);
				}*//*
			}
		}*/
	}

	public List<IAccount> doReport(Visitor visitor) {
		List<IAccount> result
				= new ArrayList<IAccount>();
		for (IAccount acc : accounts) {
			IAccount account = acc.accept(visitor);
			if(account!=null)
				result.add(account);
		}
		return result;
	}

	public void doOperation(Operations operations)
	{
		operations.execute();
	}

}
