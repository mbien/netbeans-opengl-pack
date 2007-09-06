/*
 * Settings.java
 *
 * Created on April 17, 2007, 2:49 PM
 */

package net.java.nboglpack.visualdesigner.tools;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
//import org.jdom.Attribute;
//import org.jdom.Document;
//import org.jdom.Element;
//import org.jdom.JDOMException;
//import org.jdom.JDOMFactory;
//import org.jdom.input.SAXBuilder;
//import org.jdom.output.XMLOutputter;

/**
 * This class is a tool to easily save and load settings in xml format
 *
 * @author Samuel Sperling
 */
public class Settings {
    
//    private File file;
//    private Document document;
//    private Element element;
//    private Settings parentSettings;
//    private XMLOutputter xmlOutputter;
//    private FileWriter fileWriter;
//    private HashMap<String, Settings> subSettings;
//    private String name;
//    
//    public Settings() {
//        this("settings");
//    }
//    
//    public Settings(String name) {
//        this.name = name;
//        this.element = new Element(name);
//        this.document = new Document(this.element);
//    }
//    
//    /**
//     * Creates a new instance of Settings
//     *
//     * @param file  XML file with all settings. This file will be used for
//     *              saving changes made to the settings.
//     */
//    public Settings(File file) throws JDOMException, IOException {
//        this.file = file;
//        this.document = (new SAXBuilder()).build(file);
//        element = document.getRootElement();
//        name = element.getName();
//    }
//    
//    protected Settings(Settings parentSettings, String name) {
//        this.parentSettings = parentSettings;
//        
//        // if name doesn't exist element should be null
//        // the element is then created by next getElement call
//        this.element = parentSettings.getElement().getChild(name);
//        if (this.element == null) {
//            // Node doesn't exist yet
//            this.element = new Element(name);
//            parentSettings.element.addContent(this.element);
//        }
//    }
//    
//    protected Settings(Settings parentSettings, Element element) {
//        this.parentSettings = parentSettings;
//        this.element = element;
//    }
//    
//    public void clear(String name) {
//        this.document.detachRootElement();
//        this.name = name;
//        this.document.setRootElement(new Element(name));
//    }
//    
//    /**
//     * Saves all settings to the file defined by setSaveFile()
//     * If this is just a child-branch of the main/root setting the whole
//     * settings will be saved to the file. not just this branch.
//     */
//    public void saveToFile() throws IOException, NullPointerException {
//            
//        if (document == null) {
//            this.parentSettings.saveToFile();
//        } else {
//            if (file == null)
//                throw new NullPointerException("Save file wasn't defined yet. Use SetSaveFile(...) to define it before calling saveToFile()");
//            
//            // This is the settings root node
//            if (xmlOutputter == null)
//                xmlOutputter = new XMLOutputter();
//            if (fileWriter == null)
//                fileWriter = new FileWriter(file);
//            xmlOutputter.output(document, fileWriter);
//        }
//    }
//    
//    protected void setElement(Element element) {
//        this.element = element;
//    }    
//    
//    protected Element getElement() {
//        if (this.element == null) {
//            this.element = new Element(name);
//            if (parentSettings == null) {
//                // root element
//                document.addContent(this.element);
//            } else {
//                // child element
//                parentSettings.element.addContent(this.element);
//            }
//        }
//        return this.element;
//    }
//
//    public File getSaveFile() {
//        return file;
//    }
//
//    public void setSaveFile(File file) {
//        this.file = file;
//        this.fileWriter = null;
//    }
//    
//    public String getValue() {
//        return this.element.getText();
//    }
//    
//    public String getAttributeValue(String name) {
//        return this.element.getAttributeValue(name);
//    }
//    
//    //---------------------------------------------------------------
//    // Reading Methods
//    //---------------------------------------------------------------
//    
//    /**
//     * Returns the value for the setting defined by the name parameter as boolean
//     *
//     * @param name Name of the setting to be returned
//     * @param defaultValue Value that will be returned in case the setting is
//     *  empty or not a valid boolean
//     */
//    public boolean getValueBoolean(String name, boolean defaultValue) {
//        try {
//            return getValueBoolean(name);
//        } catch(NumberFormatException ex) {
//            setValue(name, defaultValue);
//            return defaultValue;
//        } catch(NullPointerException ex) {
//            setValue(name, defaultValue);
//            return defaultValue;
//        }
//    }    
//    public boolean getValueBoolean(String name) {
//        return Boolean.parseBoolean(readElementContent(name));
//    }
//    
//    /**
//     * Returns the value for the setting defined by the name parameter as int
//     *
//     * @param name Name of the setting to be returned
//     * @param defaultValue Value that will be returned in case the setting is
//     *  empty or not a valid int number
//     */
//    public int getValueInt(String name, int defaultValue) {
//        try {
//            return getValueInt(name);
//        } catch(NumberFormatException ex) {
//            setValue(name, defaultValue);
//            return defaultValue;
//        } catch(NullPointerException ex) {
//            setValue(name, defaultValue);
//            return defaultValue;
//        }
//    }    
//    public int getValueInt(String name) {
//        return Integer.parseInt(readElementContent(name));
//    }
//    
//    /**
//     * Returns the value for the setting defined by the name parameter as long
//     *
//     * @param name Name of the setting to be returned
//     * @param defaultValue Value that will be returned in case the setting is
//     *  empty or not a valid long number
//     */
//    public long getValueLong(String name, long defaultValue) {
//        try {
//            return getValueLong(name);
//        } catch(NumberFormatException ex) {
//            setValue(name, defaultValue);
//            return defaultValue;
//        } catch(NullPointerException ex) {
//            setValue(name, defaultValue);
//            return defaultValue;
//        }
//    }
//    public long getValueLong(String name) {
//        return Long.parseLong(readElementContent(name));
//    }
//    
//    
//    /**
//     * Returns the value for the setting defined by the name parameter as float
//     *
//     * @param name Name of the setting to be returned
//     * @param defaultValue Value that will be returned in case the setting is
//     *  empty or not a valid float number
//     */
//    public float getValueFloat(String name, float defaultValue) {
//        try {
//            return getValueFloat(name);
//        } catch(NumberFormatException ex) {
//            setValue(name, defaultValue);
//            return defaultValue;
//        } catch(NullPointerException ex) {
//            setValue(name, defaultValue);
//            return defaultValue;
//        }
//    }
//    public float getValueFloat(String name) {
//        return Float.parseFloat(readElementContent(name));
//    }
//    
//    
//    /**
//     * Returns the value for the setting defined by the name parameter as Double
//     *
//     * @param name Name of the setting to be returned
//     * @param defaultValue Value that will be returned in case the setting is
//     *  empty or not a valid double number
//     */
//    public double getValueDouble(String name, double defaultValue) {
//        try {
//            return getValueDouble(name);
//        } catch(NumberFormatException ex) {
//            setValue(name, defaultValue);
//            return defaultValue;
//        } catch(NullPointerException ex) {
//            setValue(name, defaultValue);
//            return defaultValue;
//        }
//    }
//    public double getValueDouble(String name) {
//        return Double.parseDouble(readElementContent(name));
//    }
//    
//    /**
//     * Returns the value for the setting defined by the name parameter as String
//     * If the setting for this name is empty or an empty String
//     * the value of the parameter defaultValue will be returned.
//     *
//     * @param name Name of the setting to be returned
//     * @param defaultValue Value that will be returned in case the setting is
//     *  empty or a sting of length 0.
//     */
//    public String getValueString(String name, String defaultValue) {
//        String val = getValueString(name);
//        if (val == null || val.length() == 0) {
//            setValue(name, defaultValue);
//            return defaultValue;
//        } else
//            return val;
//    }
//    public String getValueString(String name) {
//        return readElementContent(name);
//    }
//    
//    /**
//     * Returns the value for the setting defined by the name parameter as String[]
//     * If the setting for this name is empty or an empty String
//     * the value of the parameter defaultValue will be returned.
//     *
//     * @param name Name of the setting to be returned
//     * @param defaultValue Value that will be returned in case the setting is
//     *  empty or a sting of length 0.
//     */
//    public String[] getValueStringArray(String name, String[] defaultValue) {
//        String[] val = getValueStringArray(name);
//        if (val == null) {
//            setValue(name, defaultValue);
//            return defaultValue;
//        } else
//            return val;
//    }
//    public String[] getValueStringArray(String name) {
//        return readElementContentArray(name);
//    }
//    
//    /**
//     * Returns the value for the setting defined by the name parameter as Color
//     *
//     * @param name Name of the setting to be returned
//     * @param defaultValue Value that will be returned in case the setting is
//     *  empty or not a valid Color
//     */
//    public Color getValueColor(String name, Color defaultValue) {
//        try {
//            return getValueColor(name);
//        } catch(NumberFormatException ex) {
//            setValue(name, defaultValue);
//            return defaultValue;
//        } catch(NullPointerException ex) {
//            setValue(name, defaultValue);
//            return defaultValue;
//        }
//    }    
//    public Color getValueColor(String name) {
//        return new Color(
//                Integer.parseInt(readAttributeContent(name, "r")),
//                Integer.parseInt(readAttributeContent(name, "g")),
//                Integer.parseInt(readAttributeContent(name, "b")),
//                Integer.parseInt(readAttributeContent(name, "a"))
//                );
//    }
//    
//    /**
//     * Returns the Attributevalue for the setting defined by the name and
//     * attributename parameter as boolean
//     *
//     * @param name Name of the setting that holds the attribute
//     * @param attributeName Name of the attribute to be returned
//     * @param defaultValue Value that will be returned in case the setting is
//     *  empty or not a valid boolean
//     */
//    public boolean getAttributeValueBoolean(String name, String attributeName, boolean defaultValue) {
//        try {
//            return getAttributeValueBoolean(name, attributeName);
//        } catch(NumberFormatException ex) {
//            setAttributeValue(name, attributeName, defaultValue);
//            return defaultValue;
//        } catch(NullPointerException ex) {
//            setAttributeValue(name, attributeName, defaultValue);
//            return defaultValue;
//        }
//    }    
//    public boolean getAttributeValueBoolean(String name, String attributeName) {
//        return Boolean.parseBoolean(readAttributeContent(name, attributeName));
//    }    
//    /**
//     * Returns the Attributevalue for the setting defined by the name and
//     * attributename parameter as int
//     *
//     * @param name Name of the setting that holds the attribute
//     * @param attributeName Name of the attribute to be returned
//     * @param defaultValue Value that will be returned in case the setting is
//     *  empty or not a valid int number
//     */
//    public int getAttributeValueInt(String name, String attributeName, int defaultValue) {
//        try {
//            return getAttributeValueInt(name, attributeName);
//        } catch(NumberFormatException ex) {
//            setAttributeValue(name, attributeName, defaultValue);
//            return defaultValue;
//        } catch(NullPointerException ex) {
//            setAttributeValue(name, attributeName, defaultValue);
//            return defaultValue;
//        }
//    }    
//    public int getAttributeValueInt(String name, String attributeName) {
//        return Integer.parseInt(readAttributeContent(name, attributeName));
//    }
//    
//    /**
//     * Returns the Attributevalue for the setting defined by the name and
//     * attributename parameter as long
//     *
//     * @param name Name of the setting that holds the attribute
//     * @param attributeName Name of the attribute to be returned
//     * @param defaultValue Value that will be returned in case the setting is
//     *  empty or not a valid long number
//     */
//    public long getAttributeValueLong(String name, String attributeName, long defaultValue) {
//        try {
//            return getAttributeValueLong(name, attributeName);
//        } catch(NumberFormatException ex) {
//            setAttributeValue(name, attributeName, defaultValue);
//            return defaultValue;
//        } catch(NullPointerException ex) {
//            setAttributeValue(name, attributeName, defaultValue);
//            return defaultValue;
//        }
//    }
//    public long getAttributeValueLong(String name, String attributeName) {
//        return Long.parseLong(readAttributeContent(name, attributeName));
//    }
//    
//    
//    /**
//     * Returns the Attributevalue for the setting defined by the name and
//     * attributename parameter as float
//     *
//     * @param name Name of the setting that holds the attribute
//     * @param attributeName Name of the attribute to be returned
//     * @param defaultValue Value that will be returned in case the setting is
//     *  empty or not a valid float number
//     */
//    public float getAttributeValueFloat(String name, String attributeName, float defaultValue) {
//        try {
//            return getAttributeValueFloat(name, attributeName);
//        } catch(NumberFormatException ex) {
//            setAttributeValue(name, attributeName, defaultValue);
//            return defaultValue;
//        } catch(NullPointerException ex) {
//            setAttributeValue(name, attributeName, defaultValue);
//            return defaultValue;
//        }
//    }
//    public float getAttributeValueFloat(String name, String attributeName) {
//        return Float.parseFloat(readAttributeContent(name, attributeName));
//    }
//    
//    
//    /**
//     * Returns the Attributevalue for the setting defined by the name and
//     * attributename parameter as double
//     *
//     * @param name Name of the setting that holds the attribute
//     * @param attributeName Name of the attribute to be returned
//     * @param defaultValue Value that will be returned in case the setting is
//     *  empty or not a valid double number
//     */
//    public double getAttributeValueDouble(String name, String attributeName, double defaultValue) {
//        try {
//            return getAttributeValueDouble(name, attributeName);
//        } catch(NumberFormatException ex) {
//            setAttributeValue(name, attributeName, defaultValue);
//            return defaultValue;
//        } catch(NullPointerException ex) {
//            setAttributeValue(name, attributeName, defaultValue);
//            return defaultValue;
//        }
//    }
//    public double getAttributeValueDouble(String name, String attributeName) {
//        return Double.parseDouble(readAttributeContent(name, attributeName));
//    }
//    
//    /**
//     * Returns the Attributevalue for the setting defined by the name and
//     * attributename parameter as String
//     * If the setting for this name is empty or an empty String
//     * the value of the parameter defaultValue will be returned.
//     *
//     * @param name Name of the setting that holds the attribute
//     * @param attributeName Name of the attribute to be returned
//     * @param defaultValue Value that will be returned in case the setting is
//     *  empty or a sting of length 0.
//     */
//    public String getAttributeValueString(String name, String attributeName, String defaultValue) {
//        String val = getAttributeValueString(name, attributeName);
//        if (val.length() == 0)
//            return defaultValue;
//        else
//            return val;
//    }
//    public String getAttributeValueString(String name, String attributeName) {
//        return readAttributeContent(name, attributeName);
//    }
//    
//    private String readElementContent(String name) {
//        return getElement().getChildText(name);
//    }
//    private String[] readElementContentArray(String name) {
//        List children = getElement().getChildren(name);
//        if (children == null || children.isEmpty()) return null;
//        
//        String[] values = new String[children.size()];
//        int i = 0;
//        for (Object elem : getElement().getChildren(name)) {
//            values[i++] = ((Element) elem).getText();
//        }
//        return values;
//    }
//    
//    private String readAttributeContent(String name, String attributeName) {
//        if (name == null)
//            return getElement().getAttributeValue(attributeName);
//        else
//            return getChild(name).getAttributeValue(attributeName);
//    }
//    
//    
//    //---------------------------------------------------------------
//    // Writing Values Methods
//    //---------------------------------------------------------------
//    
//    public void setValue(String name, boolean value) {
//        writeElementContent(name, Boolean.toString(value));
//    }    
//    
//    public void setValue(String name, int value) {
//        writeElementContent(name, Integer.toString(value));
//    }
//    
//    public void setValue(String name, long value) {
//        writeElementContent(name, Long.toString(value));
//    }
//    
//    public void setValue(String name, float value) {
//        writeElementContent(name, Float.toString(value));
//    }
//    
//    public void setValue(String name, double value) {
//        writeElementContent(name, Double.toString(value));
//    }
//    
//    public void setValue(String name, String value) {
//        writeElementContent(name, value);
//    }
//    
//    public void setValue(String name, String[] value) {
//        writeElementContent(name, value);
//    }
//    
//    public void setValue(String name, Color value) {
//        writeAttributeContent(name, "r", Integer.toString(value.getRed()));
//        writeAttributeContent(name, "g", Integer.toString(value.getGreen()));
//        writeAttributeContent(name, "b", Integer.toString(value.getBlue()));
//        writeAttributeContent(name, "a", Integer.toString(value.getAlpha()));
//    }
//    
//    //---------------------------------------------------------------
//    // Writing Attributes Methods
//    //---------------------------------------------------------------
//    
//    public void setAttributeValue(String name, String attributeName, boolean value) {
//        writeAttributeContent(name, attributeName, Boolean.toString(value));
//    }
//    
//    public void setAttributeValue(String name, String attributeName, int value) {
//        writeAttributeContent(name, attributeName, Integer.toString(value));
//    }
//    
//    public void setAttributeValue(String name, String attributeName, long value) {
//        writeAttributeContent(name, attributeName, Long.toString(value));
//    }
//    
//    public void setAttributeValue(String name, String attributeName, float value) {
//        writeAttributeContent(name, attributeName, Float.toString(value));
//    }
//    
//    public void setAttributeValue(String name, String attributeName, double value) {
//        writeAttributeContent(name, attributeName, Double.toString(value));
//    }
//    
//    public void setAttributeValue(String name, String attributeName, String value) {
//        writeAttributeContent(name, attributeName, value);
//    }
//    
//    private void writeElementContent(String name, String value) {
//        getChild(name).setText(value);
//    }
//    
//    private void writeElementContent(String name, String[] value) {
//        Element[] children = getAllChildren(name);
//        Element child;
//        for (int i = 0; i < value.length; i++) {
//            if (children != null && i < children.length) {
//                children[i].setText(value[i]);
//            } else {
//                child = new Element(name);
//                child.setText(value[i]);
//                getElement().addContent(child);
//            }
//        }
//    }
//    
//    private void writeAttributeContent(String elementName, String attributeName, String value) {
//        if (elementName == null)
//            element.setAttribute(attributeName, value);
//        else
//            getChild(elementName).setAttribute(attributeName, value);
//    }
//    
//    private Element getChild(String name) {
//        Element child = getElement().getChild(name);
//        if (child == null) {
//            child = new Element(name);
//            getElement().addContent(child);
//        }
//        return child;
//    }
//    
//    private Element[] getAllChildren(String name) {
//        List children = getElement().getChildren(name);
//        if (children == null || children.size() == 0) return null;
//        return (Element[]) children.toArray();
//    }
//    
//    public Settings getChildSettings(String name) {
//        
//        // to just have one instance per settings-branch cache the once that
//        // where already created
//        if (subSettings == null)
//            subSettings = new HashMap<String, Settings>();
//        Settings settings = subSettings.get(name);
//        if (settings == null) {
//            settings = new Settings(this, name);
//            subSettings.put(name, settings);
//        }
//        return settings;
//    }
//    
//    public boolean elementExists(String name) {
//        return getElement().getChild(name) != null;
//    }
//    
//    /**
//     * Adds an element to this settings even if it already exists.
//     * Doesn't use the caching, meaning that many setting instances can reffer
//     * to the same node in the settings-graph
//     */
//    public Settings addChildSettings(String name) {
//        Element element = new Element(name);
//        getElement().addContent(element);
//        return new Settings(this,element);
//    }
//    
//    /**
//     * Get's all children with the given name 
//     */
//    public Settings[] getChildrenSettings(String name) {
//        
//        //TODO: Needs to be enabled to caching in hashmap
//        List children = getElement().getChildren(name);
//        if (children.size() == 0) return null;
//        
//        Settings[] settingsCollection = new Settings[children.size()];
//        int i = 0;
//        for (Object element : children) {
//            settingsCollection[i++] = new Settings(this, (Element) element);
//        }
//        return settingsCollection;
//    }
//    
//    
//    /**
//     * Get child settings by a given path.
//     *
//     * @param path  The path to the child been for.
//     *              Childs have to be delimited by a forewardslash '/'
//     * @returns settings that represent the path
//     */
//    public Settings getChildSettingsByPath(String path) {
//        StringTokenizer tokenizer = new StringTokenizer(path, "/", false);
//        Settings currentChild = this;
//        while(tokenizer.hasMoreTokens()) {
//            currentChild = currentChild.getChildSettings(tokenizer.nextToken());
//        }
//        return currentChild;
//    }
//
//    public void output(File file) throws IOException {
//        (new XMLOutputter()).output(document, new FileWriter(file));
//    }
}
