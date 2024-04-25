package com.luxoft.bankapp.domain;

import java.text.DateFormat;
import java.util.*;

import com.luxoft.bankapp.domain.mail.Email;
import com.luxoft.bankapp.domain.mail.EmailService;
import com.luxoft.bankapp.exceptions.ClientExistsException;
import com.luxoft.bankapp.utils.ClientRegistrationListener;

public class Bank {
	
	private final Set<Client> clients = new HashSet<>();
	private final List<ClientRegistrationListener> listeners = new ArrayList<>();
	
	private int printedClients = 0;
	private int emailedClients = 0;
	private int debuggedClients = 0;

	private EmailService emailService;
	
	public Bank() {
		listeners.add(new PrintClientListener());
		listeners.add(new EmailNotificationListener());
		listeners.add(new DebugListener());
	}

	public Bank(EmailService emailService) {
		this();
		this.emailService = emailService;
	}
	
	public int getPrintedClients() {
		return printedClients;
	}

	public int getEmailedClients() {
		return emailedClients;
	}

	public int getDebuggedClients() {
		return debuggedClients;
	}
	
	public void addClient(final Client client) throws ClientExistsException {
    	if (clients.contains(client)) {
    		throw new ClientExistsException("Client already exists into the bank");
    	} 
    		
    	clients.add(client);
        notify(client);
	}
	
	private void notify(Client client) {
        for (ClientRegistrationListener listener: listeners) {
            listener.onClientAdded(client);
        }
    }
	
	public Set<Client> getClients() {
		return Collections.unmodifiableSet(clients);
	}
	
	class PrintClientListener implements ClientRegistrationListener {
		@Override 
		public void onClientAdded(Client client) {
	        System.out.println("Client added: " + client.getName());
	        printedClients++;
	    }

	}
	
	class EmailNotificationListener implements ClientRegistrationListener {
		@Override 
		public void onClientAdded(Client client) {
	        System.out.println("Notification email for client " + client.getName() + " to be sent");

			if (emailService != null) {
				emailService.sendNotificationEmail(new Email(client, "bank", client.getName(), "Welcome to our bank!", "Welcome to our bank!"));
			}

	        emailedClients++;
	    }
	}
	
	class DebugListener implements ClientRegistrationListener {
        @Override 
        public void onClientAdded(Client client) {
            System.out.println("Client " + client.getName() + " added on: " + DateFormat.getDateInstance(DateFormat.FULL).format(new Date()));
            debuggedClients++;
        }
    }

	public void displayStatistics() {
		BankReport bankReport = new BankReport();

		System.out.println("[statistics] Number of clients: " + bankReport.getNumberOfClients(this));
		System.out.println("[statistics] Number of accounts: " + bankReport.getNumberOfAccounts(this));
		System.out.println("[statistics] Clients sorted by balance: " + bankReport.getClientsSorted(this));
		System.out.println("[statistics] Total amount of credits: " + bankReport.getBankCreditSum(this));
		System.out.println("[statistics] Customer accounts: " + bankReport.getCustomerAccounts(this));
		System.out.println("[statistics] Clients by city: " + bankReport.getClientsByCity(this));
	}
}




