package com.luxoft.bankapp.domain;

import java.util.*;
import java.util.stream.Collectors;

public class BankReportStreams {
    /**
     * Returns the number of clients in the bank
     */
    public int getNumberOfClients(Bank bank) {
        return bank.getClients().size();
    }

    /**
     * Returns the total number of accounts for all bank clients.
     */
    public int getNumberOfAccounts(Bank bank) {
        return bank.getClients().stream().mapToInt(client -> client.getAccounts().size()).sum();
    }

    /**
     * Returns the set of all accounts. The list is ordered by current account balance.
     */
    public SortedSet<Client> getClientsSorted(Bank bank) {
        return bank.getClients().stream()
                .collect(Collectors.toCollection(
                                () ->
                                        new TreeSet<>(Comparator.comparingDouble(client -> client.getAccounts().stream().mapToDouble(Account::getBalance).sum()))
                        )
                );
    }

    /**
     * Returns the total amount of credits granted to the bank clients. That is, the sum of all values above account balance for CheckingAccount
     */
    public double getBankCreditSum(Bank bank) {
        return bank.getClients().stream().
                mapToDouble(client -> client.getAccounts()
                        .stream()
                        .filter(account -> account instanceof CheckingAccount)
                        .mapToDouble(Account::getBalance).sum()
                ).sum();
    }

    /**
     * Returns a map Client<=>List_of_HisAccounts. This method is somehow ‘artificial’,
     * because a client already has a list of his/her accounts.
     * The aim of this step is to learn to declare complex data structures using generics and convert data
     */
    public Map<Client, Collection<Account>> getCustomerAccounts(Bank bank) {
        return bank.getClients().stream().collect(Collectors.toMap(client -> client, Client::getAccounts));
    }

    /**
     * Add field city to class Client. This method needs a table Map<String, List<Client>>, with cities as
     * the keys and values – the list of clients in each city.
     * Print the resulting table, and order by city name alphabetically.
     */
    public Map<String, ArrayList<Client>> getClientsByCity(Bank bank) {
        Map<String, ArrayList<Client>> result = bank.getClients().stream().collect(Collectors.groupingBy(Client::getCity, Collectors.toCollection(ArrayList::new)));

        // print the resulting table, and order by city name alphabetically
        result.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
            System.out.println(entry.getKey());
            entry.getValue().forEach(client -> System.out.println("\t" + client.getName()));
        });

        return result;
    }
}
