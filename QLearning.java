import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Random;
 
/**
 * @author Kunuk Nykjaer
 */
public class QLearning {
    final DecimalFormat df = new DecimalFormat("#.##");
 
    // path finding
    final double alpha = 0.1;
    final double gamma = 0.9;
 
    int size;
    boolean[][] map;
    int startX, startY;
    int endX, endY;

    public QLearning(String filename) {
    	// read in the file describing the map
    	try {
    		FileReader r = new FileReader(filename);
    		BufferedReader reader = new BufferedReader(r);
    		
    		String[] line = reader.readLine().split(",");
    		startX = new Integer(line[0]);
    		startY = new Integer(line[1]);
    		
    		line = reader.readLine().split(",");
    		endX = new Integer(line[0]);
    		endY = new Integer(line[1]);
    		
			size = new Integer(reader.readLine());
    		map = new boolean[size][size];
    		
    		for (int i=0; i<size; i++) {
    			line = reader.readLine().split(",");
    			
    			for (int j=0; j<size; j++) {
    				map[i][j] = new Integer(line[j]) == 1;
    			}
    		}
    		
    		reader.close();
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    	}
    	
        init();
    }
 
    public void init() {        
    	// TODO set up the reward lookup
    }
    
 
    void run() {
        /*
         1. Set parameter , and environment reward matrix R 
         2. Initialize matrix Q as zero matrix 
         3. For each episode: Select random initial state 
            Do while not reach goal state o 
                Select one among all possible actions for the current state o 
                Using this possible action, consider to go to the next state o 
                Get maximum Q value of this next state based on all possible actions o 
                Compute o Set the next state as the current state
         */
 
        // For each episode
        Random rand = new Random();
        for (int i = 0; i < 10000; i++) { // train episodes
            // Select random initial state
        	System.out.println();
        	System.out.print("iteration");
        	System.out.print(i);
        	System.out.println();
        	System.out.print("starting from ");
        	System.out.println();
        	
        	// start with random state
            int state = rand.nextInt();
            System.out.print(state);
            System.out.println();

            while (state != 0) // goal state
            {
            	
            	System.out.print(state);
            	
            	// Select one among all possible actions for the current state
                int[] actionsFromState = new int[] { 0, 0} ;
                 
                // Selection strategy is random in this example
                // change it to e-greedy
                int index = rand.nextInt(actionsFromState.length);
                int action = actionsFromState[index];
 
                // Action outcome is set to deterministic in this example
                // Transition probability is 1
                // what happens when the transition is probabilistic? 
                int nextState = action; // data structure
 
                // Using this possible action, consider to go to the next state
                double q = Q(state, action);
                double maxQ = maxQ(nextState);
                int r = R(state, action);
 
                double value = q + alpha * (r + gamma * maxQ - q);
                setQ(state, action, value);
 
                // Set the next state as the current state
                state = nextState;
            }
            System.out.print(state);
        }
        
    }
 
    double maxQ(int s) {
    	// TODO get maximum Q value given state s
    	return 0.0;
    }
 
    int policy(int state) {
    	// TODO decide action given state
        return 0;
    }
 
    double Q(int s, int a) {
    	// TODO lookup q value
    	return 0.0;
    }
 
    void setQ(int s, int a, double value) {
    	// TODO update Q value
    }
 
    int R(int s, int a) {
    	// TODO lookup reward function
    	return 0;
    }
 
    void printResult() {
    	// TODO print result
    }
 
    void showPolicy() {
    	// TODO print policy
    }
    
    void printMap() {
    	System.out.println("Map visualization: ");
    	for (int i=0; i<size * 2 + 2; i++) {
    		System.out.print("-");
    	}
    	System.out.println("");
    	for (int i=0; i<size; i++) {
			System.out.print("|");
    		for (int j=0; j<size; j++) {
    			if (i == startX && j == startY) {
    				System.out.print("S ");
    			}
    			else if (i == endX && j == endY) {
    				System.out.print("G ");
    			}
    			else if (map[i][j]) {
    				System.out.print("X ");
    			}
    			else {
    				System.out.print("  ");
    			}
    		}
    		System.out.println("|");
    	}
    	for (int i=0; i<size * 2 + 2; i++) {
    		System.out.print("-");
    	}
    	System.out.println("");
    }

    public static void generateMap(String name, int size, int blocked) {
    	boolean[][] positions = new boolean[size][size];
    	
    	for (int i=0; i<size; i++) {
    		for (int j=0; j<size; j++) {
    			positions[i][j] = false;
    		}
    	}
    	
    	Random rn = new Random();
    	int count = 0;
    	
    	while (count < blocked) {
    		int i = rn.nextInt(size);
    		int j = rn.nextInt(size);
    		if (!positions[i][j]) {
    			positions[i][j] = true;
    			count++;
    		}
    	}
    	
    	// find random start and end positions
    	int[] start, end;
		int i = rn.nextInt(size);
		int j = rn.nextInt(size);
		while (positions[i][j]) {
			i = rn.nextInt(size);
			j = rn.nextInt(size);
		}
		start = new int[] {i, j};

		i = rn.nextInt(size);
		j = rn.nextInt(size);
		while (positions[i][j]) {
			i = rn.nextInt(size);
			j = rn.nextInt(size);
		}
		end = new int[] {i, j};
    	
    	// save the result
		try {
			PrintWriter writer = new PrintWriter(name);
			writer.println(start[0] + "," + start[1]);
			writer.println(end[0] + "," + end[1]);
			writer.println(size);
			
			for (i=0; i<size; i++) {
				for (j=0; j<size; j++) {
					writer.print((positions[i][j] ? "1" : "0") + ",");
				}
				writer.println();
			}
			
			writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
    }
}
