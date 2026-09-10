/*
Group 2 - File Processing Program
John Gerega (ger9822@pennwest.edu
Colleen Bucher (buc4883@pennwest.edu)
Lance Ramsey(ram28736@pennwest.edu)
Clayton Sanner(san5024@pennwest.edu) 
Purpose of this program is to receive an input file to read (user or command), then validate said file
The program will then receive an output file to create (user or command), then validate said file
Program will continue to validate until either a valid name is entered or no name is entered, in which case it will terminate
Program will open the input file for reading, read each line, tokenize the line, and then fix each token to ensure
1. The token is a word - meaning it begins with a letter and only contains hypens, digits, or numbers
2. The token is a number - meaning it is a sequence of digits
Once finished processing, numbers will be added to the total sum, and words will be kept in an array of word objects
Once the file is read, The out put file will contain
The list of words in the order encountered
the count of each word
The total number of unique words
The total sum of integers
*/

import java.io.*;
import java.util.*;

class Program2
{
	public static void main(String args[]) throws IOException
	{
		String[] ioname = new String[2];							//creates array of strings called ioname
		Boolean OK = IOFile.getnames(args, ioname);					//calls getnames function and begins file validation process
			if (OK)													//only enters loop if valid file names are entered from getnames
			{
				int i = 0; 											//array index
				int find = 0;
				int count = 0; 										//number of Word objects in word array
				int sum = 0; 										//sum of integers
				String word = null;
				String num = null; 									//String of number
				int temp; 											//stores integer found in temporary variable to add into su,
				BufferedReader infile = IOFile.openin(ioname[0]);	//open input file reader
				PrintWriter outfile = IOFile.openout(ioname[1]);	//open output file reader
				Word[] mywords = new Word[100];						//create array of Word Objects with a max size of 100
				for (int j = 0; j <mywords.length; j++)
				{
					mywords[j] = new Word("");						//Initialize all elements to "" to prevent Nullpointer error
				}
		        word = infile.readLine();							//word string is set to contain the first line of the input file
		        while(word != null)									//while the string is not null
			    {
				    //System.out.println("Read line: " + word); 	//Debug output
				    StringTokenizer inline = new StringTokenizer(word, "\t\n\r !@#$%^&*()_+=~`;:><.,/?|{}[]\\"); //String tokenizer with delimeters of tab, newline, return, and space
					while(inline.hasMoreTokens())					//while loop checking if more tokens are available to check
					{   
					    word = inline.nextToken().toLowerCase();	//sets word to lowercase
					    num = null;									//resets num to null to make sure there is no carry over
					    //System.out.println("Token is: " + word);	//debugging statement
					    char first = word.charAt(0);				//extract first character of word and store it into character first
			            if (Character.isDigit(first))
			            {
				            num = word.substring(0, howMany(word)+1);		//calls howmany function to extract every number in front of first non number character
				            word = word.substring(howMany(word)+1);
				            if (isInt(num))	//checks if num as a valid number
							{
								temp = Integer.parseInt(num);
								sum += temp;										//adds number to sum if true
							} 
				            if (word.length() > 0)
							{
								first = word.charAt(0);		//moves first character to the first character in the new string
							}
							else
							{
								first = num.charAt(0);		//moves first character to the first character in the new string
							}
				            
			            }
            
			            while(!Character.isLetterOrDigit(first) && word.length() > 1)	//while loop for when the first character is neither a number or a digit
						{
							if (Character.isDigit(word.charAt(1)))	//checks if the character after first is a number
							{
								if (first == '-')	//check if the first character is a minus sign
								{
									num = word.substring(0, howMany(word)+1);	//calls howmany function to extract every number in front of first alpha character and place it into num
									if (isInt(num))	//checks if num as a valid number
									{
										temp = Integer.parseInt(num);
										sum += temp;										//adds number to sum if true
									} 
									word = word.substring(howMany(word)+1);		//word is set to the remaining string
									if (word.length() > 0)
									{
										first = word.charAt(0);		//moves first character to the first character in the new string
									}
									else
									{
										first = num.charAt(0);		//moves first character to the first character in the new string
									}
								}
								else
								{
									num = word.substring(1);
									num = num.substring(0, howMany(num)+1);		//calls howmany function to extract every number in front of first alpha character and place it into num
									if (isInt(num))	//checks if num as a valid number
									{
										temp = Integer.parseInt(num);
										sum += temp;										//adds number to sum if true
									} 
									word = word.substring(howMany(word)+1);		//word is set to the remaining string
									if (word.length() > 0)
									{
										first = word.charAt(0);		//moves first character to the first character in the new string
									}
									else
									{
										first = num.charAt(0);		//moves first character to the first character in the new string
									}
								}
						}
						else
						{
							word = word.substring(1);	//creates a substring starting at the next character
							first = word.charAt(0);		//moves first character to the first character in the new string
						}
					}
						
						if (!isInt(word))	//found a word
					    {
							if (i > 0)	// checks if i is greater than 0
							{
								Word check = new Word(word);	//creates new word object called check, passing value in word to constructor
								find = check.FindWord(mywords, word, mywords.length);	//find calls findword, returning an index posiiton of the word or -1 if the word is not in mywords
						        if (find > -1)
						        {
							      mywords[find].addOne();	//adds one to count if word is found
						        }
						        else if (find == -1)
						        {
							      mywords[i] = check;	//stores check into index i in mywords
							      i++;	//increments i by 1
						        }
							}
							else if (i == 0)
							{
								mywords[i] = new Word(word);	//creates new word object out of word
						        i++; //increment i by 1
							}
					    }
				     }
				
					    word = infile.readLine();	//read next line of file
					}
					
			    
			    infile.close();	//close input file
			    int c = 0;
			    while (c < mywords.length && mywords[c].getWord() != "")
			    {
					    mywords[c].print(outfile);	//calls print function of word class to print to outfile
					    c++;	//increments c by one
			    }
			    outfile.println("Number of unique words:\t" + c);
			    outfile.println("Sum of integers:\t" + sum);	//output sum of integers
			    
			    outfile.close();	//closes output file
			    
			    System.out.println("Data stored in file name: " + IOFile.FileName(ioname[1]));
        	}
    }
	
	
	static Boolean isInt(String s)		//function header for IsInt
	{
		int i;	//initalize placeholder integer
		try
			{
				i = Integer.parseInt(s);	//tries converting string into integer
				return true;				//returns true if converted
			}
			catch (NumberFormatException e)
			{
				return false;		//returns flase if conversion fails
			}
	}
	static int howMany(String s)		//custom function, returns last index of a number appearing
	{
		int lastnum = 0;	//last num set to 0
		int i = 0;			//set i to 0
		if (s.charAt(0) == '-' && Character.isDigit(s.charAt(1)))
		{
			i = 1;
		}
		
		while (i < s.length() && Character.isDigit(s.charAt(i)))	//while loop for while i is less than 
		{
			lastnum = i;
			i++;	//increments i by 1
		}
		return lastnum;		//returns index of last number
	}
}


class Word extends Program2	//word class header
{
    private String word;	//private string word declared
    private int quant;		//private int quant declared
    Word (String s)			//constructor for word class
    {
	    this.word = s;		//word set to value in string s
	    quant = 1;			//quant set to 1
    }
    int getCount()		//returns count of word object
    {
        return quant;
    }
    String getWord()	//returns word of word object
    {
        return word;
    }
    boolean isWord(String word)	//returns true if word is equal to this.word, returns false otherwise
    {
        return word.equals(this.word);
    }
    boolean isWordIgnoreCase(String word)	//returns true if word (not case-sensitive) is equal to this.word, returns false otherwise
    {
        return word.equalsIgnoreCase(this.word);
    }
    void addOne()	//adds one to the count
    {
        quant++;
    }
    void print(PrintWriter out)		//print function taking in outfile and printing out word and count in outfile
    {
        out.println(word+"\t"+quant);
    }
    int FindWord(Word[] list, String word, int n)		//find word function taking array of Word objects, a word, and integer n(number of elements)
    {
	    int i = 0;	//set i = 0
		
	    while (i < n && !list[i].word.equalsIgnoreCase(word))	//traverse through array until word matches entered word or if i exceeds n
	    {
		   i++; 
	    }
	    
	    if (i < n && list[i].word.equalsIgnoreCase(word))	
	    {
		    return i;	//returns index (i) if the word is found
	    }
	    else
	    {
		    return -1;		//returns -1 if word is not find
	    }
    }
}

class IOFile extends Program2		//IOFile class header
{
	static Boolean FileExist(String name)	//returns true or false based on file existence
	{
		//String full = IOFile.FilePath(name) + IOFile.FileName(name) + IOFile.FileExtension(name);
		File check = new File(name);	//creates new file with the string entered as the name
		return check.exists();			//checks if file exists
	}
	
	static void FileBackup(String name, String exten)	//function for backing up file
	{
		String fname = IOFile.FileName(name);		//combines name and extension into one string
		String newname = fname + exten;		//puts string name and .bak extension into string named newname
		File old = new File(name);			//creates a file with original name	
		File back = new File(newname);		//creates a file with newname
		if (IOFile.FileExist(newname))		//checks if newname file exists
		{
			back.delete();		//delets if file exists
		}
		old.renameTo(back);		//renames old file to name of back file
	}
	
	static String FileExtension(String name)		//extracts extension from string name
	{
		String exten;		//creates string named extension
		int begin = name.lastIndexOf(".");		//sets integer named begin to the last index of the period
		exten = name.substring(begin+1, name.length());	//exten is now equal to a substring from the last . to the remaining length of the string
		return exten;	//returns exten
	}
	
	static String FileName(String name)	//returns just the file name
	{
		String fname = "";
		int begin = name.lastIndexOf('\\');
		int end = name.lastIndexOf('.');
		fname = name.substring(begin+1, end);
		return fname;
		//File test = new File(name);
		//return test.getName();
	}
	static String FilePath(String name)			//return file path
	{	
		String pname = name.substring(0, name.lastIndexOf('\\'));
		return pname;
	}
	static BufferedReader openin(String name) throws IOException
	{
		BufferedReader in = null;		//sets buffered reader to null
		try
		{
			in = new BufferedReader(new FileReader(name));		//tries opening the file name for reading
		}
		catch (FileNotFoundException exception)
		{
			System.out.print("Could not open file for reading.");		//error message if file isn't found
		}
		return in; 														//tries returning buffered reader in
	}
	
	static PrintWriter openout(String name) throws IOException		
	{
		PrintWriter out = null;		//sets printwriter named out to null
		try
		{
			out = new PrintWriter(new FileWriter(name));		//tries opening printwriter with output file name
		}
		catch (FileNotFoundException exception)
		{
			System.out.print("Could not open file for writing.");	//error message if file isn't found
		}
		return out;			//returns out
		
	}
	
	static String setFile(String name)
	{
		File test = new File(name);
		return test.getAbsolutePath();
	}
	
	static String promptInput(String name)			//functioning for validating input files
	{
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));		//new buffered reader
		boolean inexist = false;				//boolean inexist set initially to false
		String line = name;						//sets string line equal to name passed in
		String result = "";						//sets string result to empty, this will be returned
		if (line.isEmpty())
		{
			System.out.print("Enter an input file name: ");			//prompts for name if command prompt passed in was empty
			try
			{
				line = stdin.readLine();			//tries reading in input
			}
			catch(IOException e)
			{
				System.out.println("Error occurred while reading from the keyboard: " +e);		//error message for IOException
			}	
		}
		while(!line.isEmpty() && result.isEmpty())//do
		{
			if(!line.isEmpty())		//checks to make sure line isn't empty
			{
				inexist = IOFile.FileExist(line);			//sets inexist on the file name passed in/entered in
				if (inexist)		//checks if inexist is true
				{
					System.out.println("Found input file, moving on");		//prints message for file found
					result = IOFile.setFile(line);						//gets path and stores it into result
				}
				else
				{
						System.out.println("File does not exist");		//print message for input file not existing
						
				}
				
			
			}
			if (result.isEmpty())
			{
				System.out.print("Enter an input file name: ");		//prompt again if the result string is empty
				try
				{
					line = stdin.readLine();				//tries reading in input
				}
				catch(IOException e)
				{
					System.out.println("Error occurred while reading from the keyboard: " +e);	//error message for IOException
				}			
			}
		
		}		//while(!line.isEmpty() && result.isEmpty());	//this loop will repeat until the user either hits enter key or a enters a file that exists
		
		
		return result;		//returns string stored in result
		
	}
	
	static String promptOutput(String name)		//function for validating input files
	{
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));		//new buffered reader
		boolean outexist = true;		//boolean outexist set initially to true
		String line = name;				//sets string line equal to name passed in
		String temp = "";				//sets string temp to "", used for overwrite prompt
		String result = "";				//sets string result to empty, this will be returned
		if (line.isEmpty())
		{
			System.out.print("Enter an output file name: ");			//prompts for output file name if command prompt is 0
			try
			{
				line = stdin.readLine();			//reads in file name
				outexist = IOFile.FileExist(line);		//sets outexist to value returned from FileExist
			}
			catch(IOException e)
			{
				System.out.println("Error occurred while reading from the keyboard: " +e);
			}
		}
		do
		{
			if(!line.isEmpty())
			{
				outexist = IOFile.FileExist(line);		//sets outexist to value returned from FileExist
				if (!outexist)
				{
					System.out.println("Output file does not exist, moving on");		//message for output not existing
					result = IOFile.setFile(line);							//stores path in result
				}
				else
				{
					
					System.out.println("Output file already exists");		//message for file already existing
				}
			}
			
			if (outexist)		//result not initialized
			{
					System.out.print("Enter a new name or overwrite (press o): ");		//overwrite or new name prompt
					try
					{
						temp = line;		//store line into temp, that way we can determine if user enters an o in the prompt
						line = stdin.readLine();		//reads in line
					}
					catch(IOException e)
					{
						System.out.println("Error occurred while reading from the keyboard: " +e);		//error message
					}
					if (line.isEmpty())
					{
						result = "";		//sets result to empty if enter key is hit
					}
					else if (line.equals("o"))
					{
						IOFile.FileBackup(temp, ".bak");	//backs up file if 'o' is entered
						//outexist = IOFile.FileExist(temp);										//double checks existence
						result = IOFile.setFile(temp);											//stores path in result
					}
			}
			
		}while(!line.isEmpty() && result.isEmpty());		//loops while line is not empty and result is empty
		
		
		
		return result;		//return value from pyzdrowski
	}
	static Boolean getnames(String args[], String[] ioname) throws IOException
	{
		String cmdInput = "";		//command input string
		String cmdOutput = "";		//command output string
		boolean moveon = true;		//boolean for running the program
		
		switch (args.length)
		{
			case 0:
				ioname[0] = promptInput("");	//sends empty input into promptinput
				if (ioname[0].isEmpty())		//checks if ioname is empty
				{
					moveon = false;			//marks moveon to be false
					System.out.println("No entry found. Terminating");	//terminating message
				}
				else					
				{
					ioname[1] = promptOutput("");		//otherwise, send empty string into promptoutput
					if (ioname[1].isEmpty())			//checks if file name is empty
					{
						moveon = false;			//marks moveon to be false
						System.out.println("No entry found. Terminating");	//terminating message
					}
				}
				break;
			case 1:
				cmdInput = args[0];		//case 1 means one argument has been entered in command prompt
				ioname[0] = promptInput(cmdInput);		//ioname set to value returnd by prompt input with command agrument passed in
				
				if (ioname[0].isEmpty())	//checks if ioname is empty
				{
					moveon = false;	//marks moveon to be false
					System.out.println("No entry found. Terminating");	//terminating message
				}
				else
				{
					ioname[1] = promptOutput("");	//otherwise, send empty string into promptoutput
					if (ioname[1].isEmpty())		//checks if file name is empty
					{
						moveon = false;		//marks moveon to be false	
						System.out.println("No entry found. Terminating");	//terminating message
					}
				}
				break;
			default:
				cmdInput = args[0];		//default case only takes in two arguments from command, even if more are entered
				//cmdInput = IOFile.FileName(cmdInput) + IOFile.FileExtension(cmdInput);
				//System.out.println(cmdInput);
				cmdOutput = args[1];
				ioname[0] = promptInput(cmdInput);	//ioname set to value returnd by prompt input with command agrument passed in
				if (ioname[0].isEmpty())	//checks if ioname is empty
				{
					moveon = false;	//marks moveon to be false
					System.out.println("No entry found. Terminating");	//terminating message
				}
				else
				{
					ioname[1] = promptOutput(cmdOutput);	//otherwise, send args index 1 into promptoutput
					if (ioname[1].isEmpty())	//checks if ioname is empty
					{
						moveon = false;		//marks moveon to be false
						System.out.println("No entry found. Terminating");	//terminating message
					}
				}
				break;
			
		}
		
		return moveon;		//returns value of moveon
	}
}