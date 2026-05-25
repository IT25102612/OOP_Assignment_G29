package com.tranquility.servlets;
// Update Payment Servlet - IT25102650
import com.tranquility.dao.PaymentDAO;

public class UpdatePaymentServlet {

    public static void main(String[] args) {

        PaymentDAO dao = new PaymentDAO();

        boolean updated = dao.updatePayment(
                "PAY003",
                "CASH",
                "CONFIRMED"
        );

        if (updated) {
            System.out.println("Payment Updated Successfully!");
        } else {
            System.out.println("Payment Not Found!");
        }
    }
}
