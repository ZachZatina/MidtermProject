public class CreditCardPayment extends Payment {

	private String ccNumber;
	private String expDate;
	private String ccv;
	private double total;

	@Override
	public void payment() {
		this.total = super.toPaymentTotal();

	}

	public String getCcNumber() {
		return ccNumber;
	}

	public void setCcNumber(String ccNumber) {
		ccNumber = ccNumber.replace(ccNumber.subSequence(0, 11), "XXXX-XXXX-XXXX-");
		this.ccNumber = ccNumber;

	}

	public String getExpDate() {
		return expDate;
	}

	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}

	public String getCcv() {
		return ccv;
	}

	public void setCcv(String ccv) {
		this.ccv = ccv;
	}

}
