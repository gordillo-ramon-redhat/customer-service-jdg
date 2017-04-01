package com.redhat;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

	@Autowired
	private CustomerRepository repository;
	
    @RequestMapping(method=RequestMethod.GET,value="/customer")
    public Customer getCustomer(
    		@RequestParam(value="id") UUID id
    		) {
    	Customer c = repository.findById(id);
    	
    	if (c ==null){
    		throw new CustomerNotFoundException();
    	}
    	
        return c;
    }

    @RequestMapping(method=RequestMethod.PUT,value="/customer")
    public Customer putCustomer(
    		@RequestParam(value="first-name") String firstName,
    		@RequestParam(value="last-name") String lastName,
    		@RequestParam(value="email") String email
    		) {
         return repository.insert(UUID.randomUUID(), firstName, lastName, email);
    }

    @RequestMapping(method=RequestMethod.DELETE,value="/customer")
    public void deleteCustomer(@RequestParam(value="id") UUID id
    		) {
    	repository.delete(id);
    }

    
    @RequestMapping(method=RequestMethod.POST,value="/customer")
    public Customer updateCustomer(
    		@RequestParam(value="id") UUID id,
    		@RequestParam(value="first-name", required = false) String firstName,
    		@RequestParam(value="last-name", required = false) String lastName,
    		@RequestParam(value="email", required = false) String email
    		) {
    	Customer c_cache = repository.findById(id);
    	
    	if (c_cache == null){
    		throw new CustomerNotFoundException();
       	}
    		
        Customer c_update = repository.insert(
        		id, 
        		(firstName!=null)?firstName:c_cache.getFirstName(), 
        		(lastName!=null)?lastName:c_cache.getLastName(), 
        		(email!=null)?email:c_cache.getEmail());
        
    	return c_update;
    }
    
    
    @ResponseStatus(value=HttpStatus.NOT_FOUND,reason="This customer is not found in the system")
    public class CustomerNotFoundException extends RuntimeException 
    {
    	private static final long serialVersionUID = 100L;
    }
    
}