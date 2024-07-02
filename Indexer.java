/**
 * Minecraft datapack indexer
 * This program allows you to find and replace text across an entire datapack.
 * by Devin Illy
 * Version 1.7
 * New this version
 * - Count command now skips files without .mcfunction extension
 * - Fixed a bug where the replace function would append a new line to the end of every function
 * DEVELOPERS NOTE:
 * My code is garbage and so are my comments, if you have any questions my discord is devini15
 * Thank you for using my funny little app. Please leave a star!
 */

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.List;

public class Indexer extends ConsoleColors {
    //Imagine commenting your global variables, couldn't be me.
    private static Scanner userInput = new Scanner(System.in);
    private static FileInputStream inStream;
    private static Scanner fileRead;
    private static File functionsDir = new File("functions");
    private static File[] functions;
    public static void main(String[] args) {
        try {
            functions = functionsDir.listFiles(); //Stores all functions in an array
            System.out.println(GREEN + "Welcome to Datapack Indexer v1.7" + RESET);
            System.out.println("This program allows you to sort through the function files of a datapack");
            System.out.println("Run this program from the directory containing the " + CYAN + "functions" + RESET + " folder.");
            System.out.println(GREEN + "Select a function:" + RESET);
            promptUser();
            while(true) { //If you weren't meant to use while(true), they wouldn't have included it in java.
                System.out.println(GREEN + "Do another operation?");
                promptUser();
            }
        }catch(Exception e){
            //Probably user error
            System.out.println("Encountered an unexpected error");
            e.printStackTrace();
        }
    }
    /**
    * Literally just asks the user what they want to do. Find, Replace, Count, or Exit.
    * If the selection requires further input, it is collected here and passed to the appropriate handler method
    */
    private static void promptUser(){
        System.out.print(CYAN + "1. Find\n2. Replace\n3. Count\n4. Exit" + BLUE + "\n> " + RESET);
        String response = userInput.nextLine(); //Collect menu selection
        if(response.equals("1")||response.equalsIgnoreCase("find")){
            System.out.print("Enter the term you would like to search for." + BLUE + "\n> " + RESET);
            findTerm(userInput.nextLine());
        }else if(response.equals("2")){
            System.out.print("Enter the term you would like to replace" + BLUE + "\n> " + RESET);
            String oldTerm = userInput.nextLine();
            System.out.print("Enter the replacement term" + BLUE + "\n> " + RESET);
            replaceTerm(oldTerm, userInput.nextLine());
        }else if(response.equals("3")){
            countCode();
        }else System.exit(0);
    }

    /**
     * Finds every instance of a given term across the datapack and prints the filename and line number of each to console.
     * This includes hits in files that are not MCFUNCTION files.
     * @param term The term we are searching the datapack for.
     */
    private static void findTerm(String term){
        long startTime = System.nanoTime(); //Benchmarking
        StringBuilder finalOutput = new StringBuilder(); //This just saves a lot of printing which I think should save runtime?
        int line; //line number of current line being looked at
        int numFound = 0; //total number of instances found
        String currentLine; //current line being looked at
        System.out.println(YELLOW + "Working...\n" + RESET);
        try{
            for(File f : functions){
                inStream = new FileInputStream(f);
                fileRead = new Scanner(inStream);
                line = 0;
                while(fileRead.hasNextLine()){
                    line++;
                    currentLine = fileRead.nextLine();
                    if(currentLine.contains(term)){ //appends a string representation of each instance to the final output
                        numFound++;
                        finalOutput.append(f.getName());
                        finalOutput.append(" Line ").append(line).append("\n");
                    }
                }
            }
            System.out.println(finalOutput);
            // The rest of this method is just for benchmarking
            long finalTime = System.nanoTime() - startTime;
            int benchmark = (int)(finalTime/1000000);
            System.out.println(GREEN + "DONE" + RESET);
            System.out.println("Found on " + CYAN + numFound + RESET + " lines in " + CYAN + benchmark + RESET + " ms");
        }catch (FileNotFoundException e){
            //I don't know why this would get thrown, sorry for your loss.
            System.out.println(RED + "File Not Found Error" + RESET);
        }

    }

    /**
     * Replaces every instance of a given term with a new term in every file.
     * Good for small syntax changes or item renaming.
     * This will impact all files, even if they are not MCFUNCTIONs
     * @param old Term to be replaced
     * @param replacement Term that will replace every instance of old
     */
    private static void replaceTerm(String old, String replacement){
        long startTime = System.nanoTime();
        List<String> lines; //Every command in the current file
        FileOutputStream output; //Out stream for PrintWriter
        PrintWriter printer; //Replaces text in selected file
        int numFound = 0; //Track number of replacements
        System.out.println(YELLOW + "Working...\n" + RESET);
        try {
            for (File f : functions) {
                lines = Files.readAllLines(f.toPath(), StandardCharsets.UTF_8); //stores all lines of the current file in a List
                for(String s : lines){
                    if(s.contains(old)){ //Replace the old term with the replacement term on every line with the old term
                        numFound++;
                        lines.set(lines.indexOf(s), s.replaceAll(old, replacement));
                    }
                }
                output = new FileOutputStream(f);
                printer = new PrintWriter(output);
                //Replace the contents of the current file with the contents of the newly modified lines
                for(String s : lines){
                    //This if/else block only exists to resolve an issue where using the replace command would append
                    //a new line to the end of every function, which would cause them to stop working
                    if(lines.indexOf(s) < lines.size() - 1){
                        printer.println(s);
                    }else printer.print(s);
                }
                printer.close();
                output.close();
            }
            //Benchmarking ↓
            long finalTime = System.nanoTime() - startTime;
            int benchmark = (int)(finalTime/1000000);
            System.out.println(GREEN + "DONE" + RESET);
            System.out.println("Replaced on " + CYAN + numFound + RESET + " lines in " + CYAN + benchmark + RESET + " ms");
        }catch (IOException e){
            //I don't print stack trace here because if you get this error you're probably SOL
            System.out.println(RED + "IO Error" + RESET);
        }
    }

    /**
     * Counts the total number of functions and commands in your datapack.
     * This will ignore any file that does not have the .mcfunction extension.
     */
    private static void countCode() {
        int fileCount = 0; //Tracks number of files
        int lineCount = 0; //Tracks the number of lines
        System.out.println(YELLOW + "Working..." + RESET);
        long startTime = System.nanoTime();
        try {
            List<String> lines;
            functions = functionsDir.listFiles(); //stores all functions in an array
            assert functions != null; //My compiler just taught me what an assert statement does!
            for (File f : functions) {
                if(f.toPath().toString().endsWith(".mcfunction")) {
                    fileCount++;
                    lines = Files.readAllLines(f.toPath(), StandardCharsets.UTF_8);
                    lineCount += lines.size();
                }
            }
            //Hell yeah, brother, it's benchmarkin' time B)
            long finalTime = System.nanoTime() - startTime;
            int benchmark = (int)(finalTime/1_000_000);
            System.out.println(GREEN + "DONE" + RESET);
            System.out.println("Found " + CYAN + fileCount + RESET + " functions and " + CYAN + lineCount + RESET + " commands.");
            System.out.println("Completed in " + CYAN + benchmark + RESET + " ms");
        }catch(IOException e){
            //uhhhhhhhhhhhhhhhh
            System.out.println(RED + "IO Error" + RESET);
            e.printStackTrace();
        }
    }
}
