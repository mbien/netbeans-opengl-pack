/*
 * ProjectPersistor.java
 *
 * Created on June 2, 2007, 2:04 PM
 */

package net.java.nboglpack.visualdesigner;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
//import org.jdom.JDOMException;
import net.java.nboglpack.visualdesigner.shader.programs.IShaderProgramFactory;
import net.java.nboglpack.visualdesigner.shader.programs.IShaderProgram;
import net.java.nboglpack.visualdesigner.shader.variables.VariableValue;
import net.java.nboglpack.visualdesigner.shader.variables.VariablesCollection;
import net.java.nboglpack.visualdesigner.tools.Settings;

/**
 * Saves the state of the project to a binary output
 * This Class is not threadsave!
 *
 * @author Samuel Sperling
 */
public class ProjectPersistor {
    
    protected static String fileIdentification = "rsp";
    protected static int fileVersion = 0;
    private Writer writer;
    private Reader reader;
    private Settings settings;
    
    /**
     * Creates a new instance of ProjectPersistor
     */
    public ProjectPersistor() {
    }
    
//    public void outputToFile(File file) throws IOException, JDOMException {
//        settings.output(file);
//    }
//    public void setInputFile(File file) throws IOException, JDOMException {
//        settings = new Settings(file);
//    }
    
//    public void saveProject(Editor editor) throws PersistanceException {
//
//        settings = new Settings(fileIdentification);
//        settings.setAttributeValue(null, "desciption", "Rapid Shading Project");
//        
//        // Write Version
//        settings.setAttributeValue("project", "version", fileVersion);
//        
//        save("project",editor);
//    }
//    public void loadProject(Editor editor) throws PersistanceException {
//        int fileVersion = settings.getAttributeValueInt("project", "version");
//        if (fileVersion != this.fileVersion)
//            throw new PersistanceException("Fileversion is not compatiable");
//        load("project", editor);
//    }
//    
//    public void save(String name, IPersistable savable) throws PersistanceException {
//        if (savable == null) return;
//        Settings oldSettingsLevel = this.settings;
//        this.settings = this.settings.getChildSettings(name);
//        savable.saveState(this);
//        this.settings = oldSettingsLevel;
//    }
//    
//    /**
//     * Saves this element in a unique node
//     */
//    public void saveUnique(String name, IPersistable savable) throws PersistanceException {
//        if (savable == null) return;
//        Settings oldSettingsLevel = this.settings;
//        this.settings = this.settings.addChildSettings(name);
//        savable.saveState(this);
//        this.settings = oldSettingsLevel;
//    }
//    
//    public void load(String name, IPersistable loadable) throws PersistanceException {
//        Settings oldSettingsLevel = this.settings;
//        this.settings = this.settings.getChildSettings(name);
//        loadable.loadState(this);
//        this.settings = oldSettingsLevel;
//    }
////    
////    public IPersistable load(String name) throws IOException {
////        Settings oldSettingsLevel = this.settings;
////        this.settings = this.settings.getChildSettings(name);
////        loadable.loadState(this);
////        this.settings = oldSettingsLevel;
////    }
//    
//    
//    
//    //////////////////////////////////
//    // Common dataTypes and classes //
//    //////////////////////////////////
//    
//    public void save(String name, String value) throws PersistanceException {
//        settings.setValue(name, value);
//    }
//    
//    public String loadString(String name) throws PersistanceException {
//        return settings.getValueString(name);
//    }
//    
//    public void save(String name, int value) throws PersistanceException {
//        settings.setValue(name, value);
//    }
//    
//    public Integer loadInteger(String name) throws PersistanceException {
//        return settings.getValueInt(name);
//    }
//    
//    public void save(String name, long value) throws PersistanceException {
//        settings.setValue(name, value);
//    }
//    
//    public Long loadLong(String name) throws PersistanceException {
//        return settings.getValueLong(name);
//    }
//    
//    public void save(String name, float value) throws PersistanceException {
//        settings.setValue(name, value);
//    }
//    
//    public Float loadFloat(String name) throws PersistanceException {
//        return settings.getValueFloat(name);
//    }
//    
//    public void save(String name, double value) throws PersistanceException {
//        settings.setValue(name, value);
//    }
//    
//    public Double loadDouble(String name) throws PersistanceException {
//        return settings.getValueDouble(name);
//    }
//    
//    public void save(String name, boolean value) throws PersistanceException {
//        settings.setValue(name, value);
//    }
//    
//    public Boolean loadBoolean(String name) throws PersistanceException {
//        return settings.getValueBoolean(name);
//    }
//    
//    public void save(String name, Color value) throws PersistanceException {
//        settings.setValue(name, value);
//    }
//    
//    public Color loadColor(String name) throws PersistanceException {
//        return settings.getValueColor(name);
//    }
//
//    public void save(String name, Boolean[] value) throws PersistanceException {
//        settings.setAttributeValue(name, "count", value.length);
//        for (int i = 0; i < value.length; i++)
//            settings.setAttributeValue(name, "v"+i, value[i]);
//    }
//
//    public Boolean[] loadBooleanArray(String name) throws PersistanceException {
//        int count = settings.getAttributeValueInt(name, "count");
//        Boolean[] values = new Boolean[count];
//        for (int i = 0; i < count; i++)
//            values[i] = settings.getAttributeValueBoolean(name, "v" + i);
//        return values;
//    }
//
//    public void save(String name, Integer[] value) throws PersistanceException {
//        settings.setAttributeValue(name, "count", value.length);
//        for (int i = 0; i < value.length; i++)
//            settings.setAttributeValue(name, "v"+i, value[i]);
//    }
//
//    public Integer[] loadIntegerArray(String name) throws PersistanceException {
//        int count = settings.getAttributeValueInt(name, "count");
//        Integer[] values = new Integer[count];
//        for (int i = 0; i < count; i++)
//            values[i] = settings.getAttributeValueInt(name, "v" + i);
//        return values;
//    }
//
//    public void save(String name, Float[] value) throws PersistanceException {
//        settings.setAttributeValue(name, "count", value.length);
//        for (int i = 0; i < value.length; i++)
//            settings.setAttributeValue(name, "v"+i, value[i]);
//    }
//
//    public Float[] loadFloatArray(String name) throws PersistanceException {
//        int count = settings.getAttributeValueInt(name, "count");
//        Float[] values = new Float[count];
//        for (int i = 0; i < count; i++)
//            values[i] = settings.getAttributeValueFloat(name, "v" + i);
//        return values;
//    }
//
//    public void save(String name, Float[][] value) throws PersistanceException {
//        settings.setAttributeValue(name, "count1", value.length);
//        for (int i = 0; i < value.length; i++) {
//            settings.setAttributeValue(name, "count1-"+i, value[i].length);
//            for (int j = 0; j < value[i].length; j++) {
//                settings.setAttributeValue(name, "v"+i+"-"+j, value[i][j]);
//            }
//        }
//    }
//
//    public Float[][] loadFloatArray2(String name) throws PersistanceException {
//        int count = settings.getAttributeValueInt(name, "count1");
//        Float[][] values = new Float[count][];
//        for (int i = 0; i < count; i++) {
//            int count2 = settings.getAttributeValueInt(name, "count1-"+"i");
//            for (int j = 0; j < count2; j++) {
//                values[i][j] = settings.getAttributeValueFloat(name, "v"+i+"-"+j);
//            }
//        }
//        return values;
//    }
//
//    public void save(String name, NodeGraphPanel nodeGraph) throws PersistanceException {
//        save(name,nodeGraph);
//    }
//
//    public void save(String name, Point point) {
//        settings.setAttributeValue(name, "x", (int) Math.round(point.getX()));
//        settings.setAttributeValue(name, "y", (int) Math.round(point.getY()));
//    }
//
//    public Point loadPoint(String name) {
//        Point point = new Point();
//        point.x = settings.getAttributeValueInt(name, "x");
//        point.y = settings.getAttributeValueInt(name, "y");
//        return point;
//    }
//    
//    ////////////////////////
//    // Individual classes //
//    ////////////////////////
//
//    public void loadNodeGraphPanel(String name, NodeGraphPanel nodeGraphPanel) throws PersistanceException {
//        load(name,nodeGraphPanel);
//    }
//
//    public void save(String name, HashMap<String, GraphNode> nodes) throws PersistanceException {
//        settings.setAttributeValue(name, "size", nodes.size());
//        
//        Settings oldSettingsLevel = this.settings;
//        this.settings = settings.getChildSettings(name);
//        for (GraphNode node : nodes.values()) {
//            save("node", node);
//        }
//        this.settings = oldSettingsLevel;
//    }
//
//    public void loadGraphNodes(String name, NodeGraphPanel nodeGraphPanel) throws PersistanceException {
//        int size = settings.getAttributeValueInt(name, "size");
//        GraphNode node;
//        IShaderProgram[] shaderProgram = new IShaderProgram[size];
//        Settings oldSettingsLevel = this.settings;
//        this.settings = settings.getChildSettings(name);
//        Settings[] nodeSettings = settings.getChildrenSettings("node");
//        
//        // Add Nodes first
//        for (int i = 0; i < size; i++) {
//            this.settings = nodeSettings[i];
//            shaderProgram[i] = loadShaderProgram();
//            shaderProgram[i].getShaderNode().setName(settings.getValueString("name"));
//            nodeGraphPanel.addNode(shaderProgram[i].getShaderNode());
//        }
//        
//        // Load Node properties
//        for (int i = 0; i < size; i++) {
//            this.settings = nodeSettings[i];
//            shaderProgram[i].loadState(this);
//        }
//        
//        this.settings = oldSettingsLevel;
//    }
//
//    public void save(String name, GraphNode node) throws PersistanceException {
//        Settings oldSettings = this.settings;
//        this.settings = settings.addChildSettings(name);
//        if (node instanceof ShaderNode) {
//            save(((ShaderNode) node).getShaderProgram());
//        } else
//            throw new RuntimeException("Node is not a ShaderNode and can't be saved");
//        this.settings = oldSettings;
//    }
//
//    public void save(IShaderProgram shaderProgram) throws PersistanceException {
//        Class factory = shaderProgram.getFactoryClass();
//        
////        settings.setAttributeValue
//        //TODO: add support for saving PlugIn based Nodes. meaning: save the .jar filename
//        settings.setAttributeValue(null, "shaderprogramfactory", shaderProgram.getFactoryClass().getName());
//        if (shaderProgram.getVariant() != null)
//            settings.setAttributeValue(null, "variant", shaderProgram.getVariant());
//        shaderProgram.saveState(this);
//    }
//
//    private IShaderProgram loadShaderProgram() throws PersistanceException {
//        String factoryClassName = settings.getAttributeValueString(null, "shaderprogramfactory");
//        String variant = settings.getAttributeValueString(null, "variant");
//        
//        IShaderProgram shaderProgram;
//        try {
//            if (variant != null && variant.length() > 0)
//                shaderProgram = ((IShaderProgramFactory) Class.forName(factoryClassName).newInstance()).createShaderProgram(variant);
//            else
//                shaderProgram = ((IShaderProgramFactory) Class.forName(factoryClassName).newInstance()).createShaderProgram();
//        } catch (InstantiationException ex) {
//            throw new PersistanceException(ex);
//        } catch (ClassNotFoundException ex) {
//            throw new PersistanceException(ex);
//        } catch (IllegalAccessException ex) {
//            throw new PersistanceException(ex);
//        }
//        return shaderProgram;
//    }
//
//    public void save(String name, VariablesCollection vars) throws PersistanceException {
//        settings.setAttributeValue(name, "size", vars.size());
//        
//        Settings oldSettingsLevel = this.settings;
//        this.settings = this.settings.getChildSettings(name);
//        for (int i = 0; i < vars.size(); i++) {
//            saveUnique(name + "-element", vars.get(i));
//        }
//        this.settings = oldSettingsLevel;
//    }
//
//    public void loadVariablesCollection(String name, VariablesCollection vars) throws PersistanceException {
//        int size = settings.getAttributeValueInt(name, "size");
//        
//        Settings oldSettingsLevel = this.settings;
//        this.settings = settings.getChildSettings(name);
//        Settings[] varSettings = settings.getChildrenSettings(name + "-element");
//        int items = varSettings == null ? 0 : varSettings.length;
//        if (size != items)
//            System.out.println("collectionsize is inconsistant");
//        for (int i = 0; i < items; i++) {
//            this.settings = varSettings[i];
//            vars.get(this.settings.getValueString("name")).loadState(this);
//        }
//        
//        this.settings = oldSettingsLevel;
//    }
//
//    public void save(String name, VariableValue value) throws PersistanceException {
//        save(name,value);
//    }
//
//    public VariableValue loadVariableValue(String name) throws PersistanceException {
//        if (!settings.elementExists(name)) return null;
//        VariableValue variableValue = new VariableValue();
//        load(name, variableValue);
//        return variableValue;
//    }
//
//    public void save(String name, List list) throws PersistanceException {
//        settings.setAttributeValue(name, "size", list.size());
//        
//        Settings oldSettingsLevel = this.settings;
//        this.settings = this.settings.getChildSettings(name);
//        for (int i = 0; i < list.size(); i++) {
//            saveUnique(name + "-element", (IPersistable) list.get(i));
//        }
//        this.settings = oldSettingsLevel;
//    }
//    public void loadArrayList(String name, List list) throws PersistanceException {
//        int size = settings.getAttributeValueInt(name, "size");
//        
//        Settings oldSettingsLevel = this.settings;
//        this.settings = settings.getChildSettings(name);
//        Settings[] varSettings = settings.getChildrenSettings(name + "-element");
//        if (size != varSettings.length)
//            System.out.println("collectionsize is inconsistant");
//        
//        for (int i = 0; i < varSettings.length; i++) {
//            this.settings = varSettings[i];
//            ((IPersistable) list.get(i)).loadState(this);
//        }
//        
//        this.settings = oldSettingsLevel;
//    }

}
