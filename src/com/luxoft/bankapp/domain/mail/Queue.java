package com.luxoft.bankapp.domain.mail;

import java.util.ArrayList;
import java.util.List;

public class Queue {
    private List<Email> queue = new ArrayList<>();

    public void add(Email email) {
        queue.add(email);
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public Email poll() {
        if (queue.isEmpty()) {
            return null;
        }

        Email email = queue.get(0);
        queue.remove(0);

        return email;
    }
}