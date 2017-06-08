package SWE645_Assignment3_varun;

/*
 * Name: Varun Rajavelu
 * This file contains the mean & SD and its corresponding setters/getters
 * */

import javax.faces.bean.ManagedBean;

@ManagedBean  (name="winningResult")
public class WinningResult {
	
	private double mean;
	private double standardDeviation;
	
	public WinningResult(){
		
	}

	public double getMean() {
		return mean;
	}

	public void setMean(double mean) {
		this.mean = mean;
	}

	public double getStandardDeviation() {
		return standardDeviation;
	}

	public void setStandardDeviation(double standardDeviation) {
		this.standardDeviation = standardDeviation;
	}

}
