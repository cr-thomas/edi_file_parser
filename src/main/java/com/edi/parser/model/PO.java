/**
 * 
 */
package com.edi.parser.model;

import java.util.List;

/**
 * @author CTh116
 *
 */
public class PO {
	
	private String poNumber;
	
	private DistributionCenter distributionCenter;
	
	private List<LineItem> allLineItems;

	/**
	 * @return the poNumber
	 */
	public String getPoNumber() {
		return poNumber;
	}

	/**
	 * @param poNumber the poNumber to set
	 */
	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	/**
	 * @return the distributionCenter
	 */
	public DistributionCenter getDistributionCenter() {
		return distributionCenter;
	}

	/**
	 * @param distributionCenter the distributionCenter to set
	 */
	public void setDistributionCenter(DistributionCenter distributionCenter) {
		this.distributionCenter = distributionCenter;
	}

	/**
	 * @return the allLineItems
	 */
	public List<LineItem> getAllLineItems() {
		return allLineItems;
	}

	/**
	 * @param allLineItems the allLineItems to set
	 */
	public void setAllLineItems(List<LineItem> allLineItems) {
		this.allLineItems = allLineItems;
	}	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PO [poNumber=" + poNumber + ", distributionCenter=" + distributionCenter + ", allLineItems=" + allLineItems
				+ "]";
	}

}
