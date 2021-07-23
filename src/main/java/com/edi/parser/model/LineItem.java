/**
 * 
 */
package com.edi.parser.model;

/**
 * @author CTh116
 *
 */
public class LineItem {
	
	private String lineItemId;
	
	private String sku;
	
	private String bpn;
	
	private String purchasePrice;
	
	private String qty;

	/**
	 * @return the lineItemId
	 */
	public String getLineItemId() {
		return lineItemId;
	}

	/**
	 * @param lineItemId the lineItemId to set
	 */
	public void setLineItemId(String lineItemId) {
		this.lineItemId = lineItemId;
	}

	/**
	 * @return the sku
	 */
	public String getSku() {
		return sku;
	}

	/**
	 * @param sku the sku to set
	 */
	public void setSku(String sku) {
		this.sku = sku;
	}

	/**
	 * @return the bpn
	 */
	public String getBpn() {
		return bpn;
	}

	/**
	 * @param bpn the bpn to set
	 */
	public void setBpn(String bpn) {
		this.bpn = bpn;
	}

	/**
	 * @return the purchasePrice
	 */
	public String getPurchasePrice() {
		return purchasePrice;
	}

	/**
	 * @param purchasePrice the purchasePrice to set
	 */
	public void setPurchasePrice(String purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	/**
	 * @return the qty
	 */
	public String getQty() {
		return qty;
	}

	/**
	 * @param qty the qty to set
	 */
	public void setQty(String qty) {
		this.qty = qty;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[lineItemId=" + lineItemId + ", sku=" + sku + ", bpn=" + bpn + ", purchasePrice="
				+ purchasePrice + ", qty=" + qty + "]";
	}
}
