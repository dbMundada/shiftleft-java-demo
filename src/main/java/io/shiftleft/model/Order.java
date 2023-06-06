package io.shiftleft.model;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Order {
  public Order() {
  }

  public Order(long customerAC, String orderNumber, double totalAmount, Address shippingAddress,
               String paymentMethod, String paymentStatus, String deliveryStatus, LocalDateTime deliveryDate) {
    this.customerAC = customerAC;
    this.orderNumber = orderNumber;
    this.totalAmount = totalAmount;
    this.shippingAddress = shippingAddress;
    this.paymentMethod = paymentMethod;
    this.paymentStatus = paymentStatus;
    this.deliveryStatus = deliveryStatus;
    this.deliveryDate = deliveryDate;
    this.phoneNumber = phoneNumber;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private long customerAC;
  private String orderNumber;
  private double totalAmount;
  private Address shippingAddress;
  private String paymentMethod;
  private String paymentStatus;
  private String deliveryStatus;
  private LocalDateTime deliveryDate;
  private String phoneNumber;

  public long getId() {
    return id;
  }

  public long getCustomerAC() {
    return customerAC;
  }

  public void setCustomerAC(long customerAC) {
    this.customerAC = customerAC;
  }

  public String getOrderNumber() {
    return orderNumber;
  }

  public void setOrderNumber(String orderNumber) {
    this.orderNumber = orderNumber;
  }

  public double getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(double totalAmount) {
    this.totalAmount = totalAmount;
  }

  public Address getShippingAddress() {
    return shippingAddress;
  }

  public void setShippingAddress(Address shippingAddress) {
    this.shippingAddress = shippingAddress;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public String getPaymentMethod() {
    return paymentMethod;
  }

  public void setPaymentMethod(String paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  public String getPaymentStatus() {
    return paymentStatus;
  }

  public void setPaymentStatus(String paymentStatus) {
    this.paymentStatus = paymentStatus;
  }

  public String getDeliveryStatus() {
    return deliveryStatus;
  }

  public void setDeliveryStatus(String deliveryStatus) {
    this.deliveryStatus = deliveryStatus;
  }

  public LocalDateTime getDeliveryDate() {
    return deliveryDate;
  }

  public void setDeliveryDate(LocalDateTime deliveryDate) {
    this.deliveryDate = deliveryDate;
  }

  @Override
  public String toString() {
    return "Order [id=" + id + ", customerAC=" + customerAC + ", orderNumber=" + orderNumber + ", totalAmount="
        + totalAmount + ", shippingAddress=" + shippingAddress + ", paymentMethod=" + paymentMethod
        + ", paymentStatus=" + paymentStatus + ", deliveryStatus=" + deliveryStatus + ", deliveryDate=" + deliveryDate
        + "]";
  }
}
