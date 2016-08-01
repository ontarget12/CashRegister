package register;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;

public class CashRegister 
{
	private Path inputFile;
	private Path outputPath;
	private String outputFile = null;	
	
	public CashRegister(String inputFile,String outputPath,String outputFile) throws IOException
	{
		this.inputFile = new File(inputFile).toPath();
		this.outputPath = new File(outputPath).toPath();
		
		if(outputFile == null)
		{
			throw new IOException("outputFile is null");
		}
		
		if(!Files.exists(this.inputFile))
		{
			throw new IOException("input path " + inputFile.toString() + " does not exist");
		}		
		
		if(!Files.exists(this.outputPath))
		{
			throw new IOException("output Path " + outputPath.toString() + " does not exist");
		}
		
		//this.inputFile = inputFile;
		//this.outputPath = outputPath;
		this.outputFile = outputFile;
	}
	
	public boolean startProcessing() throws IOException
	{
		boolean success = false;
		String[] input;
		File oFile = new File(outputPath.toString() + File.separator + outputFile);
		PrintWriter pw = new PrintWriter(oFile);
		FileOutputStream fos = new FileOutputStream(outputFile,false);		
		FileInputStream inputStream = new FileInputStream(inputFile.toFile());//inputFn);
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));		
		String strLine;
		CalculateChange cc = new CalculateChange();

		while ((strLine = br.readLine()) != null)   
		{
		  //System.out.println (strLine);
			CharSequence s = ",";
			
			
			if(!strLine.contains(s))
			{
				pw.println("no delimiters");
			}
			else
			{
				//String ret = 
				input = strLine.split(",");
				
				if(input.length > 2)
				{
					pw.println("too many data elements");
				}
				else if(input.length == 2)
				{
					BigDecimal cost;
					
					try
					{
						cost = new BigDecimal(input[0]);
					}
					catch(NumberFormatException n)
					{
						pw.println("invalid cost");
						continue;
					}
					
					if(Utility.getDecimalPlaces(cost) > 2)
					{
						pw.println("too many decimals in cost");
						continue;
					}
					
					BigDecimal cash;
					
					try
					{
						cash = new BigDecimal(input[1]);
					}
					catch(NumberFormatException n)
					{
						pw.println("invalid cash");
						continue;
					}
					
					if(Utility.getDecimalPlaces(cash) > 2)
					{
						pw.println("too many decimals in cash");
						continue;
					}
					
					/*public int compareTo(BigDecimal val)
					Compares this BigDecimal with the specified BigDecimal. Two BigDecimal objects that are equal in value but have a different scale (like 2.0 and 2.00) are considered equal by this method. This method is provided in preference to individual methods for each of the six boolean comparison operators (<, ==, >, >=, !=, <=). The suggested idiom for performing these comparisons is: (x.compareTo(y) <op> 0), where <op> is one of the six comparison operators.
					Specified by:
					compareTo in interface Comparable<BigDecimal>
					Parameters:
					val - BigDecimal to which this BigDecimal is to be compared.
					Returns:
					-1, 0, or 1 as this BigDecimal is numerically less than, equal to, or greater than val.*/
					
					int comp = cost.compareTo(cash);
					
					if(comp == 0)
					{
						pw.println("no change");
					}
					else if(comp == 1)
					{
						pw.println("not enough money");
					}
					else if(comp == -1)
					{
						String change = cc.getChange(cost, cash);
						pw.println(change);
					}
					
				}
				
			}
			
		}

		pw.flush();
		pw.close();
		br.close();
		cc = null;		
		return success;
	}
	
	public static void main(String [] args) throws IOException
	{
		/*
		 * C:\Program Files\Java\jdk1.8.0_101\bin>javac -cp ..\lib c:\eclipse\workspaces\register\register\src\register\CashRegister.java c:\eclipse\workspaces\register\register\src\register\CalculateChange.java c:\eclipse\workspaces\register\register\src\register\Utility.java
		 * C:\Program Files\Java\jdk1.8.0_101\bin>java -cp ..\lib;C:\eclipse\workspaces\register\register\src register.CashRegister c:\myPath\input.txt c:\myOutputPath myOutputFile.txt
		 */
		
		if(args.length == 0)
		{
			System.out.println("usage: CashRegister input file, output path, output file" );
			System.out.println("example: CashRegister c:\\myPath\\myInputFile.txt c:\\myOutputPath myOutputFile.txt");
		}
		
		if(args[0] == null || args[1] == null || args[2] == null)
		{
			//String inputFile,String outputPath,String outputFile
			System.out.println("usage: CashRegister input file, output path, output file" );
			System.out.println("example: CashRegister c:\\myPath\\myInputFile.txt c:\\myOutputPath myOutputFile.txt");
		}
		
		CashRegister cr = new CashRegister(args[0],args[1],args[2]);
		cr.startProcessing();
	}

}
