/*
Group 2 - Average Program
John Gerega (ger9822@pennwest.edu
Colleen Bucher (buc4883@pennwest.edu)
Lance Ramsey(ram28736@pennwest.edu)
Clayton Sanner(san5024@pennwest.edu) 
Purpose of this program is to take grades from user input and find the average, total sum , and number of grades entered
Program will output the sum, number of grades, and the average
*/

import java.io.*;		//imports all high level classes

class Average	//class declaration and name
{
	public static void main(String[] args) throws IOException	//main method declaration
	{
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in)); //Opens new buffered reader
		String line;																//Initializes String named line for reading in data
		Double grade = 0.0;															//Initializes a Double named grade
		double sum = 0.0; 															//Initializes a double named sum
		int count = 0; 																//Initializes an integer named grade
		double average = 0.0; 														// Initalizes a double named average
		do { 																		//beginning of a do while loop
			System.out.print("Enter a grade(0.0-100.0): ");							//Asks for user input of a grade
			System.out.flush();														//flushes output stream
			line = stdin.readLine();												//reads in user input as string and assigns it to the line string
			grade = Double.valueOf(line);										//converts value of line into a double value and assigns it to grade
			
			
			if (grade >= 0.0 && grade <= 100.0)										//if statement checking if the value of grade is between 0.0 and 100.0
			{
				count++;															//adds 1 to the count if condition is met	
				sum += grade;														//adds the value of grade to the sum if condition is met
			}
			
		} while (grade >= 0.0 && grade <= 100.0);									//end of do while, checks again if the value of grade is between 0.0 and 100.0
		
		Double d = new Double(sum/count);		//intializes new value d with the new found value of grade
			if (d.isNaN() || d.isInfinite())	//if statement checking if d is NaN or if d is infinite
			{
				System.out.print("A valid average value could not be found with the data given.");		
				//prints out error statement if either condition from if statement is true
			}
			else
			{
				average = sum/count;	//initializes the average value to the result of sum divided by count
				System.out.print("Average is: " + average + "\nCount is: " + count + "\nSum is: " + sum); 
				//print statement displaying the average vaue, number of grades (count), and the total sum of the grades
			}
		
		
	}	
}