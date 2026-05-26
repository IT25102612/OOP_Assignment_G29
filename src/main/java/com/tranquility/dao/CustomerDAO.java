package com.tranquility.dao;

import com.tranquility.models.Customer;
//libraries
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    private static final String FILE_NAME = "customers.txt";

    //addCustomer
    public void addCustomer(Customer customer) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true));

        writer.write(customer.toString());
        writer.newLine();

        writer.close();
    }
    //getAllCustomers
    public List<Customer> getAllCustomers() throws IOException {

        List<Customer> customers = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));

        String line;

        while ((line = reader.readLine()) != null) {

            String[] data = line.split(",");

            Customer customer = new Customer(
                    data[0],
                    data[1],
                    data[2],
                    data[3],
                    data[4]
            );

            customers.add(customer);
        }

        reader.close();

        return customers;
    }
    //getById
    public Customer getById(String customerId) throws IOException {

        List<Customer> customers = getAllCustomers();

        for (Customer c : customers) {

            if (c.getCustomerId().equals(customerId)) {
                return c;
            }
        }

        return null;
    }
    //getByEmail
    public Customer getByEmail(String email) throws IOException {

        List<Customer> customers = getAllCustomers();

        for (Customer c : customers) {

            if (c.getEmail().equals(email)) {
                return c;
            }
        }

        return null;
    }
    //nicExists
    public boolean nicExists(String nic) throws IOException {

        List<Customer> customers = getAllCustomers();

        for (Customer c : customers) {

            if (c.getNic().equals(nic)) {
                return true;
            }
        }

        return false;
    }
    //generateCustomerId
    public String generateCustomerId() throws IOException {

        List<Customer> customers = getAllCustomers();

        if (customers.isEmpty()) {
            return "C001";
        }

        String lastId = customers.get(customers.size() - 1).getCustomerId();

        int number = Integer.parseInt(lastId.substring(1));

        number++;

        return String.format("C%03d", number);
    }
    //updateCustomer
    public boolean updateCustomer(Customer updatedCustomer) throws IOException {

        List<Customer> customers = getAllCustomers();

        BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME));

        boolean updated = false;

        for (Customer c : customers) {

            if (c.getCustomerId().equals(updatedCustomer.getCustomerId())) {

                writer.write(updatedCustomer.toString());

                updated = true;

            } else {

                writer.write(c.toString());
            }

            writer.newLine();
        }

        writer.close();

        return updated;
    }
    //deleteCustomer
    public boolean deleteCustomer(String customerId) throws IOException {

        List<Customer> customers = getAllCustomers();

        BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME));

        boolean deleted = false;

        for (Customer c : customers) {

            if (!c.getCustomerId().equals(customerId)) {

                writer.write(c.toString());
                writer.newLine();

            } else {

                deleted = true;
            }
        }

        writer.close();

        return deleted;
    }
}