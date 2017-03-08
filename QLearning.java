import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
 
public class QLearning {
    final DecimalFormat df = new DecimalFormat("#.##");
 
    // q-learning constants
    final double alpha = 0.1;
    final double gamma = 0.9;
    
    double[][] Q;	// q-value structure
    int[][] R;		// reward lookup structure
 
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
    	// set up the reward and q-values
    	R = new int[size * size][size * size];
    	Q = new double[size * size][size * size];
    	
    	for (int i=0; i<size * size; i++) {
    		for (int j=0; j<size * size; j++) {
    			R[i][j] = -1;	// discourage long paths
    			Q[i][j] = 0.0;	// initalize q-val to zero
    		}
    	}
    	
    	// now reward transition to goal point
    	for (int i=0; i < size*size; i++) {
    		R[i][startX * size + startY] = 100;
    	}
    }
    
 
    void run(int episodes) {
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
        for (int i = 0; i < episodes; i++) { // train episodes
            // Select random initial state
        	System.out.print("iteration ");
        	System.out.println(i);
        	System.out.println("starting from ");
        	
        	// start in random (valid) state
            int[] state = new int[] {rand.nextInt(size), rand.nextInt(size)};

            while (map[state[0]][state[1]]) {
            	state = new int[] {rand.nextInt(size), rand.nextInt(size)};
            }

            System.out.println(state[0] + "," + state[1]);

            while (state[0] != endX || state[1] != endY) // goal state
            {
            	
            	
            	// Select one among all possible actions for the current state
                List<int[]> actionsFromState = new ArrayList<>();
                
                // disallow blocked moves and out of bounds moves
                if (state[0] - 1 >= 0 && !map[state[0]-1][state[1]]) {
					actionsFromState.add(new int[] {state[0] - 1, state[1]});
                }
                if (state[1] - 1 >= 0 && !map[state[0]][state[1]-1]) {
					actionsFromState.add(new int[] {state[0], state[1]-1});
                }
                if (state[0] + 1 < size && !map[state[0]+1][state[1]]) {
					actionsFromState.add(new int[] {state[0] + 1, state[1]});
                }
                if (state[1] + 1 < size && !map[state[0]][state[1]+1]) {
					actionsFromState.add(new int[] {state[0], state[1]+1});
                }
                 
                // Selection strategy is random in this example
                // TODO change it to e-greedy
                int index = rand.nextInt(actionsFromState.size());
                int[] action = actionsFromState.get(index);
                
                // disallow invalid moves
                while (map[action[0]][action[1]]) {
					index = rand.nextInt(actionsFromState.size());
					action = actionsFromState.get(index);
                }
 
                // Action outcome is set to deterministic in this example
                // Transition probability is 1
                // what happens when the transition is probabilistic? 
                int[] nextState = action; // data structure
 
                // Using this possible action, consider to go to the next state
                double q = Q(state, action);
                double maxQ = maxQ(nextState);
                int r = R(state, action);
 
                double value = q + alpha * (r + gamma * maxQ - q);
                setQ(state, action, value);
 
                // Set the next state as the current state
                state = nextState;

				System.out.println(state[0] + "," + state[1]);
            }
        }
        
    }
 
    double maxQ(int[] state) {
    	// find maximum q for a qiven state
    	double max = Q(state, new int[]{0, 0});

    	for (int i=0; i<size; i++) {
			for (int j=0; j<size; j++) {
				double nextQ = Q(state, new int[]{i, j});
				if (nextQ > max) {
					max = nextQ;
				}
			}
    	}
    	
    	return max;
    }
 
    int policy(int[] state) {
    	// TODO decide action given state
    	// this should be epsilon-greedy, I think
        return 0;
    }
 
    double Q(int[] state, int[] action) {
    	int s = state[0] * size + state[1];
    	int a = action[0] * size + action[1];

    	return Q[s][a];
    }
 
    void setQ(int[] state, int[] action, double value) {
    	int s = state[0] * size + state[1];
    	int a = action[0] * size + action[1];
    	
    	Q[s][a] = value;
    }
 
    int R(int[] state, int[] action) {
    	int s = state[0] * size + state[1];
    	int a = action[0] * size + action[1];
    	
    	return R[s][a];
    }
 
    void printResult() {
    	// TODO print actual result with path
    	printMap();
    }
 
    void showPolicy() {
    	// TODO print policy
    	// it seems excessive to show all possible state->action pairs
    	// but may be useful for debugging
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
