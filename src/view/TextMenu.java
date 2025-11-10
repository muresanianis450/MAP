package view;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;



public class TextMenu {
    private final Map<String, Command> commands = new HashMap<>();

    public void addCommand(Command c){
        commands.put(c.getKey(), c);
    }

    private void printMenu() {
        for (Command c : commands.values()) {
            String line = String.format("%4s : %s", c.getKey(), c.getDescription());
            System.out.println(line);
        }
    }

    public void show(){
        Scanner scanner = new Scanner(System.in);
        while(true){
            printMenu();
            System.out.print(">");
            String key = scanner.nextLine();
            Command com = commands.get(key);
            if(com == null){
                System.out.println(key + " is not a valid command.");
                continue;
            }
            com.execute();
        }
    }

}
