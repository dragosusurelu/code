package parallelSearch;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;


public class Searcher implements Runnable {
	int id;
	CyclicBarrier b1;
	int x;
	Semaphore qsem, rsem;
	Searcher(int id, CyclicBarrier b1,  int x, Semaphore qsem, Semaphore rsem) {
		this.id = id;
		this.b1 = b1;
		this.x = x;
		this.qsem = qsem;
		this.rsem = rsem;
	}

	@Override
	public void run() {

		System.out.println("Start thread " + id);

		while(Main.pozitie == -1 && Main.q <= Main.r && Main.g >= 0)
		{
			
			//Main.j[id] = (int) ((Main.q-1) + (id-1) *(Math.pow(Main.N_SEARCHERS+1, (Main.g-1))));

			Main.j[id] = (int) ((Main.q-1) + (id-1) *((Main.r - Main.q + 1 )/(Main.N_SEARCHERS+1)));
			
			try {
	                b1.await();
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            } catch (BrokenBarrierException e) {
	                e.printStackTrace();
	            }
			

			if( Main.j[id] <= Main.r )
			{
				if(Main.v[Main.j[id]] == x ){
					synchronized (Main.pozitie) {
						Main.pozitie = Main.j[id];
					}
				}
				else if(Main.v[Main.j[id]] > x) {
					Main.c[id] = Main.st;
				}
				else {
					Main.c[id] = Main.dr;
				}
			}
			else
			{
				Main.j[id] = Main.r + 1;
				Main.c[id] = Main.st;
			}
			
			 try {
	                b1.await();
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            } catch (BrokenBarrierException e) {
	                e.printStackTrace();
	            }
			
			if(Main.c[id] != Main.c[id-1])
			{
				
				/*
				synchronized (Main.r) {
					Main.r= Main.j[id]-1;
				}
				*/
				
				try {
					rsem.acquire();
					Main.r= Main.j[id]-1;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				rsem.release();
				
				if( id > 1 ){
					//synchronized (Main.q) {
							// Main.q = Main.j[id-1] +1;
					//}	
						try {
							qsem.acquire();
							Main.q = Main.j[id-1] +1;
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						qsem.release();
						
					
					
				}
			}

			if(id == Main.N_SEARCHERS && Main.c[id] != Main.c[id+1])
			{
				// synchronized (Main.q) {
						// Main.q= Main.j[id]+1;
				//}
					try {
						qsem.acquire();
						Main.q= Main.j[id]+1;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					qsem.release();
				
			}
			if( id == Main.N_SEARCHERS) {
				Main.g--;
			}


			 try {
	                b1.await();
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            } catch (BrokenBarrierException e) {
	                e.printStackTrace();
	            }
		}
	}

}
/* DO NOT MODIFY */