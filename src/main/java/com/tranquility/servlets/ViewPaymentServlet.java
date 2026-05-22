package main.java.com.tranquility.servlets;

import com.tranquility.dao.PaymentDAO;
import com.tranquility.models.Payment;

public class ViewPaymentServlet {

    public static void main(String[] args) {

        PaymentDAO dao = new PaymentDAO();

        Payment payment = dao.getByReservationId("RES003");

        if (payment != null) {

            System.out.println("----- PAYMENT RECEIPT -----");
            System.out.println("Payment ID: " + payment.getPaymentId());
            System.out.println("Reservation ID: " + payment.getReservationId());
            System.out.println("Amount: " + payment.getAmount());
            System.out.println("Method: " + payment.getMethod());
            System.out.println("Status: " + payment.getStatus());
            System.out.println("Date: " + payment.getPaymentDate());

        } else {
            System.out.println("Payment not found!");
        }
    }
}