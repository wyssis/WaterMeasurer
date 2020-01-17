import java.util.stream.IntStream;
import java.lang.Math;

public class Main {
	//accepts 2 space-separated arguments of integers, eg: java Main 3 5
    public static void main(String[] args) {
        int steps;
        int firstVolume = 3;
        int secondVolume = 5;
        MeasureWater measure = new MeasureWater();

        if (args.length >= 2) {
        	String regex = "[1-9]"; //default, use the other regex for numbers 1-100
        	//String regex = "[1-9][0-9]?|100";
        	if (!args[0].matches(regex) || !args[1].matches(regex)) {
        		System.out.println("Incorrect input: Acceptable input would be " +
        			"\ntwo space separated integers with a value between 1 and 9." + 
                    "\nUsing default values of 3 and 5.");
        	} else {
                firstVolume = Integer.parseInt(args[0]);
                secondVolume = Integer.parseInt(args[1]);
            }

            if (args.length > 2) {
                System.out.println("Ignoring any argument after the first two.");
                System.out.println();
            }
        } else {
            System.out.println("Input did not contain two integers." + 
                "\nUsing default values of 3 and 5.");
        }

        int smallBottle = Math.min(firstVolume, secondVolume); //volume of small bottle
        int bigBottle = Math.max(firstVolume, secondVolume); //volume of big bottle
        int[] range = IntStream.rangeClosed(1, (bigBottle+smallBottle)).toArray(); //requires java8
        if (firstVolume == secondVolume || (bigBottle%smallBottle == 0 && smallBottle != 1)) {
    		System.out.println("Incorrect input: " +
    			"Volumes can't be the same or multiples of each other (1 is allowed).");
    		return;
        }

        System.out.println(String.format("Small bottle: %d%nBig bottle: %d%n" + 
        	"Will calculate steps to find all volumes between 1 and %d", 
        	firstVolume, secondVolume, firstVolume+secondVolume));
        System.out.println();
        for (int targetLvl : range) {
	        steps = measure.findTarget(targetLvl, smallBottle, bigBottle);
	        System.out.println("Steps required for " + targetLvl + " litre(s): " + 
	        	(steps <= measure.MAX_STEPS ? steps : 
	        		"couldn't find in " + measure.MAX_STEPS + " steps"));
	        System.out.println();
		}
    }
}