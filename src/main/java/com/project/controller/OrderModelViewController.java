package com.project.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.project.entity.Address;
import com.project.entity.Customer;
import com.project.entity.Order;
import com.project.entity.Product;
import com.project.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/client/orders")
@RequiredArgsConstructor
public class OrderModelViewController {

   //TODO: find a way to display the orders table dynamically considering how thymeleaf works with lists (I need to transfer the products list data to a Java list)
   //The error I get whenever I submit the form for placing orders: java.lang.NullPointerException: Cannot invoke "java.util.List.isEmpty()" because the return value of "com.project.entity.Order.getProducts()" is null

   private final OrderService orderService;

   @GetMapping
   public String getAllOrdersView(Model model) {
      model.addAttribute("orders", orderService.getAllOrders());
      return "orders/index";
   }

   @GetMapping("/customers")
   public String getAllOrdersCustomersView(Model model) {
      model.addAttribute("customers", orderService.getAllOrders().stream().map(Order::getCustomer).collect(Collectors.toList()));
      return "orders/customers";
   }

   @GetMapping("/addresses")
   public String getAllOrdersAddressesView(Model model) {
      model.addAttribute("addresses", orderService.getAllOrders().stream().map(Order::getAddress).collect(Collectors.toList()));
      return "orders/addresses";
   }

   @GetMapping("/products")
   public String getAllOrdersProductsView(ModelMap model) {
      model.addAttribute("orders", orderService.getAllOrders());
      model.addAttribute("products", orderService.getAllOrders().stream().map(Order::getProducts).flatMap(List::stream).collect(Collectors.toList()));
      return "orders/products";
   }

   @GetMapping("/create-order/step1")
   public String createOrderFormStep1View(Model model) {
      model.addAttribute("order", new Order());
      model.addAttribute("products", new ArrayList<Product>());
      return "orders/create-order/step1";
   }

   @GetMapping("/create-order/step2")
   public String createOrderFormStep2View(Model model) {
      model.addAttribute("order", new Order());
      model.addAttribute("customer", new Customer());
      return "orders/create-order/step2";
   }

   @GetMapping("/create-order/step3")
   public String createOrderFormStep3View(Model model) {
      model.addAttribute("order", new Order());
      model.addAttribute("address", new Address());
      return "orders/create-order/step3";
   }

   @PostMapping("/create-order/step2")
   public String goToSecondStepOrderView(@ModelAttribute("order") Order order) {
      return "redirect:/client/orders/create-order/step2";
   }

   @PostMapping("/create-order/step3")
   public String goToThirdStepOrderView(@ModelAttribute("order") Order order) {
      return "redirect:/client/orders/create-order/step3";
   }

   //TODO: outsource the logic for the views returned in the get and post methods in the add order section

   @PostMapping("/save-new-order")
   public String saveOrderView(@ModelAttribute("order") Order order) {
      try {
         orderService.saveOrder(order);
      } catch(Exception ex) {
         log.info("Encounter error " + ex.getMessage() + " when trying to save your order");
         return "redirect:/client/orders?successfulSave";
      }
      return "redirect:/client/orders?errorSave";
   }

   @GetMapping("/update-order/{id}")
   public String updateOrderFormView(@PathVariable Long id, Model model) {
      model.addAttribute("order", orderService.getOrderById(id));
      return "orders/update-order";
   }

   @PostMapping("/save-updated-order/{id}")
   public String updateOrderView(@ModelAttribute("order") Order order, @PathVariable Long id) {
      try {
         orderService.updateOrderById(order, id);
      } catch(Exception ex) {
         log.info("Encounter error " + ex.getMessage() + " when trying to save your order");
         return "redirect:/client/orders?successfulUpdate";
      }
      return "redirect:/client/orders?errorUpdate";
   }

   @GetMapping("/delete-order/{id}")
   public String deleteOrderView(@PathVariable Long id, Model model) {
      model.addAttribute("order", orderService.getOrderById(id));
      return "orders/delete-order";
   }

   @PostMapping("/delete-order/{id}")
   public String deleteOrderView(@PathVariable Long id) {
      try {
         orderService.deleteOrderById(id);
      } catch(Exception ex) {
         log.info("Encounter error " + ex.getMessage() + " when trying to save your order");
         return "redirect:/client/orders?successfulDelete";
      }
      return "redirect:/client/orders?errorDelete";
   }
}
