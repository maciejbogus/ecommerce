package pl.mbogus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args){
        List<String> names = Arrays.asList("Jakub","Anna");
        List<String> ladies = new ArrayList<String>();

        for(String name: names){
            if(name.endsWith("a")){
                ladies.add(name);
            }
        }

        Greeter greeter = new Greeter();

        for(String ladyName: ladies){
            greeter.greet(ladyName);
        }

        names.stream()
            .filter(name -> name.endsWith("a"))
            .filter(name -> name.startsWith("A"))
            .forEach(greeter::greet);
    }
}
