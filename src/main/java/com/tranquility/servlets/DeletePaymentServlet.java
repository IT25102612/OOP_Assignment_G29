package com.tranquility.servlets;
// Delete Payment Servlet - IT25102650
import com.tranquility.dao.PaymentDAO;

public class DeletePaymentServlet {

    public static void main(String[] args) {

        PaymentDAO dao = new PaymentDAO();

        boolean deleted = dao.deletePayment("PAY003");

        if (deleted) {
            System.out.println("Payment Deleted Successfully!");
        } else {
            System.out.println("Payment Not Found!");
        }
    }
}

