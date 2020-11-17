package multipleProducersMultipleConsumersNBuffer;

import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Gabriel Gutu <gabriel.gutu at upb.ro>
 *
 */
public class Buffer {
    
    Queue queue;
    public static Semaphore consumerSemaphore;
    public static Semaphore producerSemaphore;
    
    public Buffer(int size) {
        queue = new LimitedQueue(size);
        consumerSemaphore = new Semaphore(0);
        producerSemaphore = new Semaphore(size);
    }

	void put(int value) {
        try {
            producerSemaphore.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Buffer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        synchronized(queue) {
            queue.add(value);
        }
        
        consumerSemaphore.release();

	}

	int get() {
        try {
            consumerSemaphore.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Buffer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int a;
        synchronized(queue) {
            a = (int)queue.poll();
        }
        
        producerSemaphore.release();
		return a;
	}
}
