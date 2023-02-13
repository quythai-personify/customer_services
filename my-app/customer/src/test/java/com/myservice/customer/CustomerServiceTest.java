package com.myservice.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @Mock private CustomerRepository customerRepository;
    private CustomerService underTest;
    @Mock private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerRepository, restTemplate);
    }

    @Test
    void canRegisterCustomer() {
        // given
        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(
                "Jamila",
                "Hasmed",
                "jamila@gmail.com"
        );

        // when
        underTest.registerCustomer(customerRegistrationRequest);

        // then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertEquals(capturedCustomer.getFirstName(), customerRegistrationRequest.firstName());
        assertEquals(capturedCustomer.getLastName(), customerRegistrationRequest.lastName());
        assertEquals(capturedCustomer.getEmail(), customerRegistrationRequest.email());
    }

    @Test
    void willThrowWhenCustomerExist() {
        // given
        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(
                "Jamila",
                "Hasmed",
                "jamila@gmail.com"
        );
        given(customerRepository.exists(any())).willReturn(true);

        // when
        // then
        assertThatThrownBy( () ->
                underTest.registerCustomer(customerRegistrationRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("customer is already exists");

        verify(customerRepository, never()).save(any());
    }

    @Test
    void getCustomerById() {
        Integer id = 1;
        // when
        underTest.getCustomerById(id);
        // then
        verify(customerRepository).findById(id);
    }

    @Test
    void getCustomers() {
        // when
        Integer currentPageNumber = 0;
        Integer itemCountPerPage = 10;
        List<Customer> customers = underTest.getCustomers(currentPageNumber, itemCountPerPage);
        // then
        verify(customerRepository).findAll(
                PageRequest.of(currentPageNumber, itemCountPerPage, Sort.by(Sort.Direction.ASC, "id")));
    }

    @Test
    void canDeleteStudent() {
        Integer id = 1;
        given(customerRepository.existsById(id)).willReturn(true);
        // when
        underTest.deleteStudent(id);
        // then
        verify(customerRepository).deleteById(id);
    }

    @Test
    void willThrowWhenCustomerDoesNotExist() {
        Integer id = 1;
        given(customerRepository.existsById(id)).willReturn(false);

        // when
        // then
        assertThatThrownBy( () ->
                underTest.deleteStudent(id))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("customer not exists");

        verify(customerRepository, never()).deleteById(any());
    }

    @Test
    void canUpdateCustomerByParam() {
        //given
        Integer id = 1;
        String firstName = "Quy";
        String lastName = "Thai";
        String email = "quy.thai@gmail.com";

        Customer customer = Customer.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .build();

        String newFirstName = "Toan";
        String newLastName = "Nguyen";
        String newEmail = "toan.nguyen@gmail.com";

        given(customerRepository.existsById(id)).willReturn(true);
        given(customerRepository.findById(id)).willReturn(Optional.of(customer));
        // when
        underTest.updateCustomerByParam(id, newFirstName, newLastName, newEmail);
        // then
        Optional<Customer> optionalCustomer = underTest.getCustomerById(id);
        assertEquals(optionalCustomer.get().getId(), id);
        assertEquals(optionalCustomer.get().getFirstName(), newFirstName);
        assertEquals(optionalCustomer.get().getLastName(), newLastName);
        assertEquals(optionalCustomer.get().getEmail(), newEmail);
    }
}