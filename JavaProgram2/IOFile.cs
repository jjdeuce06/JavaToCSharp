using System;
using System.IO;

public class IOFile
    {
        public IOFile()
        {
        }

        public static Boolean FileExists(String name)    //returns true or false based on file existence
        {
            return File.Exists(name);                    //File is a static class in C#, so no "new File(...)"
        }

        public static void FileBackup(String name, String exten) //backup file function
        {
            String fname = FileName(name);               //file name with no path and no extension
            String newname = fname + exten;              //e.g. "output" + ".bak"
            if (File.Exists(newname))
            {
                File.Delete(newname);                    //remove the old backup if there is one
            }
            File.Move(name, newname);                    //"rename" in C# is a Move
        }

        public static String FileExtension(String name)  //extension without the period
        {
            int begin = name.LastIndexOf('.');
            if (begin < 0)
            {
                return "";                               //no period found
            }
            return name.Substring(begin + 1);            //2nd argument of Substring is a COUNT, so leave it off
        }

        public static String FileName(String name)       //just the file name, no path and no extension
        {
            return Path.GetFileNameWithoutExtension(name);
        }

        public static String FilePath(String name)       //just the folder path
        {
            return Path.GetDirectoryName(name);
        }

        public static StreamReader openin(String name)
        {
            StreamReader reader = null;
            try
            {
                reader = new StreamReader(name);
            }
            catch (IOException)                          //covers FileNotFoundException and DirectoryNotFoundException
            {
                Console.WriteLine("Could not open file for reading.");
            }
            return reader;
        }

        public static StreamWriter openout(String name)
        {
            StreamWriter writer = null;
            try
            {
                writer = new StreamWriter(name);
            }
            catch (IOException)                          //bad folder, file in use, etc.
            {
                Console.WriteLine("Could not open file for writing.");
            }
            catch (UnauthorizedAccessException)          //read-only file or no permission
            {
                Console.WriteLine("Could not open file for writing.");
            }
            return writer;
        }

        public static String setFile(String name)        //full path of the file
        {
            return Path.GetFullPath(name);
        }

        public static String promptInput(String name)
        {
            Boolean inexist = false;
            String line = name;
            String result = "";

            if (String.IsNullOrEmpty(line))
            {
                Console.Write("Enter an input file name: ");
                line = Console.ReadLine();               //returns null at end of input; IsNullOrEmpty handles that
            }

            while (!String.IsNullOrEmpty(line) && String.IsNullOrEmpty(result))
            {
                inexist = FileExists(line);
                if (inexist)
                {
                    Console.WriteLine("Found input file, moving on");
                    result = setFile(line);
                }
                else
                {
                    Console.WriteLine("File does not exist");
                }

                if (String.IsNullOrEmpty(result))
                {
                    Console.Write("Enter an input file name: ");
                    line = Console.ReadLine();
                }
            }

            return result;
        }

        public static String promptOutput(String name)
        {
            Boolean outexist = true;
            String line = name;
            String temp = "";
            String result = "";

            if (String.IsNullOrEmpty(line))
            {
                Console.Write("Enter an output file name: ");
                line = Console.ReadLine();
                outexist = FileExists(line);
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
                    Console.Write("Enter a new name or overwrite (press o): ");
                    temp = line;                         //remember the name in case they press o
                    line = Console.ReadLine();

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

        public static Boolean getnames(String[] args, String[] ioname)   //String[] args, not String args[]
        {
            String cmdInput = "";
            String cmdOutput = "";
            Boolean moveon = true;

            switch (args.Length)
            {
                case 0:
                    ioname[0] = promptInput("");
                    if (String.IsNullOrEmpty(ioname[0]))
                    {
                        moveon = false;
                        Console.WriteLine("No entry found. Terminating");
                    }
                    else
                    {
                        ioname[1] = promptOutput("");
                        if (String.IsNullOrEmpty(ioname[1]))
                        {
                            moveon = false;
                            Console.WriteLine("No entry found. Terminating");
                        }
                    }
                    break;

                case 1:
                    cmdInput = args[0];
                    ioname[0] = promptInput(cmdInput);
                    if (String.IsNullOrEmpty(ioname[0]))
                    {
                        moveon = false;
                        Console.WriteLine("No entry found. Terminating");
                    }
                    else
                    {
                        ioname[1] = promptOutput("");
                        if (String.IsNullOrEmpty(ioname[1]))
                        {
                            moveon = false;
                            Console.WriteLine("No entry found. Terminating");
                        }
                    }
                    break;

                default:
                    cmdInput = args[0];
                    cmdOutput = args[1];
                    ioname[0] = promptInput(cmdInput);
                    if (String.IsNullOrEmpty(ioname[0]))
                    {
                        moveon = false;
                        Console.WriteLine("No entry found. Terminating");
                    }
                    else
                    {
                        ioname[1] = promptOutput(cmdOutput);
                        if (String.IsNullOrEmpty(ioname[1]))
                        {
                            moveon = false;
                            Console.WriteLine("No entry found. Terminating");
                        }
                    }
                    break;
            }

            return moveon;
        }
    }

