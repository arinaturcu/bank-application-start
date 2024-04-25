package com.luxoft.bankapp.domain.mail;

public class EmailService {
    private final Queue emailQueue;
    private volatile boolean running = true;

    public EmailService() {
        emailQueue = new Queue();

        new Thread(() -> {
            synchronized (emailQueue) {
                while (true) {
                    System.out.println("Waiting for emails...");

                    while (emailQueue.isEmpty()) {
                        if (!running) {
                            return;
                        }

                        try {
                            emailQueue.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }

                    Email email = emailQueue.poll();
                    System.out.println("--- Sending email:\n" + email);
                }
            }
        }).start();
    }

    public void sendNotificationEmail(Email email) {
        synchronized (emailQueue) {
            if (running) {
                emailQueue.add(email);
                emailQueue.notify();
            }
        }
    }

    public void close() {
        synchronized (emailQueue) {
            System.out.println("Closing email service...");
            running = false;
            emailQueue.notify();
        }
    }
}