package com.demo.ecommerce.service;

import com.demo.ecommerce.configuration.JwtRequestFilter;
import com.demo.ecommerce.dao.CartDao;
import com.demo.ecommerce.dao.OrderDetailDao;
import com.demo.ecommerce.dao.ProductDao;
import com.demo.ecommerce.dao.UserDao;
import com.demo.ecommerce.entity.*;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderDetailService {

    private static final String ORDER_PLACED = "Placed";

    private static final String KEY = "rzp_test_AXBzvN2fkD4ESK";
    private static final String KEY_SECRET = "bsZmiVD7p1GMo6hAWiy4SHSH";
    private static final String CURRENCY = "INR";

    @Autowired
    private OrderDetailDao orderDetailDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CartDao cartDao;

    public List<OrderDetail> getAllOrderDetails(String status) {
        List<OrderDetail> orderDetails = new ArrayList<>();

        if(status.equals("All")) {
            orderDetailDao.findAll().forEach(
                    x -> orderDetails.add(x)
            );
        } else {
            orderDetailDao.findByOrderStatus(status).forEach(
                    x -> orderDetails.add(x)
            );
        }


         return orderDetails;
    }
    public double getAllOrdersAmounts() {
        List<OrderDetail> orderDetails = (List<OrderDetail>) orderDetailDao.findAll();
        double somme=0;
        for(OrderDetail order : orderDetails) {
        	somme=somme+order.getOrderAmount();
        }
        return somme;
       
    }
    public double getDelivredOrdersAmounts() {
        List<OrderDetail> orderDetails = (List<OrderDetail>) orderDetailDao.findAll();
        double somme=0;
        for(OrderDetail order : orderDetails) {
        	if(order.getOrderStatus().equals("Delivered")) {
        		somme=somme+order.getOrderAmount();
        	}
        }
        return somme;
       
    }
    public double getPlacedOrdersAmounts() {
        List<OrderDetail> orderDetails = (List<OrderDetail>) orderDetailDao.findAll();
        double somme=0;
        for(OrderDetail order : orderDetails) {
        	if(order.getOrderStatus().equals("Placed")) {
        		somme=somme+order.getOrderAmount();
        	}
        }
        return somme;
       
    }
    public int getNumberOfOrders() {
    	List<OrderDetail> orderDetails = (List<OrderDetail>) orderDetailDao.findAll();
    	return orderDetails.size();
    }

    public List<OrderDetail> getOrderDetails() {
        String currentUser = JwtRequestFilter.CURRENT_USER;
        User user = userDao.findById(currentUser).get();

        return orderDetailDao.findByUser(user);
    }

    public void placeOrder(OrderInput orderInput, boolean isSingleProductCheckout) {
        List<OrderProductQuantity> productQuantityList = orderInput.getOrderProductQuantityList();

        for (OrderProductQuantity o: productQuantityList) {
            Product product = productDao.findById(o.getProductId()).get();

            String currentUser = JwtRequestFilter.CURRENT_USER;
            User user = userDao.findById(currentUser).get();

            OrderDetail orderDetail = new OrderDetail(
                  orderInput.getFullName(),
                  orderInput.getFullAddress(),
                  orderInput.getContactNumber(),
                  orderInput.getAlternateContactNumber(),
                    ORDER_PLACED,
                    product.getProductDiscountedPrice() * o.getQuantity(),
                    product,
                    user,
                    orderInput.getTransactionId()
            );

            // empty the cart.
            if(!isSingleProductCheckout) {
                List<Cart> carts = cartDao.findByUser(user);
                carts.stream().forEach(x -> cartDao.deleteById(x.getCartId()));
            }

            orderDetailDao.save(orderDetail);
        }
    }

    public void markOrderAsDelivered(Integer orderId) {
        OrderDetail orderDetail = orderDetailDao.findById(orderId).get();

        if(orderDetail != null) {
            orderDetail.setOrderStatus("Delivered");
            orderDetailDao.save(orderDetail);
        }

    }

//    public TransactionDetails createTransaction(Double amount) {
//        try {
//
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("amount", (amount * 100) );
//            jsonObject.put("currency", CURRENCY);
//
//            RazorpayClient razorpayClient = new RazorpayClient(KEY, KEY_SECRET);
//            
//            Order order = razorpayClient.orders.create(jsonObject);
//
//            TransactionDetails transactionDetails =  prepareTransactionDetails(order);
//            return transactionDetails;
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//        return null;
//    }

    public TransactionDetails prepareTransactionDetails(Double amount) {
        
    	JSONObject jsonObject = new JSONObject();
        jsonObject.put("amount", (amount * 100) );
        TransactionDetails transactionDetails = new TransactionDetails();
        transactionDetails.setAmount(jsonObject.getInt("amount"));
        return transactionDetails;
    }
}
