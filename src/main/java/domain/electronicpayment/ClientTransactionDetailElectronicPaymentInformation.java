package domain.electronicpayment;

import com.mbasys.common.utility.Money;

public class ClientTransactionDetailElectronicPaymentInformation {
	
	private String cardNumber;
	private int month;
	private int year;
	private String securityCode;
	private String accountNumber;
	private String confirmAccountNumber;
	private String routingNumber;
	private String checkNumber;
	private String firstName;
	private String lastName;
	private String emailAddress;
	private String streetAddress;
	private String zipCode;
	private Money paymentAmount;
	private String comments;
	private boolean isPrintOnClientStatement;
	
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getSecurityCode() {
		return securityCode;
	}
	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getConfirmAccountNumber() {
		return confirmAccountNumber;
	}
	public void setConfirmAccountNumber(String confirmAccountNumber) {
		this.confirmAccountNumber = confirmAccountNumber;
	}
	public String getRoutingNumber() {
		return routingNumber;
	}
	public void setRoutingNumber(String routingNumber) {
		this.routingNumber = routingNumber;
	}
	public String getCheckNumber() {
		return checkNumber;
	}
	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getStreetAddress() {
		return streetAddress;
	}
	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	@Override
	public String toString() {
		return "ClientTransactionDetailElectronicPaymentInformation [cardNumber="
				+ cardNumber
				+ ", month="
				+ month
				+ ", year="
				+ year
				+ ", securityCode="
				+ securityCode
				+ ", accountNumber="
				+ accountNumber
				+ ", confirmAccountNumber="
				+ confirmAccountNumber
				+ ", routingNumber="
				+ routingNumber
				+ ", checkNumber="
				+ checkNumber
				+ ", firstName="
				+ firstName
				+ ", lastName="
				+ lastName
				+ ", emailAddress="
				+ emailAddress
				+ ", streetAddress="
				+ streetAddress
				+ ", zipCode="
				+ zipCode
				+ ", paymentAmount="
				+ paymentAmount
				+ ", comments="
				+ comments
				+ ", isPrintOnClientStatement="
				+ isPrintOnClientStatement
				+ "]";
	}
	public Money getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(Money paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public boolean isPrintOnClientStatement() {
		return isPrintOnClientStatement;
	}
	public void setPrintOnClientStatement(boolean isPrintOnClientStatement) {
		this.isPrintOnClientStatement = isPrintOnClientStatement;
	}
	

}
