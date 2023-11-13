package com.demo.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.demo.ecommerce.service.OrderDetailService;
import com.demo.ecommerce.service.ProductService;
import com.demo.ecommerce.service.UserService;
@RestController
public class StatsConroller {
	
	@Autowired 
	ProductService prodService;
	@Autowired
	UserService userService;
	@Autowired
    private OrderDetailService orderDetailService;
	
	//Number of products
	//@PreAuthorize("hasRole('Admin')")
    @GetMapping("NumberOfProducts")
	public long NumberOfProducts()
	{
		return prodService.getNumberOfProducts();
    }
	
	//@PreAuthorize("hasRole('Admin')")
    @GetMapping("NumberOfRegularUsers")
	public int NumberOfRegularUsers()
	{
		System.out.println("Nombre de users :::: "+userService.getUsersWithUserRole());
		return userService.getUsersWithUserRole();
    }
	
	//@PreAuthorize("hasRole('Admin')")
	@GetMapping("NumberOfAdminUsers")
	public int NumberOfAdminUsers()
	{
		System.out.println("Nombre de Admins :::: "+userService.getUsersWithAdminRole());
		return userService.getUsersWithAdminRole();
    }
	
	//@PreAuthorize("hasRole('Admin')")
	@GetMapping("NumberOfUsersWithBothRoles")
	public int NumberOfUsersWithBothRoles()
	{
		System.out.println("Nombre de users multiples :::: "+userService.getUsersWithBothRoles());
		return userService.getUsersWithBothRoles();
    }
	
	//@PreAuthorize("hasRole('Admin')")
	@GetMapping("TotalAmountsOfAllOrders")
	public double TotalAmountsOfAllOrders()
	{
		System.out.println("***************************");
		System.out.println("***************************");
		System.out.println("Amounts of All Orders : "+orderDetailService.getAllOrdersAmounts()+"$");
		System.out.println("***************************");
		System.out.println("***************************");
		return orderDetailService.getAllOrdersAmounts();
    }
	@GetMapping("TotalAmountsOfPlacedOrders")
	public double TotalAmountsOfPlacedOrders()
	{
		System.out.println("***************************");
		System.out.println("***************************");
		System.out.println("Amounts of All Orders : "+orderDetailService.getAllOrdersAmounts()+"$");
		System.out.println("***************************");
		System.out.println("***************************");
		return orderDetailService.getPlacedOrdersAmounts();
    }
	@GetMapping("TotalAmountsOfDelivredOrders")
	public double TotalAmountsOfDelivredOrders()
	{
		System.out.println("***************************");
		System.out.println("***************************");
		System.out.println("Amounts of All Orders : "+orderDetailService.getAllOrdersAmounts()+"$");
		System.out.println("***************************");
		System.out.println("***************************");
		return orderDetailService.getDelivredOrdersAmounts();
    }
	
	@GetMapping("NumberOfOrders")
	public long NumberOfOrders()
	{
		return orderDetailService.getNumberOfOrders();
    }
	
}
