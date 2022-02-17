
public class Parser {
	String commandName;
	String[] args;

	// This method will divide the input into commandName and args
	// where "input" is the string command entered by the user
	public boolean parse(String input) 
	{
		args = input.split(" ");
		commandName = args[0];
		
		if (isValid(commandName)) 
		{
			return true;
		}

		return false;
	}
	

	public String getCommandName() 
	{
		return commandName;
	}

	public String[] getArgs() {
		return args;
	}

	public boolean isValid(String command) 
	{
		String[] commands = { "cd", "ls", "ls -r", "pwd", "echo", "mkdir", "cat", "touch", "cp", "rm", "rmdir",
				"exit" };

		for (String s : commands) 
		{
			if (s.equals(command)) 
			{
				return true;
			}
		}
		return false;
	}
}
