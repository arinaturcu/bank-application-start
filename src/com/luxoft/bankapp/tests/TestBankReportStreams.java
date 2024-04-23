package com.luxoft.bankapp.tests;

import com.luxoft.bankapp.domain.*;
import com.luxoft.bankapp.exceptions.ClientExistsException;
import com.luxoft.bankapp.service.BankService;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestBankReportStreams {

    @Test
    public void testGetNumberOfClients() throws ClientExistsException {
        Bank bank = new Bank();
        BankReportStreams bankReportStreams = new BankReportStreams();

        Client client1 = new Client("Smith John", Gender.MALE, "New York");
        client1.addAccount(new SavingAccount(1, 1000.0));
        client1.addAccount(new CheckingAccount(2, 1000.0, 100.0));

        Client client2 = new Client("Smith Michelle", Gender.FEMALE, "Los Angeles");
        client2.addAccount(new SavingAccount(3, 2000.0));
        client2.addAccount(new CheckingAccount(4, 1500.0, 200.0));

        Client client3 = new Client("Doe Jane", Gender.FEMALE, "New York");
        client3.addAccount(new SavingAccount(5, 3000.0));
        client3.addAccount(new CheckingAccount(6, 2500.0, 300.0));

        BankService.addClient(bank, client1);
        BankService.addClient(bank, client2);
        BankService.addClient(bank, client3);

        int numberOfClients = bankReportStreams.getNumberOfClients(bank);

        assertEquals(3, numberOfClients);
    }

    @Test
    public void testGetNumberOfAccounts() throws ClientExistsException {
        Bank bank = new Bank();
        BankReportStreams bankReportStreams = new BankReportStreams();

        Client client1 = new Client("Smith John", Gender.MALE, "New York");
        client1.addAccount(new SavingAccount(1, 1000.0));
        client1.addAccount(new CheckingAccount(2, 1000.0, 100.0));

        Client client2 = new Client("Smith Michelle", Gender.FEMALE, "Los Angeles");
        client2.addAccount(new SavingAccount(3, 2000.0));

        BankService.addClient(bank, client1);
        BankService.addClient(bank, client2);

        int numberOfAccounts = bankReportStreams.getNumberOfAccounts(bank);

        assertEquals(3, numberOfAccounts);
    }

    @Test
    public void testGetClientsSorted() throws ClientExistsException {
        Bank bank = new Bank();
        BankReportStreams bankReportStreams = new BankReportStreams();

        Client client1 = new Client("Smith John", Gender.MALE, "New York");
        client1.addAccount(new SavingAccount(1, 1000.0));

        Client client2 = new Client("Smith Michelle", Gender.FEMALE, "Los Angeles");
        client2.addAccount(new SavingAccount(2, 2000.0));

        Client client3 = new Client("Doe Jane", Gender.FEMALE, "New York");
        client3.addAccount(new SavingAccount(3, 1500.0));

        BankService.addClient(bank, client1);
        BankService.addClient(bank, client2);
        BankService.addClient(bank, client3);

        SortedSet<Client> sortedClients = bankReportStreams.getClientsSorted(bank);

        Iterator<Client> clientIterator = sortedClients.iterator();
        assertEquals(client1, clientIterator.next());
        assertEquals(client3, clientIterator.next());
        assertEquals(client2, clientIterator.next());
    }

    @Test
    public void testGetBankCreditSum() throws ClientExistsException {
        Bank bank = new Bank();
        BankReportStreams bankReportStreams = new BankReportStreams();

        Client client1 = new Client("Smith John", Gender.MALE, "New York");
        client1.addAccount(new CheckingAccount(1, 1000.0, 100.0));

        Client client2 = new Client("Smith Michelle", Gender.FEMALE, "Los Angeles");
        client2.addAccount(new CheckingAccount(2, 1500.0, 200.0));

        BankService.addClient(bank, client1);
        BankService.addClient(bank, client2);

        double bankCreditSum = bankReportStreams.getBankCreditSum(bank);

        assertEquals(2500.0, bankCreditSum, 0.0);
    }

    @Test
    public void testGetCustomerAccounts() throws ClientExistsException {
        Bank bank = new Bank();
        BankReportStreams bankReportStreams = new BankReportStreams();

        Client client1 = new Client("Smith John", Gender.MALE, "New York");
        client1.addAccount(new SavingAccount(1, 1000.0));

        Client client2 = new Client("Smith Michelle", Gender.FEMALE, "Los Angeles");
        client2.addAccount(new CheckingAccount(2, 1500.0, 200.0));

        BankService.addClient(bank, client1);
        BankService.addClient(bank, client2);

        Map<Client, Collection<Account>> customerAccounts = bankReportStreams.getCustomerAccounts(bank);

        assertEquals(2, customerAccounts.size());
        assertTrue(customerAccounts.containsKey(client1));
        assertTrue(customerAccounts.containsKey(client2));
        assertEquals(1, customerAccounts.get(client1).size());
        assertEquals(1, customerAccounts.get(client2).size());
    }

    @Test
    public void testGetClientsByCity() throws ClientExistsException {
        Bank bank = new Bank();

        BankReportStreams bankReportService = new BankReportStreams();

        Client client1 = new Client("Smith John", Gender.MALE, "New York");
        client1.addAccount(new SavingAccount(1, 1000.0));
        client1.addAccount(new CheckingAccount(2, 1000.0, 100.0));

        Client client2 = new Client("Smith Michelle", Gender.FEMALE, "Los Angeles");
        client2.addAccount(new SavingAccount(3, 2000.0));
        client2.addAccount(new CheckingAccount(4, 1500.0, 200.0));

        Client client3 = new Client("Doe Jane", Gender.FEMALE, "New York");
        client3.addAccount(new SavingAccount(5, 3000.0));
        client3.addAccount(new CheckingAccount(6, 2500.0, 300.0));

        BankService.addClient(bank, client1);
        BankService.addClient(bank, client2);
        BankService.addClient(bank, client3);

        Map<String, ArrayList<Client>> clientsByCity = bankReportService.getClientsByCity(bank);

        assertEquals(2, clientsByCity.size());
        assertEquals(2, clientsByCity.get("New York").size());
        assertEquals(1, clientsByCity.get("Los Angeles").size());
        assertTrue(clientsByCity.get("New York").contains(client1));
        assertTrue(clientsByCity.get("New York").contains(client3));
        assertTrue(clientsByCity.get("Los Angeles").contains(client2));
    }

}
