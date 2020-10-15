package project2cs360s2020;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

class people{
	public int ID;
	public double capacity; 
	public double happyA;
	public double happyB;
	public int team; 
	public int diverse;
	public people (int id, double capacity, double happyA, double happyB, int team, int diverse) {
		this.ID = id;
		this.capacity = capacity; 
		this.happyA = happyA; 
		this.happyB = happyB; 
		this.team = team; 
		this.diverse = diverse; 
		
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public double getCapacity() {
		return capacity;
	}
	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}
	public double getHappyA() {
		return happyA;
	}
	public void setHappyA(double happyA) {
		this.happyA = happyA;
	}
	public double getHappyB() {
		return happyB;
	}
	public void setHappyB(double happyB) {
		this.happyB = happyB;
	}
	public int getTeam() {
		return team;
	}
	public void setTeam(int team) {
		this.team = team;
	}
	public int getDiverse() {
		return diverse;
	}
	public void setDiverse(int diverse) {
		this.diverse = diverse;
	}
}
public class project2cs360s2020 {
	public static class Minimax {
	    private Minimax() {}
	    public static int minimaxDecision(State state, boolean method) {
            double resultValue = -9999999;
            State result = null; 
	    	for (State s : state.getActions()) {
	    		double val = minValue(s,-999999, 999999, method);
	    		if (val > resultValue ) {
	    			result = s;
	    			resultValue = val;
	    		}
	    	}
	    	//System.out.println(resultValue); // should be -84 for input 3
	    	return result.ID;
	    }
	    private static double maxValue(State state, double alpha, double beta, boolean method) { // add four line : ( add double alpha , double beta) 
	        if(state.isTerminal()){
	            return state.getUtility();
	        }
	        double value = -9999999;
	        for ( State s: state.getActions()) {
            	value = Math.max(value, minValue(s, alpha, beta,method));
            	// add if is minimax ; // if not alpha beta 
            	if (method) {
	            	if (value >= beta) {
	            		return value; 
	            	}
	            	beta = Math.max(beta,value);
            	}
            }
	        return value;
	    }
	    private static double minValue(State state, double alpha , double beta, boolean method ) { // add four line ( add double alpha , double beta) 
	        if(state.isTerminal()){
	            return state.getUtility();
	        }
            double value = 999999;
            for ( State s: state.getActions()) {
            	value = Math.min(value, maxValue(s, alpha, beta,method));
            	if (method) {
                	if (value <= alpha) {
                		return beta;
                	}
                	beta = Math.min(beta, value);
            	}
            }
            
            return value;
	    }
		private static class State{
		    List<people> A_team ; 
		    List<people> B_team ; 
		    List<people> no_team = new ArrayList<people>(); 
		    int ID = 0; 
		    boolean turn = true; 
		    public State (List<people> A_team, List<people> B_team , List<people> no_team,  int id) {
		    	this.A_team = new ArrayList<people>(A_team); 
		    	this.B_team = new ArrayList<people>(B_team); 
		    	this.no_team = new ArrayList<people>(no_team); 
		    	this.ID = id; 
		    	//add boolean to check turns 
		    }
		    
		    boolean isTerminal() {
		    	if ( A_team.size()==5 && B_team.size()==5) { // check A_team and b_team size 
		    		return true; 
		    	}
		        return false;
		    } 
		    // every leaf is from the perspective of team a 
		    Collection<State> getActions(){ 
		    	List<State> teammates = new LinkedList<>();
		    	if (!isTerminal()) { 
		    		for ( people people : no_team) {
		    			int id = people.ID; 
		    			List<people> temp = new ArrayList<people>(A_team);
			    	  	List<people> temp_B = new ArrayList<people>(B_team);
					    List<people> remove_temp = new ArrayList<people>(no_team);
					    State s = new State (temp, temp_B, remove_temp, id); 
		    		    s.no_team.remove(people);
		    		    
		    		    if (A_team.size()==B_team.size()) { // a turn 
		    		    // pass an argument - to get action - changing turns
				    		s.A_team.add(people); 
				    	}
		    			else if (A_team.size()>B_team.size()){
		    				s.turn = false;
		    				s.B_team.add(people); 
		    			}
		    			teammates.add(s);
	    			}
		    	}
			  // System.out.println("size of teammates " + teammates.size());
		       return teammates;
		    }
		    
		    boolean diverseCheck(List<people> A_team) {
		    	for (int i = 0 ; i < A_team.size()-1; i++) {
		    		for ( int j = i+1 ; j < A_team.size(); j++)
		    		{
		    			if (A_team.get(i).diverse == A_team.get(j).diverse ) {
		    				return false; 
		    			}
		    		}
		    	}
		    	return true; 
		    }
		    
		    double getUtility() {// calculate power and subtract it 
		    	double U = 0;
		    	double powerA = 0;
		    	double powerB = 0 ; 
		    	
	    		for (people people:A_team) {
		    	//	powerA += people.capacity * people.happyA;
		    		U += people.capacity * people.happyA;
		    	}
		    	for (people people:B_team) {
		    		//powerB += people.capacity * people.happyB;
		    		U -= people.capacity * people.happyB;
		    	}
		    	
		    	// method : diverse
		    	if ( diverseCheck(A_team)) {
		    		//powerA +=120; 
		    		U +=120;
		    	}
		    	if (diverseCheck(B_team) ) {
		    		//powerB +=120;
		    		U-=120;
		    	}
		    //	U = powerA - powerB; 
		    	//System.out.println(U); 
//		    	if ( no_team.size()%2 ==0 ) {
//		    		return U; 
//		    	}
//		    	else {
//		    		return 0-U; 
//		    	}
		    	return U; 
		    	//power A == -powerB
		    }
		}
	}// minimax class : https://www.e4developer.com/2018/09/23/implementing-minimax-algorithm-in-java/
	
	public static void main(String[] args) throws Exception{
	    List<people> A_team = new ArrayList<people>(); 
	    List<people> B_team = new ArrayList<people>(); 
	    List<people> no_team = new ArrayList<people>();
		int number  = 0; 
		boolean ab = false; 
		try {
		      BufferedReader reader = new BufferedReader(new FileReader("src/project2cs360s2020/input.txt"));
		      Scanner scan = new Scanner(reader);
		      while (scan.hasNextLine()) {
		    	   number = scan.nextInt();
		    	  scan.nextLine();
		    	  String method = scan.nextLine();
		    	  // declare state
		    	  for ( int i = 0 ; i < number; i ++) {
						String[] info = scan.nextLine().split(","); 
						int id = Integer.parseInt(info[0]); 
						double capacity = Double.parseDouble(info[1]); 
						double happyA = Double.parseDouble(info[2]);
						double happyB = Double.parseDouble(info[3]); 
						int pickedTeam = Integer.parseInt(info[4]); 
						int diverse = id%10;
						// construct into "people"
						people candidate = new people ( id,  capacity,  happyA,  happyB,  pickedTeam,  diverse) ; 
						// can update state 
						if (pickedTeam == 1) {
							A_team.add(candidate);
						}
						else if (pickedTeam ==2) {
							B_team.add(candidate);
						}
						// sorting : 
						else {
							no_team.add(candidate); 
							Collections.sort(no_team,(t1,t2)->(t1.ID - t2.ID));
						}
				 }
		    	  
		    	  if (method.equalsIgnoreCase("ab")) {
		    		  ab=true;
		    	  }else {
		    		  ab=false;
		    	  }
		    	 
		      }
		   
		    scan.close();
		    int id = 0; 
	    	Minimax.State s = new Minimax.State(A_team, B_team, no_team, id); 
 	       	int decision = Minimax.minimaxDecision(s,ab);
	        System.out.println(decision);
	        String hi = Integer.toString(decision);
	       BufferedWriter output = null;
	       try {
	            File file = new File("output.txt");
	            output = new BufferedWriter(new FileWriter(file));
	            output.write(decision);
	        } catch ( IOException e ) {
	            e.printStackTrace();
	        } finally {
	          if ( output != null ) {
	            output.close();
	          }
	        }
		  } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
	}
}
}
