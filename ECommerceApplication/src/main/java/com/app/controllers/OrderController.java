package com.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.config.AppConstants;
import com.app.payloads.OrderDTO;
import com.app.payloads.OrderResponse;
import com.app.payloads.ResponseDTO;
import com.app.services.OrderService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class OrderController {
	
	@Autowired
	public OrderService orderService;
	
	@PostMapping("/public/users/{email}/carts/{cartId}/payments/{paymentMethod}/order")
	public ResponseEntity<?> orderProducts(@PathVariable String email, @PathVariable Long cartId,
			@PathVariable String paymentMethod, @RequestParam(name = "bankName", required = false) String bankName) {
		
		if (paymentMethod.toLowerCase().equals("transferbank")) {
			int mandiri = 1234567890;
			int bca = 1235564662;
			int bri = 1343655775;

			if (bankName == null) {
				String responseMsg = "Bank name is required for bank transfer payment method, Bank available: Mandiri, BCA, BRI";
				ResponseDTO response = new ResponseDTO(responseMsg, null, HttpStatus.BAD_REQUEST.value());
				return new ResponseEntity<ResponseDTO>(response, HttpStatus.BAD_REQUEST);

			} else {
				Boolean isBankMandiri = bankName.toLowerCase().equals("mandiri");
				Boolean isBankBCA = bankName.toLowerCase().equals("bca");
				Boolean isBankBRI = bankName.toLowerCase().equals("bri");
				String responseMsg;

				if (isBankMandiri) {
					responseMsg = "Please transfer to this Mandiri account number: " + mandiri;
				} else if (isBankBCA) {
					responseMsg = "Please transfer to this BCA account number: " + bca;
				} else if (isBankBRI) {
					responseMsg = "Please transfer to this BRI account number: " + bri;
				} else {
					responseMsg = "Bank " + bankName + " is not available, Bank available: Mandiri, BCA, BRI";
					ResponseDTO response = new ResponseDTO(responseMsg, null, HttpStatus.BAD_REQUEST.value());
					return new ResponseEntity<ResponseDTO>(response, HttpStatus.BAD_REQUEST);
				}
				
				OrderDTO order = orderService.placeOrder(email, cartId, paymentMethod);
				ResponseDTO response = new ResponseDTO(responseMsg, order, HttpStatus.CREATED.value());

				return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
			}
		}
		
		String message = "Payment method " + paymentMethod
				+ " is not available, Payment method available: TransferBank";
		ResponseDTO response = new ResponseDTO(message, null, HttpStatus.BAD_REQUEST.value());
		
		return new ResponseEntity<ResponseDTO>(response, HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/admin/orders")
	public ResponseEntity<OrderResponse> getAllOrders(
			@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_ORDERS_BY, required = false) String sortBy,
			@RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
		
		OrderResponse orderResponse = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortOrder);

		return new ResponseEntity<OrderResponse>(orderResponse, HttpStatus.FOUND);
	}
	
	@GetMapping("public/users/{email}/orders")
	public ResponseEntity<List<OrderDTO>> getOrdersByUser(@PathVariable String email) {
		List<OrderDTO> orders = orderService.getOrdersByUser(email);
		
		return new ResponseEntity<List<OrderDTO>>(orders, HttpStatus.FOUND);
	}
	
	@GetMapping("public/users/{email}/orders/{orderId}")
	public ResponseEntity<OrderDTO> getOrderByUser(@PathVariable String email, @PathVariable Long orderId) {
		OrderDTO order = orderService.getOrder(email, orderId);
		
		return new ResponseEntity<OrderDTO>(order, HttpStatus.FOUND);
	}
	
	@PutMapping("admin/users/{email}/orders/{orderId}/orderStatus/{orderStatus}")
	public ResponseEntity<OrderDTO> updateOrderByUser(@PathVariable String email, @PathVariable Long orderId, @PathVariable String orderStatus) {
		OrderDTO order = orderService.updateOrder(email, orderId, orderStatus);
		
		return new ResponseEntity<OrderDTO>(order, HttpStatus.OK);
	}

}
