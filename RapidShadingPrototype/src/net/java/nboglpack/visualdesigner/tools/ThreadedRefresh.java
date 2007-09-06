/*
 * ThreadedRefresh.java
 *
 * Created on April 18, 2007, 4:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.java.nboglpack.visualdesigner.tools;

import java.util.ArrayList;

/**
 * This class holds a thread that fires an event in a defined frequency.
 * Classes can be attached to this class so they can do a refresh task to their
 * context.
 *
 * @author Samuel Sperling
 */
public class ThreadedRefresh implements Runnable {
    
    private Thread refreshThread;
    private long frequency;
    private float idleFrequencyFactor = 0f;
    private boolean isRunning = false;
    private ArrayList<IRefreshable> eventConsumers;
    
    public ThreadedRefresh() {
        this(50);
    }
    
    /** Creates a new instance of ThreadedRefresh */
    public ThreadedRefresh(long frequency) {
        this.frequency = frequency;
        this.refreshThread = new Thread(this, "Refresh Thread");
    }
    
    public void start() {
        isRunning = true;
        
        // Just start the thread if there are any consumers.
        // Otherwise the thread will be started as soon as a thread is connected
        if (this.eventConsumers != null && this.eventConsumers.size() > 0)
            refreshThread.start();
    }
    
    public void run() {
        long time;
        int activeConsumers;
        while (isRunning) {
            // Remember the consumed time to reduce from sleeptime
            time = System.currentTimeMillis();
            
            // No reason of keeping this thread alive if there are no consumers left
            if (eventConsumers == null) break;
            
            // Call attached consumers
            activeConsumers = 0;
            if (eventConsumers.size() > 0) {
                for (IRefreshable refreshable : eventConsumers) {
                    if (refreshable.needsRefresh()) {
                        refreshable.refresh();
                        activeConsumers++;
                    }
                }
            }
            
            // sleep for a while
            try {
                time += frequency - System.currentTimeMillis();
                if (activeConsumers <= 0)
                    time += frequency * idleFrequencyFactor;
                if (time > 0)
                    Thread.sleep(time);
//                System.out.print(time + " ");
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                break;
            }
        }
    }
    
    public boolean attachConsumer(IRefreshable refreshable) {
        if (this.eventConsumers == null) {
            this.eventConsumers = new ArrayList<IRefreshable>();
            if (isRunning)
                refreshThread.start();
        }
        return this.eventConsumers.add(refreshable);
    }
    
    public boolean removeConsumer(IRefreshable refreshable) {
        if (this.eventConsumers == null)
            return false;
        boolean removed = this.eventConsumers.remove(refreshable);
        
        // Completely remove eventConsumers to make thread stop
        if (this.eventConsumers.size() == 0) this.eventConsumers = null;
        return removed;
    }
    
    public void stop() {
        this.isRunning = false;
    }

    public String getThreadName() {
        return refreshThread.getName();
    }

    public void setThreadName(String threadName) {
        refreshThread.setName(threadName);
    }

    /**
     * @return  Number of milliseconds between each refreshment.
     */
    public long getFrequency() {
        return frequency;
    }

    /**
     * @param frequency  Number of milliseconds between each refreshment.
     */
    public void setFrequency(long frequency) {
        this.frequency = frequency;
    }

    public float getIdleFrequencyFactor() {
        return idleFrequencyFactor;
    }

    /**
     * @param idleFrequencyFactor  When there is now activity of refreshing.
     *  The refreshingrate can be reduced by the IdleFrequencyFactor.
     *  0.0 Means this feature is diabled
     */
    public void setIdleFrequencyFactor(float idleFrequencyFactor) {
        this.idleFrequencyFactor = idleFrequencyFactor;
    }

    
}
