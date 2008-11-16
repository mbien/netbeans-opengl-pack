/*
* "Created on Sep 30, 2008  8:12:40 PM">
*/

package com.mbien.engine.glsl;

/**
 * A shader fragment is a part of an shader and represents in most cases the content of a file.
 * @author Michael Bien
 */
public class CodeFragment<T> {

    public final String name;
    public final String source;
    public final T sourceObj;

    public CodeFragment(String name, String source, T codeSourceObj) {
        this.name = name;
        this.source = source;
        this.sourceObj = codeSourceObj;
    }


}
