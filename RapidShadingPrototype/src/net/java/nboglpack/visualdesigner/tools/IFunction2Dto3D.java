/*
 * IFunction2D.java
 *
 * Created on 16. Juni 2006, 10:59
 *
 */

package net.java.nboglpack.visualdesigner.tools;

import javax.vecmath.Point3f;

/**
 * Objekte die diese Schnittstelle unterstützen müssen eine Funktion bereitstellen,
 * die eine 2D Koordinate alseingabe und eine 3D Koordinate als ausgabe verarbeitet.
 * @author SSperlin
 */
public interface IFunction2Dto3D {
    /**
     * Errechnet aus einer 2D Koordinate eine 3D Koordinate.
     * @param u Erster Parameter
     * @param v Zweiter Parameter
     * @return Gibt die berechnete 3D Koordinate zurück.
     */
    public Point3f getValue(float u, float v);
}
