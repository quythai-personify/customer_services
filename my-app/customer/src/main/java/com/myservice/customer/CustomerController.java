package com.myservice.customer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    @GetMapping(path = "all")
    public List<Customer> getCustomers(
            @RequestParam(required = false) Integer pageCount){
        return customerService.getCustomers(pageCount);
    }
//    public Page<Customer> getCustomers(
//            @RequestParam(required = false) Integer pageCount){
//        return customerService.getCustomers(pageCount);
//    }
    @PostMapping
    public void registerCustomer(@RequestBody CustomerRegistrationRequest customerRegistrationRequest){
        log.info("new customer registration {}", customerRegistrationRequest);
        customerService.registerCustomer(customerRegistrationRequest);
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
