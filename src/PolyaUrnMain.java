import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class PolyaUrnMain
{
	//voting constants
	final static double VOTER_PERCENTAGE_CANDIDATE_A = 50; //This is a %, This is likely to be, but not exactly, the % of votes candidate A will receive based of polling 
	final static double VOTER_PERCENTAGE_CANDIDATE_B = 50; //This is a %, This is likely to be, but not exactly, the % of votes candidate B will receive based of polling 
	final static double VOTER_TURNOUT = 100; //This is a %, MUST be greater than 10
	final static int POPULATION = 100;
	
	//just for fun, introduce some fraudulent votes
	final static double FRAUD_COEFFICIENT_CANDIDATE_A = 5; //This is a %, how likely are candidate A's votes going to be fraudulent
	final static double FRAUD_COEFFICIENT_CANDIDATE_B = 3; //This is a %, how likely are candidate A's votes going to be fraudulent
	
	//if you want to be extra thorough, up the number of permutations
	final static int POLYA_URN_ITERATIONS = 1000; //how many permutations of Polya's Urn
	
	static double sampleCoefficient = 0.1;  //sample size is initially 10% of total votes
	
	
	public static void main(String[] args)
	{
		int totalVotes = (int) ((VOTER_TURNOUT/100)*POPULATION);
		
		int[] votes = vote(totalVotes); //election!
		
		boolean verified = false; //p<0.05
		
		int candidate1VotesPercent = (int)(((0.0+sumArray(votes))/totalVotes)*100);
		int candidate2VotesPercent = (int)(((0.0+totalVotes-sumArray(votes))/totalVotes)*100);
		
		//Print out results...the world is watching!!
		System.out.println("===================================================");
		System.out.println("CNN Projects Candidate A recieved "+candidate1VotesPercent+"% of the votes");
		System.out.println("CNN Projects Candidate B recieved "+candidate2VotesPercent+"% of the votes");
		System.out.println("====================================================\n");
		
		while(!verified && sampleCoefficient<1)
		{
			System.out.println("Sampling "+(int)((sampleCoefficient+0.0)*100)+"% of votes");
			
			int sampleSize = (int)(sampleCoefficient*totalVotes);
			
			//collect sample of votes
			int[] sample = new int[sampleSize];
			for(int i = 0; i<sampleSize; i++)
			{
				int index = new Random().nextInt(sampleSize);
				sample[i] = votes[index];
			}
			
			//begin Polya's Urn sampling
			int candidate1Wins = 0;
			int candidate2Wins = 0;
			
			for(int i = 0; i<POLYA_URN_ITERATIONS;i++)
			{
				int[] results = permutation(sample, totalVotes);
				if(results[0]>results[1]) candidate1Wins++;
				else candidate2Wins++;
			}
			
			//check to see if results are verified, i.e. the candidate that 'won' the vote also recieved more than 95% of wins through Polya's Urn
			boolean candidate1Verified = (candidate1VotesPercent>candidate2VotesPercent)&&(candidate1Wins>(int)(0.95*POLYA_URN_ITERATIONS));
			boolean candidate2Verified = (candidate1VotesPercent>candidate2VotesPercent)&&(candidate2Wins>(int)(0.95*POLYA_URN_ITERATIONS));
			
			//if we do not have a verification, increase the samplesize and try again
			if(candidate1Verified||candidate2Verified)
			{
				verified = true;
				System.out.println("\n=================================================");
				if(candidate1Verified)
					System.out.println("Candidate A has won the election with "+(int)(((0.0)+candidate1Wins)/POLYA_URN_ITERATIONS*100)+"% certainty\nWe only had to count "+(int)((sampleCoefficient+0.0)*100)+"% of the votes.");
				else
					System.out.println("Candidate B has won the election with "+(int)(((0.0)+candidate2Wins)/POLYA_URN_ITERATIONS*100)+"% certainty\nWe only had to count "+(int)((sampleCoefficient+0.0)*100)+"% of the votes.");
				System.out.println("=================================================");
				
			}
			else
				sampleCoefficient+=0.05;
		}
		
		if(verified==false)
			System.out.println("\n\nBayesian Inference is not robust enough to verify the results of this election. However, this \ndoes not necessarily mean the most popular candidate did not win. A recount by hand is recommended.");
	}

	private static int[] vote(int totalVotes2)
	{
		//the voting array (since this is a two party system) is an array of 0's and 1's
		int[] votes = new int[totalVotes2];
		for(int i = 0; i<totalVotes2; i++)
		{
			int vote = new Random().nextInt(100);
			if(vote<(int)(VOTER_PERCENTAGE_CANDIDATE_A+(FRAUD_COEFFICIENT_CANDIDATE_A-FRAUD_COEFFICIENT_CANDIDATE_B)))
				votes[i]++;
		}
		return votes;
	}
	
	private static int sumArray(int[] array)
	{
		int sum = 0;
		for(int i =0; i<array.length; i++)
			sum+=array[i];
		return sum;
	}
	
	private static int sumArrayList(ArrayList<Integer> arrayList)
	{
		int sum = 0;
		for(int i =0; i<arrayList.size(); i++)
			sum+=arrayList.get(i);
		return sum;
	}
	
	private static int[] permutation(int[] sample, int totalVotes)
	{
		int[] candidateVotes = new int[2];
		
		//represent votes as an ArrayList, that way we can add to it easily
		ArrayList<Integer> votes = new ArrayList<Integer>();
		for(int i = 0; i<sample.length;i++)
			votes.add(sample[i]);
		
		//begin process of simulating votes
		while(votes.size()<totalVotes)
		{
			//pick one from random
			Collections.shuffle(votes);
			int candidateVote = votes.get(0);
			
			//duplicate
			votes.add(candidateVote);
		}

		candidateVotes[0] = sumArrayList(votes);
		candidateVotes[1] = totalVotes - candidateVotes[0];
		return candidateVotes;
	}
}
