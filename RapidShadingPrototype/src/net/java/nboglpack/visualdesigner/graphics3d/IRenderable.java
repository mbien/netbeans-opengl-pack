/*
 * IRenderable.java
 *
 * Created on 10. April 2006, 13:49
 *
 */

package net.java.nboglpack.visualdesigner.graphics3d;

/**
 * Objekte die dieses Interface unterstützen, können mit OpenGL gerendert werden.
 * @author SSperlin
 */
public interface IRenderable {
    /**
     * Rendert das Objekt auf ein dem Objekt bekanntes OpenGL Device
     */
    void render();
    /**
     * Rendert die Normalen des Objekts auf ein dem Objekt bekanntes OpenGL Device
     */
    void renderNormals();
}
