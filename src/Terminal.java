import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Terminal {
    private Parser parser;
    private String curDir;

    Terminal() {
        parser = new Parser();
        //Get the current path
        String currentWorkingPath = System.getProperty("user.dir");
        String fullPath = currentWorkingPath + File.separator;
        curDir = fullPath;

    }

    //Parameterized Constructor
    Terminal(Parser parser) {
        this.parser = parser;
    }

    // helper method to print file
    void printFile(File f) throws FileNotFoundException {
        Scanner myReader = new Scanner(f);

        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            System.out.println(line);
        }
        myReader.close();
    }

    // Implement each command in a method, for example:

    //function pwd that returns the current path
    public String pwd() {

        return curDir;
    }

    // cd to change the working directory
    public void cd(String[] args) {
        // check if the number of args is greater than 3
        if (args.length >= 3) {
            System.out.println("Error: Too many arguments");
        } else {
            // change directory to parent directory
            if (args.length == 1 || args[1].equals("..") || args[1].equals("../")) {
                File f = new File(curDir);
                curDir = f.getParent();
            }

            //change directory  working directory
            else {
                File file = new File(args[1]);
                // if the input path is long path eg(/home/Folder1/Folder2)
                if (file.isAbsolute()) {
                    if (file.isDirectory()) {
                        curDir = args[1];
                    }

                    // if the changed directory is not found in the current directory or not found at all
                    else {
                        System.out.println("Error: No such file or directory");
                    }

                }
                // if the input path is short path eg(Folder2) and must be exit in the current directory
                else {
                    file = new File(curDir + File.separator + args[1]);

                    if (file.isDirectory()) {
                        curDir = file.getAbsolutePath();
                    } else {
                        System.out.println("Error: No such file or directory");
                    }
                }
            }
        }
    }

    // Function to create an empty directory
    public void mkdir(String[] args) {
        String dir = "";
        ArrayList<String> arr = new ArrayList();

        for (int i = 1; i < args.length; ++i) {
            File cur = new File(args[i]);

            if (cur.isDirectory()) {
                dir = cur.getAbsolutePath();
                break;
            } else {
                arr.add(args[i]);
            }
        }

        if (dir.equals("")) {
            dir = curDir;
        }

        for (String s : arr) {

            File file = new File(dir + File.separator + s);
            if (!file.exists()) {
                file.mkdir();
            } else {
                System.out.println("Error:" + s + "This directory already exist");
            }

        }
    }

    // printing files and directories in the directory
    public void ls() {
        File file = new File(curDir);
        for (String s : Objects.requireNonNull(file.list())) {
            System.out.println(s);
        }
    }

    //  printing files and directories in the directory in reverse order
    public void lsR() {
        File file = new File(curDir);
        String[] arr = file.list();
        assert arr != null;

        for (int i = arr.length - 1; i >= 0; --i) {
            System.out.println(arr[i]);
        }

    }


    public void echo(String[] args) {
        for (int i = 1; i < args.length; ++i) {
            System.out.print(args[i] + " ");
        }
        System.out.print("\n");
    }

    // create file in a given directory
    public void touch(String[] args) {
        if (args.length >= 3) {
            System.out.println("Error: Too many arguments");
        }

        File createdFile = new File(args[1]);
        if (!createdFile.isAbsolute()) {
            createdFile = new File(curDir + File.separator + args[1]);
        }

        if (!createdFile.exists()) {
            try {
                if (createdFile.createNewFile()) {
                    System.out.println(createdFile.getName() + " is created successfully");
                }
            } catch (Exception e) {
                System.out.println("An error occurred");
            }
        } else {
            System.out.println("File already exist");
        }
    }


    // copy 2 files first into second
    public void cp(String[] args) {
        if (args.length > 3) {
            System.out.println("Error: Too many arguments");
        } else {
            File file1 = new File(args[1]);
            File file2 = new File(args[2]);

            if (!(file1.isAbsolute())) {
                file1 = new File(curDir + File.separator + args[1]);
            }
            if (!(file2.isAbsolute())) {
                file2 = new File(curDir + File.separator + args[2]);
            }

            if (file1.exists() && file2.exists()) {
                try {
                    FileWriter myWriter = new FileWriter(file2);
                    Scanner myReader = new Scanner(file1);

                    String line;
                    while (myReader.hasNextLine()) {
                        line = myReader.nextLine();
                        myWriter.write(line);
                    }
                    myReader.close();
                    myWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                System.out.println("One of the files is not found");
            }

        }
    }

    // Function to remove a file using command
    public void rm(String[] args) {
        if (args.length >= 3) {
            System.out.println("Error: Too many arguments");
        } else {
            String fileName = args[1];
            File deletedFile = new File(fileName);

            if (!deletedFile.isAbsolute()) {
                deletedFile = new File(curDir + File.separator + args[1]);
            }

            if (!(deletedFile.exists() && deletedFile.isFile() && deletedFile.delete())) {
                System.out.println("Error: There is no file with this  name");
            }
        }
    }

    // print file or to files
    public void cat(String[] args) {
        if (args.length > 3) {
            System.out.println("Error: Too many arguments");
        } else {
            File file1 = new File(args[1]);
            if (!(file1.isAbsolute())) {
                file1 = new File(curDir + File.separator + args[1]);
            }

            try {
                printFile(file1);
            } catch (Exception e) {
                System.out.println("File not Found");
            }

            if (args.length > 2) {
                File file2 = new File(args[2]);
                if (!(file2.isAbsolute())) {
                    file2 = new File(curDir + File.separator + args[2]);
                }
                try {
                    printFile(file2);
                } catch (Exception e) {
                    System.out.println("File not Found");
                }
            }
        }
    }

    public void rmdir(String[] args) {
        String cur = "";

        if (args.length == 2 && args[1].equals("*")) {
            File file = new File(curDir);

            for (String path : Objects.requireNonNull(file.list())) {
                File curPath = new File(curDir + File.separator + path);
                if (curPath.isDirectory()) {
                    if (!curPath.delete()) {
                        System.out.println("Error: Filed to delete " + path + " directory is not empty");
                    }
                }
            }
        } else {
            for (int i = 1; i < args.length; ++i) {
                File file = new File(args[i]);
                if (!file.isAbsolute()) {
                    file = new File(curDir + File.separator + args[i]);
                }

                {
                    if (!(file.exists() && file.isDirectory() && file.delete())) {
                        System.out.println("Error: Filed to delete " + args[i] + " directory is not empty or not exit");
                    }
                }
            }
        }

    }

    // exit command
    public void exit() {
        System.exit(0);
    }

    public void setParser(Parser p) {
        this.parser = p;
    }

    //This method will choose the suitable command method to be called
    public void chooseCommandAction() {
        String command_name = parser.getCommandName();

        String[] args = parser.getArgs();
        if (command_name.equals("ls")) {
            if (args.length > 1 && args[1].equals("-r")) {
                command_name += " -r";
            }

        }
        switch (command_name) {
            case "echo": {
                echo(args);
                break;
            }


            case "cd": {
                cd(args);
                break;
            }

            case "mkdir": {
                mkdir(args);
                break;
            }

            case "pwd": {
                System.out.println(pwd());
                break;
            }

            case "ls": {
                ls();
                break;
            }

            case "ls -r": {
                lsR();
                break;
            }


            case "touch": {
                touch(args);
                break;
            }

            case "cat": {
                cat(args);
                break;
            }

            case "cp": {
                cp(args);
                break;
            }

            case "rm": {
                rm(args);
                break;
            }

            case "rmdir": {
                rmdir(args);
                break;
            }

            case "exit": {
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







































