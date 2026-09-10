using System;
using System.Runtime.InteropServices.Marshalling;
using System.IO;
using System.Text;
using System.Runtime.Intrinsics.X86;



class Average
{
    static void Main(String[] args)
    {
           String line;
           Double grade = 0.0;
           Double sum = 0.0;
           int count = 0;
           Double average = 0.0;
        do
        {
            Console.WriteLine("Enter a grade(0.0-100.0): ");
            line = Console.ReadLine();
            grade = Double.Parse(line);

            if (grade >= 0.0 && grade <= 100.0)
            {
                count++;
                sum += grade;
            }
           
        } while (grade >= 0.0 && grade <= 100.0);

        Double d = sum / count;
        if(Double.IsNaN(d) || Double.IsInfinity(d))
        {
            Console.WriteLine("A valid average could not be found with the given data");
        }
        else
        {
            average = sum / count;
            Console.WriteLine("Average is: " + average + "\nCount is: " + count + "\nSum is: " + sum);
        }
    }
}
