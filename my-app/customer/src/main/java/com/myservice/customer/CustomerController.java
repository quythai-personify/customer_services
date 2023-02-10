package com.myservice.customer;

import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(path = "api/v1/customers")
@AllArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping(path = "/")
    public String getCustomer(){
        return "Welcome to customer service";
    }
    @GetMapping(path = "{id}")
    public Optional<Customer> getCustomer(@PathVariable("id") Integer id){
        return customerService.getCustomerById(id);
    }
//    @GetMapping(path = "all")
//    public List<Customer> getCustomers(
//            @RequestParam(required = false) Integer pageCount){
//        return customerService.getCustomers(pageCount);
//    }
@GetMapping(path = "all")
    public List<Customer> getCustomers(
            @RequestParam Integer currentPageNumber,
            @RequestParam Integer itemCountPerPage){
        return customerService.getCustomers(currentPageNumber, itemCountPerPage);
    }
    @PostMapping
    public void registerCustomer(@RequestBody CustomerRegistrationRequest customerRegistrationRequest){
        log.info("new customer registration {}", customerRegistrationRequest);
        customerService.registerCustomer(customerRegistrationRequest);
    }
    @PostMapping(path = "generateCustomers")
    public void generateCustomer(
            @RequestParam(required = true) Integer customerCount){
        Faker faker = new Faker();
        for(int i = 0; i < customerCount; i++)
        {
            String fakeFirstName = faker.name().firstName();
            String fakeLastName = faker.name().lastName();
            String fakeEmail = String.format("%s.%s@gmail.com", fakeFirstName.toLowerCase(), fakeLastName.toLowerCase());
            CustomerRegistrationRequest fakeCustomerRegistrationRequest = new CustomerRegistrationRequest(
                    fakeFirstName,
                    fakeLastName,
                    fakeEmail
            );
            customerService.registerCustomer(fakeCustomerRegistrationRequest);
        }
    }
    @DeleteMapping(path = "{id}")
    public void deleteCustomer(@PathVariable("id") Integer id){
        customerService.deleteStudent(id);
    }
    @PutMapping(path = "{id}")
    public void updateCustomerByParam(
            @PathVariable("id") Integer id,
            @RequestBody Customer customerDetail) {
        customerService.updateCustomerByParam(
                id,
                customerDetail.getFirstName(),
                customerDetail.getLastName(),
                customerDetail.getEmail()
        );
    }
}
