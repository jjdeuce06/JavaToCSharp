using System;
using System.IO;

class Program2
{
    static void Main(string[] args)
    {
        String[] ioname = new String[2];
        Boolean OK = IOFile.getnames(args, ioname);

        if (OK)
        {
            using (StreamReader input = IOFile.openin(ioname[0]))
            using (StreamWriter output = IOFile.openout(ioname[1]))
            {
                if (input != null && output != null)
                {
                    String line;
                    while ((line = input.ReadLine()) != null)
                    {
                        output.WriteLine(line);
                    }
                }
            }
        }
    }
}