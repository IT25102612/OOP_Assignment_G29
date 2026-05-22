package com.tranquility.dao;

import com.tranquility.models.Payment;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class PaymentDAO {

    private static final String FILE_NAME = "payments.txt";

    public void addPayment(Payment payment) {
        try (FileWriter fw = new FileWriter(FILE_NAME, true);
             BufferedWriter bw = new BufferedWriter(fw)) {

            bw.write(payment.toFileString());
            bw.newLine();

        } catch (IOException e) {
            System.out.println("Error adding payment: " + e.getMessage());
        }
    }

    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length == 6) {
                    Payment payment = new Payment(
                            data[0],
                            data[1],
                            Double.parseDouble(data[2]),
                            data[3],
                            data[4],
                            data[5]
                    );
                    payments.add(payment);
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading payments: " + e.getMessage());
        }

        return payments;
    }

    public Payment getByReservationId(String reservationId) {
        for (Payment payment : getAllPayments()) {
            if (payment.getReservationId().equals(reservationId)) {
                return payment;
            }
        }
        return null;
    }

    public Payment getByPaymentId(String paymentId) {
        for (Payment payment : getAllPayments()) {
            if (payment.getPaymentId().equals(paymentId)) {
                return payment;
            }
        }
        return null;
    }

    public boolean updatePayment(String paymentId, String method, String status) {
        List<Payment> payments = getAllPayments();
        boolean found = false;

        for (Payment payment : payments) {
            if (payment.getPaymentId().equals(paymentId)) {
                payment.setMethod(method);
                payment.setStatus(status);
                found = true;
                break;
            }
        }

        if (found) {
            saveAllPayments(payments);
        }

        return found;
    }

    public boolean deletePayment(String paymentId) {
        List<Payment> payments = getAllPayments();
        boolean removed = payments.removeIf(payment -> payment.getPaymentId().equals(paymentId));

        if (removed) {
            saveAllPayments(payments);
        }

        return removed;
    }

    private void saveAllPayments(List<Payment> payments) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Payment payment : payments) {
                bw.write(payment.toFileString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving payments: " + e.getMessage());
        }
    }

    public String generatePaymentId() {
        List<Payment> payments = getAllPayments();

        int nextNumber = payments.size() + 1;

        return String.format("PAY%03d", nextNumber);
    }

    public Payment createPayment(String reservationId, double amount, String method) {
        String paymentId = generatePaymentId();
        String status = "PENDING";
        String paymentDate = LocalDate.now().toString();

        return new Payment(paymentId, reservationId, amount, method, status, paymentDate);
    }
}