// Reads a Comma Separated Value file and prints its contents.

// From A Beginning Programmer's Guide to Java
// at http://beginwithjava.blogspot.com/

// From the article 'Java CSV File Reader'
// at http://beginwithjava.blogspot.com/2011/05/java-csv-file-reader.html

/* CSVRead.java

   A simple demonstration of reading a CSV file and converting it
   to a Java data array, usable in a Java program. We're just
   printing out the data after loading it into a Java object here.

   The companion file Example.csv gives some sample data that can
   be used when testing this or other csv reader programs.

  mag-11May2011

*/

//Import the needed Objects
import java.io.*;
import java.util.Arrays;

public class CSVRead{

    public static void main(String[] arg) throws Exception {

        BufferedReader CSVFile = new BufferedReader(new FileReader("Example.csv"));

        String dataRow = CSVFile.readLine(); // Read the first line of data.
        // The while checks to see if the data is null. If it is, we've hit
        //  the end of the file. If not, process the data.
        while (dataRow != null){
            String[] dataArray = dataRow.split(",");
            for (String item:dataArray) { System.out.print(item + "\t"); }
            System.out.println(); // Print the data line.
            dataRow = CSVFile.readLine(); // Read next line of data.
        }

        // Close the file once all data has been read.
        CSVFile.close();

        // End the printout with a blank line.
        System.out.println();

    } //main()
} // CSVRead