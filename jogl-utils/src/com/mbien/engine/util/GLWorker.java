/*
 * GLWorker.java
 * 
 * Created on 16.08.2007, 15:54:38
 * 
 */

package com.mbien.engine.util;

/**
 *
 * @author Michael Bien
 */
public interface GLWorker {

    public void addWork(GLRunnable runnable);

    public void destroy();

    public void work();

    public void work(GLRunnable runnable);

}
