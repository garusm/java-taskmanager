package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TaskManager {

    static final String fileName = "tasks.csv";
    static final String[] options = {"add", "remove", "list", "exit"};
    static String[][] tasks;

    public static void main(String[] args) {
        tasks = tasksLoader("tasks.csv");
        Options(options);
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNextLine()){
            String input = scanner.nextLine();
            switch(input){
                case"add":
                    addTaskLine();
                    break;
                case"remove":
                    removeTaskLine(tasks, getNumber());
                    System.out.println("Task deleted");
                    break;
                case"exit":
                    saveToFile(fileName, tasks);
                    System.out.println(ConsoleColors.RED + "Bye Friend");
                    System.exit(0);
                    break;
                case"list":
                    printTasks(tasks);
                    break;
                default:
                    System.out.println("Please select a correct option");
            }
            Options(options);
        }


    }
    public static void Options(String[] tab){
        System.out.println("\n" + ConsoleColors.BLUE_BOLD + "Please select an option: ");
        System.out.println(ConsoleColors.RESET + tab[0] + "\n" + tab[1] + "\n" + tab[2] + "\n" + tab[3]);

    }

    public static String[][] tasksLoader(String fileName){
        Path path = Paths.get(fileName);
        if(!Files.exists(path)){
            System.out.println("File not found");
            System.exit(0);
        }
        String[][] tasks = {{}, {}};
        try{
            List<String> outList = Files.readAllLines(path);
            tasks = new String[outList.size()][outList.get(0).split(", ").length];
            for(int i=0; i<outList.size(); i++){
                String[] split = outList.get(i).split(", ");
                for(int j=0; j< split.length; j++ ){
                    tasks[i][j] = split[j];
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        return tasks;
    }

    public static void printTasks(String[][] tasks){
        for(int i=0; i<tasks.length; i++){
            System.out.print(i+": ");
            for(int j=0; j<tasks[i].length; j++){
                System.out.print(tasks[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static void addTaskLine(){
        Scanner scanner = new Scanner(System.in);
        System.out.println(ConsoleColors.BLUE+"\nPlease add task description");
        String task = scanner.nextLine();
        System.out.println(ConsoleColors.BLUE+"\nPlease add task due date");
        String date = scanner.nextLine();
        System.out.println(ConsoleColors.BLUE+"\nIs your task important ?  true/false");
        String important = scanner.nextLine();

        tasks = Arrays.copyOf(tasks, tasks.length+1);
        tasks[tasks.length-1] = new String[3];
        tasks[tasks.length-1][0] = task;
        tasks[tasks.length-1][1] = date;
        tasks[tasks.length-1][2] = important;
    }

    private static void removeTaskLine(String[][] tab, int index){
        try {
            if(index < tab.length){
                tasks = ArrayUtils.remove(tab, index);
            }
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println(ConsoleColors.RED+"\nElement doesn't exist. Please select the right index");
        }

    }

    public static boolean isNumberGreater(String input){
        if(NumberUtils.isParsable(input)){
            return Integer.parseInt(input) >=0;
        }
        return false;
    }

    public static int getNumber(){
        Scanner scanner = new Scanner(System.in);
        System.out.println(ConsoleColors.RED_UNDERLINED+"\nPlease select number to remove");

        String input = scanner.nextLine();
        while (!isNumberGreater(input)){
            System.out.println(ConsoleColors.RED+"\nIncorrect argument. Please write number greater or equal 0: ");
            scanner.nextLine();
        }
        return Integer.parseInt(input);
    }

    private static void saveToFile(String fileName, String[][] tab){
        Path path = Paths.get(fileName);
        String[] lines = new String[tasks.length];
        for(int i=0; i<tab.length; i++){
            lines[i] = String.join(", ", tab[i]);
        }

        try {
            Files.write(path, Arrays.asList(lines));
        }catch (IOException e){
            e.printStackTrace();
        }

    }




}
