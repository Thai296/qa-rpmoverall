package com.overall.mar.menu;

public enum MarTabs {
	ACCESSION("Accession"),
	CLIENT("Client"),
	FILE_MAINTENANCE("File Maintenance"),
	PAYMENT("Payment"),
	PAYOR("Payor"),
	FINANCIAL_MANAGEMENT("Financial Management"), 
	XP_ACCN_EP_SUMMARY("EP Summary");

	public String marTab;

	private MarTabs(String marTab) {
		this.marTab = marTab;
	}

	public String getValue() {
		return this.marTab;
	}

}
