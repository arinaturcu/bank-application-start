package com.luxoft.bankapp.domain;

import java.util.*;

/**
 * This class does not use streams
 */
public class BankReport {

    /**
     * Returns the number of clients in the bank
     */
    int getNumberOfClients(Bank bank) {
        return bank.getClients().size();
    }

    /**
     * Returns the total number of accounts for all bank clients.
     */
    int getNumberOfAccounts(Bank bank) {
        int sum = 0;
        for (Client client : bank.getClients()) {
            sum += client.getAccounts().size();
        }

        return sum;
    }

    /**
     * Returns the set of all accounts. The list is ordered by current account balance.
     */
    SortedSet<Client> getClientsSorted(Bank bank) {
        SortedSet<Client> sortedClients = new TreeSet<>((o1, o2) -> {
            double balance1 = 0;
            double balance2 = 0;

            for (Account account : o1.getAccounts()) {
                balance1 += account.getBalance();
            }

            for (Account account : o2.getAccounts()) {
                balance2 += account.getBalance();
            }

            return Double.compare(balance1, balance2);
        });

        sortedClients.addAll(bank.getClients());
        return sortedClients;
    }

    /**
     * Returns the total amount of credits granted to the bank clients.
     * That is, the sum of all values above account balance for CheckingAccount
     */
    double getBankCreditSum(Bank bank) {
        double sum = 0;
        for (Client client : bank.getClients()) {
            for (Account account : client.getAccounts()) {
                if (account instanceof CheckingAccount) {
                    sum += account.getBalance();
                }
            }
        }

        return sum;
    }

    /**
     * Returns a map Client<=>List_of_HisAccounts. This method is somehow ‘artificial’,
     * because a client already has a list of his/her accounts.
     * The aim of this step is to learn to declare complex data structures using generics and convert data
     */
    Map<Client, Collection<Account>> getCustomerAccounts(Bank bank) {
        Map<Client, Collection<Account>> result = new HashMap<>();
        for (Client client : bank.getClients()) {
            result.put(client, client.getAccounts());
        }

        return result;
    }

    /**
     * Add field city to class Client. This method needs a table Map<String, List<Client>>, with cities as
     * the keys and values – the list of clients in each city.
     * Print the resulting table, and order by city name alphabetically.
     */
    Map<String, ArrayList<Client>> getClientsByCity(Bank bank) {
        Map<String, ArrayList<Client>> result = new HashMap<>();
        for (Client client : bank.getClients()) {
            String city = client.getCity();
            if (!result.containsKey(city)) {
                result.put(city, new ArrayList<>());
            }

            result.get(city).add(client);
        }

        Map<String, ArrayList<Client>> sortedResult = new TreeMap<>(result);
        for (Map.Entry<String, ArrayList<Client>> entry : sortedResult.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        return result;
    }
}
