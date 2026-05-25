package com.tranquility.servlets;
// Record Payment Servlet - IT25102650
import com.tranquility.dao.PaymentDAO;
import com.tranquility.models.Payment;

public class RecordPaymentServlet {

    public static void main(String[] args) {

        PaymentDAO dao = new PaymentDAO();

        Payment payment = new Payment(
                "PAY003",
                "RES003",
                30000.0,
                "CARD",
                "PENDING",
                "2026-05-16"
        );

        dao.addPayment(payment);

        System.out.println("Payment Added Successfully!");
    }
}