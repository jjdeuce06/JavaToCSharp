using System;
using System.IO;

public class IOFile
{
	public IOFile()
	{
	}

	static Boolean FileExists(String name)    //returns true or false based on file existence
	{
		File check = new File(name);  //creates new file with the string entered as the name
		return check.Exists();    //checks if file exists
	}

	static void FileBackup(String name, String exten) //backup file function
	{
		String fname = FileName(name);
		String newname = fname + exten;
		File old = new File(name);
		File back = new File(newname);
		if (back.Exists)
		{
			back.Delete();
		}

		//rename the file
	}

	static String FileExtension(String name)
	{
		String exten;
		int begin = name.LastIndexOf(".");
		exten = name.Substring(begin + 1, name.Length);
		return exten;
	}

	static String FilePath(String name)
	{
		String pname = name.Substring(0, name.LastIndexOf('\\'));
		return pname;
	}

	static StreamReader openin(String name)
	{
		StreamReader reader = null;
		try
		{
			reader = new StreamReader(name);
		}
		catch (FileNotFoundException exception)
		{
			Console.WriteLine("Could not open file for reading");
		}

		return reader;

	}

	static StreamWriter openout(String name)
	{ 
		StreamWriter writer = null;
		try
        {
			writer = new StreamWriter(name);
		}
		catch (FileNotFoundException exception)
        {
			Console.WriteLine("Could not open file for writing");
		}
		
		return writer;
	}

	static String setFile(String name)
	{
		File test = new File(name);
		return GetFullPath(test);
	}

	static String promptInput(String name)
	{
		StreamReader stdin = new StreamReader(Console.OpenStandardInput());
		Boolean inexist = false;
		String line = name;
		String result = "";

		if(String.IsNullOrEmpty(line))
		{
			Console.WriteLine("Enter an input file name: ");
			try
			{
				line = stdin.ReadLine();
			}
			catch (IOException e)
			{
				Console.WriteLine("Error occured while reading from keyboard: " + e);
			}
		}

		while(!String.IsNullOrEmpty(line) && String.IsNullOrEmpty(result))
		{
			if(!String.IsNullOrEmpty(line))
			{
				inexist = FileExists(line);
				if (inexist)
				{
					Console.WriteLine("Found input file, next step");
					result = setFile(line);
				}
				else
				{
					Console.WriteLine("File does not exist");
				}
			}

			if(String.IsNullOrEmpty(result))
			{
				Console.WriteLine("Enter an input file name: ");
                try
                {
                    line = stdin.ReadLine();
                }
                catch (IOException e)
                {
                    Console.WriteLine("Error occured while reading from keyboard: " + e);
                }
            }
		}

		return result;
	}

	static String promptOutput(String name)
	{
        StreamReader stdin = new StreamReader(Console.OpenStandardInput());
        Boolean outexist = true;     //boolean outexist set initially to true
        String line = name;             //sets string line equal to name passed in
        String temp = "";               //sets string temp to "", used for overwrite prompt
        String result = "";             //sets string result to empty, this will be returned

        if (String.IsNullOrEmpty(line))
        {
            Console.WriteLine("Enter an output file name: ");
            try
            {
                line = stdin.ReadLine();
                outexist = FileExist(line);
            }
            catch (IOException e)
            {
                Console.WriteLine("Error occured while reading from keyboard: " + e);
            }
        }

		do
		{
			if (!String.IsNullOrEmpty(line))
			{
				outexist = FileExists(line);
				if (!outexist)
				{
					Console.WriteLine("Output file does not exist, moving on");
					result = setFile(line);
				}
				else
				{
					Console.WriteLine("Output file already exists");
				}
			}

			if (outexist)
			{
				Console.WriteLine("Enter a new name or overwrite (press o): ");
				try
				{
					temp = line;
					line = stdin.ReadLine();
				}
				catch (IOException e)
				{
					Console.WriteLine("Error occurred while reading from the keyboard" + e);
				}

				if (String.IsNullOrEmpty(line))
				{
					result = "";
				}
				else if (line.Equals("o"))
				{
					FileBackup(temp, ".bak");
					result = setFile(temp);
				}
			}
		} while (!String.IsNullOrEmpty(line) && String.IsNullOrEmpty(result));

        return result;
    }

	



}
