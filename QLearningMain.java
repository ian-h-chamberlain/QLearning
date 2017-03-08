
public class QLearningMain {

	public static void main(String[] args) {
        long BEGIN = System.currentTimeMillis();
 
        
        QLearning.generateMap("trial1.txt", 10, 10);

        QLearning obj = new QLearning("trial1.txt");
        
        obj.printMap();
 
        /*
        obj.run();
        obj.printResult();
        ob1j.showPolicy();
        */
 
        long END = System.currentTimeMillis();
        System.out.println("Time: " + (END - BEGIN) / 1000.0 + " sec.");
	}

}
