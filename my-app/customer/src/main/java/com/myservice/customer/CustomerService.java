package com.myservice.customer;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final RestTemplate restTemplate;
    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();

        // todo: check email valid, not taken, store customer in db
        ExampleMatcher customerMatcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withMatcher("firstName", ignoreCase())
                .withMatcher("lastName", ignoreCase())
                .withMatcher("email", ignoreCase());
        Example<Customer> example = Example.of(customer, customerMatcher);
        Boolean exists = customerRepository.exists(example);
        if (exists){
            throw new IllegalStateException("customer is already exists");
        }
        customerRepository.saveAndFlush(customer);

        // todo: check if fraudster
        FraudCheckResponse fraudCheckResponse = restTemplate.getForObject(
                "http://FRAUD/api/v1/fraud-check/{customerId}",
                FraudCheckResponse.class,
                customer.getId()
        );
        if (fraudCheckResponse != null && fraudCheckResponse.isFraudster()){
            throw new IllegalStateException("fraudster");
        }

        customerRepository.save(customer);
        // todo: send notification
    }

    public Optional<Customer> getCustomerById(Integer id) {
        return customerRepository.findById(id);
    }

    public List<Customer> getCustomers(Integer pageCount) {
        return customerRepository.findAll();
    }

//    public Page<Customer> getCustomers(Integer pageCount) {
//        Page<Customer> page = customerRepository.findAll(
//                PageRequest.of(0, pageCount, Sort.by(Sort.Direction.ASC, "firstName")));
//        return page;
//    }

    public void deleteStudent(Integer id) {
        boolean exists = customerRepository.existsById(id);
        if (!exists){
            throw new IllegalStateException("customer not exists");
        }
        customerRepository.deleteById(id);
    }
    @Transactional
    public void updateCustomerByParam(
            Integer id,
            String firstName,
            String lastName,
            String email) {
        boolean exists = customerRepository.existsById(id);
        if (!exists){
            throw new IllegalStateException("customer not exists");
        }
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        optionalCustomer.ifPresent(
                customer ->
            {
                if (firstName != null &&
                        firstName.length() > 0 &&
                        !customer.getFirstName().equals(firstName)){
                    customer.setFirstName(firstName);
                }
                if (lastName != null &&
                        lastName.length() > 0 &&
                        !customer.getLastName().equals(lastName)){
                    customer.setLastName(lastName);
                }
                if (email != null &&
                        email.length() > 0 &&
                        !customer.getEmail().equals(email)){
                    customer.setEmail(email);
                }
            }
        );
    }
}
