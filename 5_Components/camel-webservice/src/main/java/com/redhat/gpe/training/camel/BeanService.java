package com.redhat.gpe.training.camel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.camel.Body;
import com.redhat.gpe.training.Customer;
import com.redhat.gpe.training.CustomerType;
import com.redhat.gpe.training.GetAllCustomersResponse;
import com.redhat.gpe.training.GetCustomerByName;
import com.redhat.gpe.training.GetCustomerByNameResponse;
import com.redhat.gpe.training.SaveCustomer;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanService {

    private static Logger log = LoggerFactory.getLogger(BeanService.class);
    static List<Customer> customers = new ArrayList<Customer>();
    Random randomGenerator = new Random();

    public void generateCustomer() {
        Customer customer = new Customer();
        customer.setName("redhat");
        customer.setNumOrders(randomGenerator.nextInt(100));
        customer.setRevenue(randomGenerator.nextInt(10000));
        customer.setType(CustomerType.BUSINESS);
        customer.setTest(BigDecimal.valueOf(100.00));
        customer.getAddress().add("FuseSource Office");
        customers.add(customer);
    }

    public GetAllCustomersResponse getCustomers(@Body String name) {
        GetAllCustomersResponse response = new GetAllCustomersResponse();
        response.getReturn().addAll(customers);
        return response;
    }

    public GetCustomerByNameResponse getCustomerByName(@Body GetCustomerByName cSearched) {
        List<Customer> result = new ArrayList<Customer>();
        // Search for Customer using name as key
        for(Customer c : customers) {
            if (c.getName().equals(cSearched.getName())) {
               result.add(c);
               log.info(">> Customer find !");
               break;
            }
        }

        GetCustomerByNameResponse response = new GetCustomerByNameResponse();
        response.getReturn().addAll(result);

        return response;
    }

    public Customer saveCustomer(@Body SaveCustomer c) {
        String address = (c.getCustomer().getAddress().get(0) != null) ?  c.getCustomer().getAddress().get(0) : "Unknown address";
        XMLGregorianCalendar birthDate = c.getCustomer().getBirthDate();

        // enrich the customer received from backend data
        Customer customer = new Customer();
        customer.setName(c.getCustomer().getName());
        customer.getAddress().add(address);
        customer.setBirthDate(birthDate);
        customer.setNumOrders(randomGenerator.nextInt(100));
        customer.setRevenue(randomGenerator.nextInt(10000));
        customer.setType(CustomerType.PRIVATE);
        customer.setTest(BigDecimal.valueOf(100.00));
        customers.add(customer);

        log.info(">> Customer created and added in the array.");

        return customer;
    }

}
