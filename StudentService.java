package SWE645_Assignment3_varun;

/*
 * Name: Varun Rajavelu
 * This file contains the controller and logic to calculate the mean and SD.
 * It invokes the appropriate xhmtl file to display
 * It Store the values into a file and reads them
 * It checks if survey date is before the sem start date
 * */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

@ManagedBean (name="studentService")
public class StudentService {

	public StudentService(){}
	
	@ManagedProperty(value="#{student}")
	private Student studentObj = new Student();
	
	@ManagedProperty(value="#{winningResult}")
	private WinningResult raffleObj = new WinningResult();
	
	ArrayList<HashMap<String, String>> studentData = new ArrayList<HashMap<String, String>>();
	
	public Student getStudentObj() {
		return studentObj;
	}

	public void setStudentObj(Student studentObj) {
		this.studentObj = studentObj;
	}

	public WinningResult getRaffleObj() {
		return raffleObj;
	}

	public void setRaffleObj(WinningResult raffleObj) {
		this.raffleObj = raffleObj;
	}
	
	public ArrayList<HashMap<String, String>> getStudentData() {
		return studentData;
	}

	public void setStudentData(ArrayList<HashMap<String, String>> studentData) {
		this.studentData = studentData;
	}
	
	// Function to compute the average of the numbers; 
	// It takes in a string and converts it into numbers before calculating
	public double ComputeMean(){
		double result = 0;	// Variable to store the final result
		double sum;			// Variable to store the sum of the numbers
		int totalNum;		// Variable to store the total numbers that the user has entered


		if(!studentObj.getData().isEmpty() || studentObj.getData() != "" || studentObj.getData() != null){
			String[] numbers = studentObj.getData().split(",");	// Split the string into numbers based on a delimiter

			totalNum = numbers.length;		// Calculate the total numbers

			sum = 0;
			for(int iterator = 0; iterator < totalNum; iterator++)
				sum += Integer.parseInt(numbers[iterator]);			// Calculate the sum of numbers

			result = (sum / totalNum);		//  Calculate the Mean of the numbers
		}
		return result;
	}

	// Function to compute the Standard Deviation of the numbers;
	// It takes in a string and converts it into numbers before calculating
	public double ComputeStandardDeviation(){
		double result = 0; 		// Variable to store the final result
		double mean;			// Variable to store the Mean of the numbers
		double sum;				// Variable to store the sum of the numbers
		double variance; 

		mean = ComputeMean();		// Call the Compute Mean

		int totalNum;		// Variable to store the total numbers entered by the user
		String[] numbers = studentObj.getData().split(",");	// Split the string into numbers based on a delimiter

		totalNum = numbers.length;		// Calculate the total numbers

		sum = 0;
		for(int iterator = 0; iterator < totalNum; iterator++)
			// Get the Sum of square of the difference of the mean
			sum += ((Integer.parseInt(numbers[iterator]) - mean) * (Integer.parseInt(numbers[iterator]) - mean));

		variance = (sum / totalNum);	// Calculate the Variance
		result = Math.sqrt(variance);	// Calculate the Standard Deviation

		return result;
	}

	public String calculateRaffle() throws NullPointerException, IOException{

		FacesContext context = FacesContext.getCurrentInstance();
		
		if(studentObj.getSurveyDate().before(studentObj.getSemDate())){
			FacesMessage errorMessage=
					new FacesMessage("Survey date must be after Sem start date");
					errorMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
					context.addMessage(null, errorMessage);
					return(null);
		}
		
		double mean = ComputeMean();

		WriteToFile();
		
		raffleObj.setMean(mean);
		raffleObj.setStandardDeviation(ComputeStandardDeviation());

		//raffleObj.setMean(12);
		//raffleObj.setStandardDeviation(12.2);
		
		if(mean > 90){
			return "WinnerAcknowledgement";
		}
		else{
			return "SimpleAcknowledgement";
		}
	}
	
	public void WriteToFile(){
		File file = new File("StudentDetails1.txt");
		
		FileWriter record;
		try {
			if(!file.exists()){
				file.createNewFile();
			}
			
			record = new FileWriter(file, true);
			
			record.append(studentObj.getFirstName() + "|" + studentObj.getLastName() + "|" + studentObj.getAddress() + "|" + studentObj.getCity() +
					"|" + studentObj.getState() + "|" + studentObj.getZip() + "|" + studentObj.getTelePhone() + "|" + studentObj.getEmailId() + "|" +
					studentObj.getSurveyDateToString() + "|" + studentObj.getSemDateToString() + "|" + studentObj.getThingstolikeString() + "|" + studentObj.getInterests()  + "|" + studentObj.getLikelihood() + 
					"|" + studentObj.getComments() + "|" + System.getProperty( "line.separator" ));
			
			record.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<HashMap<String, String>> ReadFile() throws IOException{
		
		ArrayList<HashMap<String, String>> read_temp = new ArrayList<HashMap<String, String>>();
		
		BufferedReader buff_reader = null;
		//StringBuilder builder = null;
		
		try{
			FileReader reader = new FileReader("StudentDetails1.txt");
			
			buff_reader = new BufferedReader(reader);
			String line = buff_reader.readLine();
		//	builder = new StringBuilder();
			
			while(line != null){
				String datafield[] = line.split("\\|");
				HashMap<String, String> hashmap = new HashMap<String, String>();
				
				hashmap.put("firstName", datafield[0]);
				hashmap.put("lastName", datafield[1]);
				hashmap.put("Address", datafield[2]);
				hashmap.put("city", datafield[3]);
				hashmap.put("state", datafield[4]);
				hashmap.put("zip", datafield[5]);
				hashmap.put("telephone", datafield[6]);
				hashmap.put("email", datafield[7]);
				hashmap.put("surveyDate", datafield[8]);
				hashmap.put("SemDate", datafield[9]);
				hashmap.put("thingsToLike", datafield[10]);
				hashmap.put("interests", datafield[11]);
				hashmap.put("likelihood", datafield[12]);
				hashmap.put("comments", datafield[13]);
				
				//System.out.println(datafield[0] +" "+datafield[1]);
				read_temp.add(hashmap);
				line = buff_reader.readLine();
			}
		}
		catch(Exception e){
			
		}
		
		setStudentData(read_temp);
		
		buff_reader.close();
		
		return studentData;
	}
}
