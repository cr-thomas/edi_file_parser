/**
 * 
 */
package com.edi.parser.model;

/**
 * @author CTh116
 *
 */
public class DistributionCenter {
	
	private String dcNumber;
	
	private String address;
	
	private String city;
	
	private String state;
	
	private String zipCode;

	/**
	 * @return the dcNumber
	 */
	public String getDcNumber() {
		return dcNumber;
	}

	/**
	 * @param dcNumber the dcNumber to set
	 */
	public void setDcNumber(String dcNumber) {
		this.dcNumber = dcNumber;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the zipCode
	 */
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * @param zipCode the zipCode to set
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[dcNumber=" + dcNumber + ", address=" + address + ", city=" + city + ", state="
				+ state + ", zipCode=" + zipCode + "]";
	}

}
