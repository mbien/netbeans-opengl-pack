/*
 * Editor.java
 *
 * Created on April 12, 2007, 5:16 PM
 */

package net.java.nboglpack.visualdesigner;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Date;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
//import org.jdom.JDOMException;
import net.java.nboglpack.visualdesigner.preview.PreviewPanel;
import net.java.nboglpack.visualdesigner.shader.exporter.ExportingExeption;
import net.java.nboglpack.visualdesigner.shader.exporter.GLSLCodeExporter;
import net.java.nboglpack.visualdesigner.shader.exporter.IShaderCodeExporter;
import net.java.nboglpack.visualdesigner.shader.programs.CollectionLoaderPanel;
import net.java.nboglpack.visualdesigner.shader.programs.FragmentInputFactory;
import net.java.nboglpack.visualdesigner.shader.programs.FragmentOutputFactory;
import net.java.nboglpack.visualdesigner.shader.programs.IShaderProgram;
import net.java.nboglpack.visualdesigner.shader.programs.VertexInputFactory;
import net.java.nboglpack.visualdesigner.shader.programs.VertexOutputFactory;
import net.java.nboglpack.visualdesigner.shader.variables.VariablesCollection;
import net.java.nboglpack.visualdesigner.tools.ExtensionFileFilter;
import net.java.nboglpack.visualdesigner.tools.Settings;
import net.java.nboglpack.visualdesigner.tools.ThreadedRefresh;

/**
 * Editor for rapid shader creation.
 *
 * @author Samuel Sperling
 */
public class Editor extends JFrame implements IPersistable {
    private Settings styleSettings;
    private Settings generalSettings;
    private Settings pathSettings;
    private IShaderCodeExporter activeExporter;
    private IShaderProgram fragmentOutputNode;
    private IShaderProgram fragmentInputNode;
    private IShaderProgram vertexOutput;
    private IShaderProgram vertexInput;
    private ProjectPersistor projectPersistor;
    private boolean showVariableNames;
    private About about;
    
    public static Settings mainSettings;
    public static ThreadedRefresh constantRefresher;
    public static VariablesCollection<GlobalVariable> globalVariables;
    
    /** Creates a new instance of Editor */
    public Editor() throws Exception {
        loadPlugIns();
        loadSettings();
        
        // Start RefreshThread
        constantRefresher = new ThreadedRefresh(
                /*generalSettings.getAttributeValueInt("RefreshThread", "refreshFrequency",*/ 40);//);
        constantRefresher.setIdleFrequencyFactor(
                /*generalSettings.getAttributeValueInt("RefreshThread", "IdleFrequencyFactor",*/ 2);//);
        constantRefresher.start();
        
        // Create global variable collection
        globalVariables = new VariablesCollection<GlobalVariable>();
        
        initComponents();
        createMainNodes();
    }

    private void loadSettings() throws Exception {
//        File settingsFile = new File("settings.xml");
//        try {
//            mainSettings = new Settings(settingsFile);
//        } catch (IOException ex) {
            mainSettings = new Settings();
//            mainSettings.setSaveFile(settingsFile);
//        } catch (JDOMException ex) {
//            int result = JOptionPane.showConfirmDialog(this,
//                    "Error while reading the settings file:\r\n" +
//                    settingsFile.getName() + "\r\n" +
//                    "Errormessage:\r\n" + ex.getMessage() + "\r\n" +
//                    "Do you want to continue?",
//                    "Error loading settings",
//                    JOptionPane.YES_NO_OPTION,
//                    JOptionPane.ERROR_MESSAGE);
//            if (result == JOptionPane.NO_OPTION)
//                throw new Exception("While reading the settings-file ann error occured.", ex);
//        }
        
//        styleSettings = mainSettings.getChildSettings("style");
//        generalSettings = mainSettings.getChildSettings("general");
//        pathSettings = mainSettings.getChildSettings("paths");
//        
//        showVariableNames = styleSettings.getValueBoolean("ShowVariableNames");
    }
    
    private void saveSettings() {
//        styleSettings.setAttributeValue("DividerLocation", "splitSListPreview", splitSListPreview.getDividerLocation());
//        styleSettings.setAttributeValue("DividerLocation", "splitToolShader", splitToolShader.getDividerLocation());
//        styleSettings.setAttributeValue("DividerLocation", "splitShaderLog", splitShaderLog.getDividerLocation());
//        styleSettings.setAttributeValue("WindowBounds", "width", this.getWidth());
//        styleSettings.setAttributeValue("WindowBounds", "height", this.getHeight());
//        styleSettings.setAttributeValue("WindowBounds", "left", this.getLocation().x);
//        styleSettings.setAttributeValue("WindowBounds", "top", this.getLocation().y);
//        styleSettings.setValue("WindowState", this.getExtendedState());
//        if (exportFileChooser != null) {
//            File f = exportFileChooser.getSelectedFile();
//            if (f != null) {
//                if (!f.isDirectory()) f = f.getParentFile();
//                pathSettings.setValue("LastExport", f.getAbsolutePath());
//            }
//        }
//        if (projectFileChooser != null) {
//            File f = projectFileChooser.getSelectedFile();
//            if (f != null) {
//                if (!f.isDirectory()) f = f.getParentFile();
//                pathSettings.setValue("LastProject", f.getAbsolutePath());
//            }
//        }
//        styleSettings.setValue("ShowVariableNames", showVariableNames);
//        
//        // -------------------------------------------------------------------
//        // Save to Disc
//        // ------------
//        Exception ex = null;
//        try {
//            styleSettings.saveToFile();
//        } catch (IOException e) {
//            ex = e;
//        } catch (NullPointerException e) {
//            ex = e;
//        }
//        if (ex != null) {
//            String filename = "";
//            if (styleSettings.getSaveFile() != null)
//                filename = styleSettings.getSaveFile().getName();
//            JOptionPane.showConfirmDialog(this, "Error while saving to the settings file:\r\n" +
//                    filename + "\r\n" +
//                    "Errormessage:\r\n" + ex.getMessage(),
//                    "Error loading settings",
//                    JOptionPane.OK_OPTION,
//                    JOptionPane.ERROR_MESSAGE);
//        }
    }
    
    private void createMainNodes() {
        vertexOutput = (new VertexOutputFactory()).createShaderProgram();
        vertexInput = (new VertexInputFactory()).createShaderProgram();
        fragmentOutputNode = (new FragmentOutputFactory()).createShaderProgram();
        fragmentInputNode = (new FragmentInputFactory()).createShaderProgram();
        nodeGraph.addMainOutputNode(vertexOutput.getShaderNode());
        nodeGraph.addMainInputNode(vertexInput.getShaderNode());
        nodeGraph.addMainOutputNode(fragmentOutputNode.getShaderNode());
        nodeGraph.addMainInputNode(fragmentInputNode.getShaderNode());
    }
    
    public void showNodeInOutputVarsPanel() {
        mainTabPanel.setSelectedComponent(nodeInOutputVars);
    }
    
    public void showNodePropertiesPanel() {
        mainTabPanel.setSelectedComponent(nodeProperties);
    }
    
    public void showGlobalVarsPanel() {
        mainTabPanel.setSelectedComponent(globalVarsPanel);
    }
    
    private NodeGraphPanel nodeGraph;
    private JSplitPane splitToolShader;
    private JSplitPane splitSListPreview;
    private JSplitPane splitShaderLog;
    private JTabbedPane mainTabPanel;
    private CollectionLoaderPanel shaderList;
    private NodeInOutputVarsEditor nodeInOutputVars;
    private NodePropertiesEditor nodeProperties;
    private GlobalVariablesEditor globalVarsPanel;
    private PreviewPanel previewPanel;
    private LogOutputPanel logOutputPanel;
    
    private void initComponents() {
        this.setTitle("RapidShading");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowListener() {
            public void windowActivated(WindowEvent e) {
            }
            public void windowClosed(WindowEvent e) {
            }
            public void windowClosing(WindowEvent e) {
                saveSettings();
                e.getWindow().dispose();
            }
            public void windowDeactivated(WindowEvent e) {
            }
            public void windowDeiconified(WindowEvent e) {
            }
            public void windowIconified(WindowEvent e) {
            }
            public void windowOpened(WindowEvent e) {
            }
        });
        this.setExtendedState(NORMAL);//styleSettings.getValueInt("WindowState", NORMAL));
        this.setBounds(0,0,800,500);
//                styleSettings.getAttributeValueInt("WindowBounds", "left", 0)
//                , styleSettings.getAttributeValueInt("WindowBounds", "top", 0)
//                , styleSettings.getAttributeValueInt("WindowBounds", "width", 800)
//                , styleSettings.getAttributeValueInt("WindowBounds", "height", 500));
        this.getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        initMenu();
        
        // FragmentGraph
        nodeGraph = new NodeGraphPanel();
        
        // SplitPane - ShaderList | Preview
        splitSListPreview = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitSListPreview.setDividerLocation(250);
//                styleSettings.getAttributeValueInt("DividerLocation", "splitSListPreview", 250));
        
        
        // ShaderListPanel
        shaderList = new CollectionLoaderPanel();
        JScrollPane shaderListScrollPane = new JScrollPane();
        splitSListPreview.setTopComponent(shaderListScrollPane);
        shaderListScrollPane.setViewportView(shaderList);
        
        // Preview
        previewPanel = new PreviewPanel();
        splitSListPreview.setBottomComponent(previewPanel);
        
        // transferVariables
        nodeInOutputVars = new NodeInOutputVarsEditor(nodeGraph);
        
        // transferVariables
        nodeProperties = new NodePropertiesEditor(nodeGraph);
        
        // transferVariables
        globalVarsPanel = new GlobalVariablesEditor(globalVariables);
        
        // Tabbed Pane
        mainTabPanel = new JTabbedPane();
        mainTabPanel.add("Graph", nodeGraph);
        mainTabPanel.add("Node In & Output Vars", nodeInOutputVars);
        mainTabPanel.add("Node Properties", nodeProperties);
        mainTabPanel.add("Global Vars", globalVarsPanel);
        mainTabPanel.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                Component comp = mainTabPanel.getSelectedComponent();
                if (comp instanceof NodeInOutputVarsEditor) {
                    ((NodeInOutputVarsEditor) comp).refresh();
                } else if(comp instanceof NodePropertiesEditor) {
                    (nodeProperties).refresh();
                }
            }
        });
        
        // Log Output Panel
        logOutputPanel = new LogOutputPanel();
//        constantRefresher.attachConsumer(logOutputPanel);
        System.setOut(logOutputPanel.getPrintStream());
        
        // SplitPane - Shader | Log
        splitShaderLog = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitShaderLog.setDividerLocation(400);
//                styleSettings.getAttributeValueInt("DividerLocation", "splitShaderLog", 400));
        splitShaderLog.setTopComponent(mainTabPanel);
        splitShaderLog.setBottomComponent(logOutputPanel);
        
        // SplitPane - Tool | Shader,Log
        splitToolShader = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitToolShader.setResizeWeight(0.5);
        splitToolShader.setLeftComponent(splitSListPreview);
        splitToolShader.setDividerLocation(150);
//                styleSettings.getAttributeValueInt("DividerLocation", "splitToolShader", 150));
        splitToolShader.setRightComponent(splitShaderLog);
        splitToolShader.setDividerSize(3);
        this.getContentPane().add(splitToolShader);
        
        // Load settings
        shaderList.loadSettings();
    }
    
    private javax.swing.JMenuBar jMainMenuBar;
    private javax.swing.JMenu mnuFile;
    private javax.swing.JMenu mnuView;
    private javax.swing.JMenu mnuPreview;
    private javax.swing.JMenu mnuHelp;
    private javax.swing.JMenuItem mnuiNew;
    private javax.swing.JMenuItem mnuiOpen;
    private javax.swing.JMenuItem mnuiSave;
    private javax.swing.JMenuItem mnuiSaveAs;
    private javax.swing.JMenuItem mnuiExit;
    private javax.swing.JMenuItem mnuiShowVarName;
    private javax.swing.JMenuItem mnuiRefresh;
    private javax.swing.JMenuItem mnuiFullscreen;
    private javax.swing.JMenuItem mnuiAbout;
    
    private void initMenu() {
        jMainMenuBar = new JMenuBar();
        setJMenuBar(jMainMenuBar);
        
        mnuFile = new JMenu();
        mnuFile.setText("File");
        mnuFile.setMnemonic('F');
        jMainMenuBar.add(mnuFile);
        
        mnuiOpen = new JMenuItem();
        mnuiOpen.setText("Open Project");
        mnuiOpen.setMnemonic('O');
        mnuiOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        mnuiOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openProject();
            }
        });
        mnuFile.add(mnuiOpen);
        
        mnuiNew = new JMenuItem();
        mnuiNew.setText("New Project");
        mnuiNew.setMnemonic('N');
        mnuiNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        mnuiNew.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nodeGraph.clear();
            }
        });
        mnuFile.add(mnuiNew);
        
        mnuiSave = new JMenuItem();
        mnuiSave.setText("Save Project");
        mnuiSave.setMnemonic('S');
        mnuiSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        mnuiSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveProject();
            }
        });
        mnuFile.add(mnuiSave);
        
        mnuiSaveAs = new JMenuItem();
        mnuiSaveAs.setText("Save Project As");
        mnuiSaveAs.setMnemonic('A');
        mnuiSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
        mnuiSaveAs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveProjectAs();
            }
        });
        mnuFile.add(mnuiSaveAs);
        
        mnuFile.addSeparator();
        JMenuItem mnuiExport = new JMenuItem();
        mnuiExport.setText("Export Shader Code");
        mnuiExport.setMnemonic('E');
        mnuiExport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
        mnuiExport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exportShaderCode();
            }
        });
        mnuFile.add(mnuiExport);
        
        mnuFile.addSeparator();
        mnuiExit = new JMenuItem();
        mnuiExit.setText("Exit");
        mnuiExit.setMnemonic('x');
        mnuFile.add(mnuiExit);
        
        // View //
        
        mnuView = new javax.swing.JMenu();
        mnuView.setText("View");
        mnuView.setMnemonic('V');
        jMainMenuBar.add(mnuView); 
        
        JMenuItem mnuiShowGraph = new JMenuItem();
        mnuiShowGraph.setText("Show Graph");
        mnuiShowGraph.setMnemonic('G');
        mnuiShowGraph.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, KeyEvent.CTRL_DOWN_MASK));
        mnuiShowGraph.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainTabPanel.setSelectedComponent(nodeGraph);
            }
        });
        mnuView.add(mnuiShowGraph);
        
        JMenuItem mnuiShowInOutputVars = new JMenuItem();
        mnuiShowInOutputVars.setText("Show Node In- & Output Vars");
        mnuiShowInOutputVars.setMnemonic('V');
        mnuiShowInOutputVars.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, KeyEvent.CTRL_DOWN_MASK));
        mnuiShowInOutputVars.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainTabPanel.setSelectedComponent(nodeInOutputVars);
            }
        });
        mnuView.add(mnuiShowInOutputVars);
        
        JMenuItem mnuiShowProperties = new JMenuItem();
        mnuiShowProperties.setText("Show Properties");
        mnuiShowProperties.setMnemonic('P');
        mnuiShowProperties.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, KeyEvent.CTRL_DOWN_MASK));
        mnuiShowProperties.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainTabPanel.setSelectedComponent(nodeProperties);
            }
        });
        mnuView.add(mnuiShowProperties);
        
        JMenuItem mnuiShowGlobalVars = new JMenuItem();
        mnuiShowGlobalVars.setText("Show Global Vars");
        mnuiShowGlobalVars.setMnemonic('G');
        mnuiShowGlobalVars.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, KeyEvent.CTRL_DOWN_MASK));
        mnuiShowGlobalVars.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainTabPanel.setSelectedComponent(globalVarsPanel);
            }
        });
        mnuView.add(mnuiShowGlobalVars);
        
        mnuView.addSeparator();
        
        mnuiShowVarName = new javax.swing.JMenuItem();
        mnuiShowVarName.setText("Toggle Show Variable Type & Names");
        mnuiShowVarName.setMnemonic('T');
        mnuiShowVarName.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
        mnuiShowVarName.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showVariableNames = showVariableNames ? false : true;
                nodeGraph.repaint();
            }
        });
        mnuView.add(mnuiShowVarName);  
        
        // Preview //
        
        mnuPreview = new javax.swing.JMenu();
        mnuPreview.setText("Preview");
        mnuPreview.setMnemonic('P');
        jMainMenuBar.add(mnuPreview);
        
        mnuiRefresh = new javax.swing.JMenuItem();
        mnuiRefresh.setText("Refresh");
        mnuiRefresh.setMnemonic('R');
        mnuiRefresh.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        mnuiRefresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                previewPanel.refresh();
            }
        });
        mnuPreview.add(mnuiRefresh);
        
        mnuiFullscreen = new javax.swing.JMenuItem();
        mnuiFullscreen.setText("Toggle Fullscreen");
        mnuiFullscreen.setMnemonic('F');
        mnuiFullscreen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0));
        mnuiFullscreen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                previewPanel.setFullscreen(!previewPanel.isFullscreen());
            }
        });
        mnuPreview.add(mnuiFullscreen);
        
        // Help //
        
        mnuHelp = new javax.swing.JMenu();
        mnuHelp.setText("Help");
        mnuHelp.setMnemonic('H');
        jMainMenuBar.add(mnuHelp); 
        
        mnuiAbout = new JMenuItem();
        mnuiAbout.setText("About");
        mnuiAbout.setMnemonic('A');
        mnuiAbout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getAbout().showDialog();
            }
        });
        mnuHelp.add(mnuiAbout);
    }
    
    private JFileChooser exportFileChooser;
    private void exportShaderCode() {
        if (exportFileChooser == null) {
            exportFileChooser = new JFileChooser();//pathSettings.getValueString("LastExport"));
            exportFileChooser.setFileFilter(new ExtensionFileFilter("glsl", "OpenGL Shader File"));
        }
        if (exportFileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = exportFileChooser.getSelectedFile();
            String filename = getExportFileName(f);
            try {
                FileWriter vertexfile = new FileWriter(filename + ".vs.glsl");
                FileWriter fragmentfile = new FileWriter(filename + ".fs.glsl");
                getActiveExporter().exportShader(vertexfile, fragmentfile);
                vertexfile.flush();
                vertexfile.close();
                fragmentfile.flush();
                fragmentfile.close();
            } catch (ExportingExeption ex) {
                JOptionPane.showMessageDialog(this, "An error occured while exporting:\n" + ex.getMessage());
                ex.printStackTrace();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "An error occured while writing file:\n" + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
    
    private String getExportFileName(File f) {
        String name = f.getAbsolutePath();
        String compExtension = ".glsl";
        String extension = name.substring(name.length() - compExtension.length());
        if (extension.equals(compExtension))
            name = name.substring(0, name.length() - compExtension.length());
        
        compExtension = ".vs";
        extension = name.substring(name.length() - compExtension.length());
        if (extension.equals(compExtension) || extension.equals(".fs"))
            name = name.substring(0, name.length() - compExtension.length());
        
        return name;
    }
    
    private File currentSaveFile;
    private JFileChooser projectFileChooser;
    
    public JFileChooser getProjectFileChooser() {
        if (projectFileChooser == null) {
            projectFileChooser = new JFileChooser();//pathSettings.getValueString("LastProject"));
            projectFileChooser.setFileFilter(new ExtensionFileFilter("rsp", "Rapid Shading Project File"));
        }
        return projectFileChooser;
    }
    
    private void saveProject() {
        if (currentSaveFile == null) {
            if (getProjectFileChooser().showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                this.currentSaveFile = getProjectFileChooser().getSelectedFile();
            } else {
                return;
            }
        }
        saveProject(currentSaveFile);
    }
    
    private void saveProjectAs() {
        if (getProjectFileChooser().showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            saveProject(getProjectFileChooser().getSelectedFile());
        }
    }
    
    protected static char[] fileIdentification = "RSP".toCharArray();
    protected static byte fileVersion = 0;
    
    /**
     * Saves project to file.
     * At the moment a quite naive and fragile implementation
     */
    private void saveProject(File saveFile) {
//        String name = saveFile.getAbsolutePath();
//        String compExtension = ".rsp";
//        String extension = name.substring(name.length() - compExtension.length());
//        if (!extension.equals(compExtension))
//            saveFile = new File(name + ".rsp");
//        
//        try {
//            getProjectPersistor().saveProject(this);
//            
//            if (saveFile.exists()) saveFile.delete();
//            saveFile.createNewFile();
//            getProjectPersistor().outputToFile(saveFile);
//            System.out.println("Project saved to file \"" + saveFile.getAbsolutePath() + "\" on " + (new Date()).toString());
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(this, "Error while writing to file " + saveFile.getName() +":\n" + ex.getMessage(),
//                    "Saving Project", JOptionPane.ERROR_MESSAGE);
//            ex.printStackTrace();
//        }
    }
    
    public void saveState(ProjectPersistor saveVisitor) throws PersistanceException {
//        saveVisitor.save("graph", this.nodeGraph);
//        saveVisitor.save("globals", this.globalVariables);
    }
    
    private void loadProject(File loadFile) {
//        try {
//            getProjectPersistor().setInputFile(loadFile);
//            getProjectPersistor().loadProject(this);
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(this, "Error while reading from file " + loadFile.getName() +":\n" + ex.getMessage(),
//                    "Loading Project", JOptionPane.ERROR_MESSAGE);
//            ex.printStackTrace();
//        }
    }
    public void loadState(ProjectPersistor loadVisitor) throws PersistanceException {
//        loadVisitor.loadNodeGraphPanel("graph", this.nodeGraph);
//        this.globalVariables = loadVisitor.loadVariablesCollection("globals");
    }
    
//        try {
//
//            ObjectOutputStream s = new ObjectOutputStream(new FileOutputStream(saveFile));
//
//            // Write File Identification
//            s.writeObject(fileIdentification);
//            // Write Version
//            s.writeByte(fileVersion);
//            // Write Node Graph
//            s.writeObject(this.nodeGraph);
//            // Write GlobalVars
//            s.writeObject(this.globalVariables);
//            s.flush();
//            s.close();
//        } catch (IOException ex) {
//            JOptionPane.showMessageDialog(this, "Error while writing to file " + saveFile.getName() +":\n" + ex.getMessage(),
//                    "Saving Project", JOptionPane.ERROR_MESSAGE);
//        }
//    }
    
//    private void loadProject(File loadFile) {
//        try {
//            ObjectInputStream s = new ObjectInputStream(new FileInputStream(loadFile));
//            String fileIdentification = (String) s.readObject();
//            byte fileVersion = s.readByte();
//            if (!fileIdentification.equals(this.fileIdentification))
//                throw new Exception("This file is not a Rapid Shading Project File.");
//            if (fileVersion != this.fileVersion)
//                throw new Exception("The version of the given file is not supported.");
//
//            this.nodeGraph = (NodeGraphPanel) s.readObject();
//            this.globalVariables = (VariablesCollection<GlobalVariable>) s.readObject();
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(this, "Error while reading from file " + loadFile.getName() +":\n" + ex.getMessage(),
//                    "Loading Project", JOptionPane.ERROR_MESSAGE);
//        }
//    }
    
    public ProjectPersistor getProjectPersistor() {
        if (projectPersistor == null) projectPersistor = new ProjectPersistor();
        return projectPersistor;
    }
    
    
    private void openProject() {
        if (getProjectFileChooser().showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            loadProject(getProjectFileChooser().getSelectedFile());
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws Exception {
        String systemLFClassName = UIManager.getSystemLookAndFeelClassName();
        UIManager.setLookAndFeel(systemLFClassName);
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run(){
                try {
                    new Editor().setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
    
    /**
     * Find this Class in a container tree. Search is just in parent side.
     */
    public static Editor findMe(Component container) {
        
        while(!(container instanceof Editor)) {
            container = container.getParent();
            if (container == null) break;
        }
        if (container == null)
            return null;
        else
            return (Editor) container;
    }
    
    /**
     * Gets a newly created Node and adds it to the graph.
     */
    public void createDragableNode(IShaderProgram shaderProgram) {
        this.mainTabPanel.setSelectedComponent(nodeGraph);
        this.nodeGraph.addNode(shaderProgram.getShaderNode(), true);
    }
    
    public IShaderCodeExporter getActiveExporter() {
        if (activeExporter == null) {
            activeExporter = new GLSLCodeExporter();
        }
        return activeExporter;
    }
    
    public void setActiveExporter(IShaderCodeExporter activeExporter) {
        this.activeExporter = activeExporter;
    }
    
    public boolean showVariableNames() {
        return showVariableNames;
    }
    
    private HashMap<String, URLClassLoader> plugInClassLoaders = new HashMap<String, URLClassLoader>();
    public Class getPluginClass(String jarFilename, String Classname) throws ClassNotFoundException {
        URLClassLoader plugInClassLoader = plugInClassLoaders.get(jarFilename);
        if (plugInClassLoader == null)
            throw new ClassNotFoundException("PlugIn " + jarFilename + " is not available.");
        return plugInClassLoader.loadClass(Classname);
    }
    
    private void loadPlugIns() {
        File plugInDir = new File("PlugIns");
        if (!plugInDir.exists()) return;
        
        File[] plugIns = plugInDir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                String fileName = file.getName().toLowerCase();
                return (file.isFile() && fileName.endsWith(".jar"));
            }
        });
        JarFile jarFile;
        Class plugInClass;
        for (File file : plugIns) {
            try {
                plugInClassLoaders.put(file.getName(), new URLClassLoader(new URL[] {file.toURI().toURL()}));
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public About getAbout() {
        if (about == null)
            about = new About(this);
        return about;
    }
}
