package io.shiftleft.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "account")
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private String type;

  private long routingNumber;

  private long accNum;

  private double balance;

  private double interest;

  public Account() {
    balance = 0;
    interest = 0;
  }

  public Account(long accNum, long routingNumber, String type, double initialBalance, double initialInterest) {
    this.accNum = accNum;
    this.routingNumber = routingNumber;
    this.type = type;
    this.balance = initialBalance;
    this.interest = initialInterest;
  }

  public void deposit(double amount) {
    balance = balance + amount;
  }

  public void withdraw(double amount) {
    balance = balance - amount;
  }

  public void addInterest() {
    balance = balance + balance * interest;
  }

  public long getId() {
    return id;
  }

  public String getType() {
    return type;
  }

  public long getRoutingNumber() {
    return routingNumber;
  }

  public long getAccNum() {
    return accNum;
  }

  public double getBalance() {
    return balance;
  }

  public double getInterest() {
    return interest;
  }

  public void setId(long id) {
    this.id = id;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setRoutingNumber(long routingNumber) {
    this.routingNumber = routingNumber;
  }

  public void setAccNum(long accNum) {
    this.accNum = accNum;
  }

  public void setBalance(double balance) {
    this.balance = balance;
  }

  public void setInterest(double interest) {
    this.interest = interest;
  }

  @Override
  public String toString() {
    return "Account [id=" + id + ", type=" + type + ", routingNumber=" + routingNumber + ", accNum="
        + accNum + ", balance=" + balance + ", interest=" + interest + "]";
  }
}
