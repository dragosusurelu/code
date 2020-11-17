package parallelSearch;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;


public class Main {
	/* Size of array */
	public static final int N = 10;
	/* Number of threads */
	public static final int N_SEARCHERS = 5;
	/* Limits: q, r */
	public static Integer q = 1;
	public static Integer r = N;
	/* Number of iterations */
	public static Integer g = (int) (Math.log(N+1)/Math.log(N_SEARCHERS+1));
	/* Constants for left and right */
	public static int st = -1, dr = 1;
	/* Position of found element */
	public static Integer pozitie = -1;
	public static int []c = new int[N+2];
	public static int []j = new int[N+1];
	/* Array of numbers */
	public static int[] v = {2,5,8,12,15,19,23,27,30,67};
	public static void main(String[] args) {
		Semaphore  qsem = new Semaphore(1), rsem = new Semaphore(1);
		int searchFor = 23;
		CyclicBarrier b1 = new CyclicBarrier(N_SEARCHERS);
		Thread threads[] = new Thread[N_SEARCHERS];

		c[0] = dr;
		c[N+1] = st;

		for( int i = 0; i < N_SEARCHERS; i++)
			threads[i] = new Thread(new Searcher(i+1, b1, searchFor, qsem, rsem));

		for (int i = 0; i < N_SEARCHERS; i++)
			threads[i].start();

		for (int i = 0; i < N_SEARCHERS; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Value was found on this position: " +  pozitie);
	}

}