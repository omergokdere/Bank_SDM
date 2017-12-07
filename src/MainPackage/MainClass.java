package MainPackage;

import Banking.Bank;
import Banking.IAccount;
import Banking.TransferCommand;
import Mediator.BanksMediator;
import Reporting.GetAllAccounts;
import Reporting.Over1000Report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import Banking.*;


public class MainClass {

	private static BanksMediator banksMediator = new BanksMediator();
	
	public static void main(String[] args) {

		prepareBanks();

		mainMenu();
	}

	private static void mainMenu() {
		System.out.println("Select a bank from following");
		System.out.println("Press '1' for WBK");
		System.out.println("Press '2' for MCB");
		System.out.println("Press '0' to exit");
		int option = new Scanner(System.in).nextInt();
		if(option == 1)
			bankMenu(banksMediator.getBankList().get(0));
		else if(option == 2)
			bankMenu(banksMediator.getBankList().get(1));
		else
			return;

		mainMenu();

	}

	private static void prepareBanks() {
		banksMediator.getBankList().add(createBankA());
		banksMediator.getBankList().add(createBankB());
	}

	private static Bank createBankA() {
		Bank bankA = new Bank(1, "WBK");
		bankA.getAccounts().add(new Account(1, "Hamza", new Date(), 8500.00, false));
		bankA.getAccounts().add(new Account(2, "Rohail", new Date(), 1200.00, false));
		bankA.getAccounts().add(new DebitAccount(new Account(3, "Omer", new Date(), 1500.00, true, 500.00)));
		return bankA;
	}

	private static Bank createBankB() {
		Bank bankA = new Bank(2, "MCB");
		bankA.getAccounts().add(new Account(1, "Ahmad", new Date(), 1000.00, false));
		bankA.getAccounts().add(new Account(2, "Hafiz", new Date(), 1800.00, false));
		bankA.getAccounts().add(new DebitAccount(new Account(3, "Hassan", new Date(), 8000.00, true, 500.00)));
		return bankA;
	}

	private static void bankMenu(Bank bank) {
		
		int option;

		System.out.println("1. Open an account");
		System.out.println("2. Offer credit to an account");
		System.out.println("3. Make transfer to an account");
		System.out.println("4. Withdraw money");
		System.out.println("5. View Over 1000 report");
		System.out.println("6. View Accounts");
		System.out.println("7. Inter Banking transfer");

		option = new Scanner(System.in).nextInt();
		List<IAccount> resultList = new ArrayList<>();

		try {
			switch (option) {
				case 1:
					System.out.println("Enter Owner Name");
					String name = new Scanner(System.in).next();

					System.out.println("Enter balance");
					Double balance = new Scanner(System.in).nextDouble();

					System.out.println("Would you like to open debit account? (y/n)");
					String type = new Scanner(System.in).next();

					if (bank.openAccount(name, balance, type.equals("y") || type.equals("Y") ? true : false)) {
						System.out.println("Banking.Account has been created successfully");
					}

					break;

				case 2:
					System.out.println("Enter the amount of credit");
					Double credit;
					credit = Double.parseDouble(System.console().readLine());

					System.out.println("Enter owner name of account");
					name = System.console().readLine();

					if (bank.creditAccount(name, credit)) {
						System.out.println("Banking.Account has been created successfully");
					}
					break;

				case 3:
					System.out.println("Enter sender account number");
					int senderAccount = -1, receiverAccount = -1;
					Double amount;

					Scanner in = new Scanner(System.in);

					senderAccount = in.nextInt();
					System.out.println("Enter receiver account number");

					receiverAccount = in.nextInt();

					System.out.println("Enter amount");
					amount = in.nextDouble();

					IAccount recAcc = bank.findAccount(receiverAccount);
					if (recAcc.equals(null)) {
						System.out.println("Receiver Banking.Account not found");
						break;
					}
					IAccount sendAcc = bank.findAccount(senderAccount);
					if (sendAcc.equals(null)) {
						System.out.println("Sender Banking.Account not found");
						break;
					}

					TransferCommand transfer = new TransferCommand(recAcc.getAccount(), sendAcc.getAccount(), amount);

					bank.doOperation(transfer);

					break;

				case 4:

					System.out.println("Enter account number");
					int accountNo = -1;

					in = new Scanner(System.in);
					accountNo = in.nextInt();

					System.out.println("Enter amount");
					amount = in.nextDouble();

					bank.withdraw(accountNo, amount);
					break;

				case 5:

					resultList = bank.doReport(new Over1000Report());
					for (IAccount acc : resultList ) {
						System.out.println("Banking.Account No: " + acc.getID() + "\n" +
								"Owner Name: "+acc.getOwner() + "\n" +
								"Balance: " + acc.getBalance());
					}
					break;

				case 6:
					resultList = bank.doReport(new GetAllAccounts());
					for (IAccount acc : resultList ) {
						System.out.println("Banking.Account No: " + acc.getID() + "\n" +
								"Owner Name: "+acc.getOwner() + "\n" +
								"Balance: " + acc.getBalance());
						if (acc.getAccountType()) {
							System.out.println("Debit Limit: " + acc.getDebitLimit());
						}
					}
					break;

				case 7:
					System.out.println("Enter the Receiver Banking name");
					String bankName;
					in = new Scanner(System.in);
					bankName = in.next();

					System.out.println("Enter the Sender Account number");
					int fromAccNo;
					//in = new Scanner(System.in);
					fromAccNo = in.nextInt();

					System.out.println("Enter the Receiver Account number");
					int toAccNo;
					//in = new Scanner(System.in);
					toAccNo = in.nextInt();

					System.out.println("Enter the Amount to transfer");
					//in = new Scanner(System.in);
					amount = in.nextDouble();

					//First deduct money from sender's account
					bank.withdraw(fromAccNo, amount);

					//then forward call to moderator with bank name, acc no and amount to add money to that account
					try {
						banksMediator.eft(bankName, toAccNo, amount);
					}catch (Exception e) {
						bank.deposit(fromAccNo, amount);
						throw e;
					}

					//id successful then return otherwise add money to sender account


					/*EFTCommand eftCommand = new EFTCommand();
					eftCommand.setToBankName(bankName);
					eftCommand.setFromBankName(bank.getName());
					eftCommand.setAmount(amount);
					banksMediator.eft(bankName, accNo, amount, eftCommand);*/

					break;

				default:
					System.out.println("Please select a valid option");
					break;

			}
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		BankMenuAgain(bank);
	}

	private static void BankMenuAgain(Bank bank) {
		System.out.println("Do you want to see main menu again? Press 'Y' or 'N'");
		String menu = new Scanner(System.in).next();
		if(menu.equals("Y") || menu.equals("y")){
			bankMenu(bank);
		}

	}

}
