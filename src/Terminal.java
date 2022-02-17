import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;





// Class Terminal
public class Terminal 
{
	private Parser parser;
	private String CurrentDirectory;

	Terminal()
	{
		parser = new Parser();
		//Get the current path
		String currentWorkingPath = System.getProperty("user.dir");
		String fullPath = currentWorkingPath + File.separator;
		CurrentDirectory = fullPath;

	}
	
	//Parameterized Constructor
	Terminal(Parser parser) 
	{
		this.parser = parser;
	}
	
	File createFile(String FileName)
	{
		File createdFile = new File(FileName);
		if(!createdFile.isAbsolute())
		{
			createdFile = new File(CurrentDirectory + File.separator + FileName);
		}
		
		return createdFile;
	}

	
	// helper method to print file
	void printFile(File FileName) throws FileNotFoundException 
	{
		Scanner myReader = new Scanner(FileName);

		while (myReader.hasNextLine()) {
			String line = myReader.nextLine();
			System.out.println(line);
		}
		myReader.close();
	}

	

	//function pwd that displays the  the current working directory
	public void pwd() {

		System.out.println(CurrentDirectory);
	}

	// cd to change the working directory
	public void cd(String[] args) 
	{
		// check if the number of args is greater than 3
		if (args.length >= 3) 
		{
			System.out.println("Error: Too many arguments");
		}

		else 
		{
			// change directory to parent directory
			if (args.length == 1 || args[1].equals("..") || args[1].equals("../"))
			{
				File fin = new File(CurrentDirectory);
				CurrentDirectory = fin.getParent();
			}
			
			//change directory  working directory
			else {
				File fin = new File(args[1]);
				// if the input path is absolute path ex:(/home/directory1/directory1)
				if (fin.isAbsolute()) {
					
					if (fin.isDirectory()) {
						CurrentDirectory = args[1];
					}
					
					// if the changed directory is not found in the current directory or not found at all
					else 
					{
						System.out.println("Error: Not a directory");
					}

				}
				// if the input path is relative path ex:(directory1) and must be exit in the current directory
				else
				{
					fin = new File(CurrentDirectory + File.separator + args[1]);
					
					if (fin.isDirectory()) 
					{
						CurrentDirectory = fin.getAbsolutePath();
					}

					else 
					{
						System.out.println("Error: Not a directory");
					}
				}
			}
		}
	}

	// Function to create an empty directory  
	
	public void mkdir(String[] args) {
		
		String dir = "";
		
		// used in case of creating multiple directories at a time
		ArrayList<String> arr = new ArrayList();
		
		for (int i = 1; i < args.length; ++i) 
		{
			File cur = new File(args[i]);
			
			if (cur.isDirectory()) {
				dir = cur.getAbsolutePath();
				break;
			} 
			else 
			{
				arr.add(args[i]);
			}
		}
		
		// if created directories will be in the same working current working directory
		if (dir.equals("")) 
		{
			dir = CurrentDirectory;
		}
		
		for (String s : arr) 
		{

			File fin = new File(dir + File.separator + s);
			if (!fin.exists()) 
			{
				fin.mkdir();
			}

			else {
				System.out.println("Error:" + s + "This directory already exist");
			}

		}
	}

	// printing files and directories in a directory
	public void ls() 
	{
		File fin = new File(CurrentDirectory);
		for (String s : Objects.requireNonNull(fin.list()))
		{
			System.out.println(s);
		}
	}

	//  printing files and directories in the directory in reverse order
	public void lsR() 
	{
		File fin = new File(CurrentDirectory);
		String[] arr = fin.list();
		assert arr != null;
		
		for (int i = arr.length - 1; i >= 0; --i)
		{
			System.out.println(arr[i]);
		}

	}
	
	// printing the command 
	public void echo(String[] args) 
	{
		for (int i = 1; i < args.length; ++i) 
		{
			System.out.print(args[i] + " ");
		}
		System.out.print("\n");
	}
	
	// create file in a given directory
	public void touch(String[] args) 
	{
		if (args.length >= 3) 
		{
			System.out.println("Error: Too many arguments");
		}

		File createdFile = createFile(args[1]);
		

		if (!createdFile.exists()) 
		{
			try
			{
				if (createdFile.createNewFile()) 
				{
					System.out.println(createdFile.getName() + " is created successfully");
				}
			} 
			catch (Exception e) 
			{
				System.out.println("An error occurred");
			}
		} 
		else 
		{
			System.out.println("File already exist");
		}
	}
	
	
	// copy 2 files first into second
	public void cp(String[] args) 
	{
		if (args.length > 3)
		{
			System.out.println("Error: Too many arguments");
		}

		else 
		{
			File fin1 = createFile(args[1]);
			File fin2 = createFile(args[2]);

			
			if (fin1.exists() && fin2.exists()) 
			{
				try 
				{
					FileWriter myWriter = new FileWriter(fin2);
					Scanner myReader = new Scanner(fin1);

					String line;
					while (myReader.hasNextLine()) 
					{
						line = myReader.nextLine();
						myWriter.write(line);
						myWriter.write('\n');
					}
					myReader.close();
					myWriter.close();
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}

			} 
			else 
			{
				System.out.println("One of the files is not found");
			}

		}
	}
	
	// Function to remove a file 
	public void rm(String[] args) 
	{
		if (args.length >= 3) 
		{
			System.out.println("Error: Too many arguments");
		} 
		
		else 
		{
			String fileName = args[1];
			File deletedFile = createFile(fileName);
			
			// if the deleted file is not a file or not exist, or failed to delete
			if (!(deletedFile.exists() && deletedFile.isFile() && deletedFile.delete()))
			{
				System.out.println("Error: There is no file with this  name");
			}
			else 
			{
				System.out.println("Deleted Successfuly");
			}
		}
	}
	
	// print file or concatenate more than on file and print them
	public void cat(String[] args) 
	{
		
		for(int i = 1; i < args.length; ++i)
		{
			File fin = createFile(args[i]);
			if(fin.isDirectory())
			{
				System.out.println(fin.getName()+ " Is Directory");
			}
			else
			{
				try 
				{
					System.out.println(fin.getAbsolutePath());
					printFile(fin);
				} 
				catch (Exception e) 
				{
					System.out.println("File not Found");
				}
				
			}
		}
				
			
	}

	public void rmdir(String[] args) 
	{
		String cur = "";
		
		// remove every file or directory inside the given directory
		if (args.length == 2 && args[1].equals("*")) 
		{
			File file = new File(CurrentDirectory);
			
			for (String path : Objects.requireNonNull(file.list())) 
			{
				File curPath = new File(CurrentDirectory + File.separator + path);
				if (curPath.isDirectory()) 
				{
					if (!curPath.delete()) 
					{
						System.out.println("Error: Filed to delete " + path + " directory is not empty");
					}
				}
			}
		}
		
		
		else
		{
			for (int i = 1; i < args.length; ++i) 
			{
				File fin = createFile(args[i]);
				

				{
					// if the deleted directory is not a directory or not exist, or failed to delete
					if (!(fin.exists() && fin.isDirectory() && fin.delete())) 
					{
						System.out.println("Error: Filed to delete " + args[i] + " directory is not empty or not exit");
					}
					else
					{
						System.out.println("Deleted Successfuly");
					}
				}
			}
		}

	}
	
	// exit command
	public void exit() 
	{
		System.exit(0);
	}
	
	public void setParser(Parser p) 
	{
		this.parser = p;
	}
	

//This method will choose the suitable command method to be called
	public void chooseCommandAction() 
	{
		String command_name = parser.getCommandName();

		String[] args = parser.getArgs();
		if (command_name.equals("ls")) 
		{
			if (args.length > 1 && args[1].equals("-r")) 
			{
				command_name += " -r";
			}

		}
		switch (command_name) 
		{
		case "echo": 
		{
			echo(args);
			break;
		}

		// TODO
		case "cd": 
		{
			cd(args);
			break;
		}
		// TODO
		case "mkdir": 
		{
			mkdir(args);
			break;
		}
		// TODO
		case "pwd": 
		{
			pwd();
			break;
		}
		// TODO
		case "ls": 
		{
			ls();
			break;
		}
		// TODO
		case "ls -r": 
		{
			lsR();
			break;
		}

		// TODO
		case "touch": 
		{
			touch(args);
			break;
		}
		// TODO
		case "cat": 
		{
			cat(args);
			break;
		}
		// TODO
		case "cp": 
		{
			cp(args);
			break;
		}
		// TODO
		case "rm": 
		{
			rm(args);
			break;
		}
		// TODO
		case "rmdir": 
		{
			rmdir(args);
			break;
		}
		// TODO
		case "exit": 
		{
			exit();
			break;
		}

		}
	}

	public static void main(String[] args) {

		Scanner input = new Scanner(System.in);

		System.out.print("$");
		String command = input.nextLine();
		Terminal t1 = new Terminal();
		
		while (true)
		{
			Parser p = new Parser();
			if (p.parse(command)) 
			{
				t1.setParser(p);
				t1.chooseCommandAction();
			}

			else
			{
				System.out.println("Error: Command not found or invalid parameters are entered");
			}
			System.out.print("$");
			command = input.nextLine();
		}

	}
}
