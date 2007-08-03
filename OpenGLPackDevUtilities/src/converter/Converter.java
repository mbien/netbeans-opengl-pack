/*
 * NewClass.java
 * 
 * Created on 04.06.2007, 00:23:34
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package converter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import net.java.nboglpack.glsleditor.vocabulary.GLSLElementDescriptor;
import net.java.nboglpack.glsleditor.vocabulary.GLSLVocabulary;

/**
 *
 * @author mbien
 */
public class Converter {
    
    Converter() {
        try {
            JAXBContext oldContext = JAXBContext.newInstance("vocab");
            Unmarshaller um = oldContext.createUnmarshaller();
            JAXBElement<MapType> root = (JAXBElement<MapType>)um.unmarshal(new File("src/util/vocab_converter/GLSLVocabulary_old.xml"));
            
            final List<EntryType> list = root.getValue().getEntry();
            
            
            GLSLVocabulary vocab = new GLSLVocabulary();
            
            for (EntryType entryType : list) {
                DescType desc = entryType.getDesc();
                if(desc != null && desc.getCategory() != null) {
                    if(desc.getCategory().endsWith("-vert")) {
                        putElement(vocab.vertexShaderVocabulary, entryType.getString(), desc);
                    }else if(desc.getCategory().endsWith("-frag")) {
                        putElement(vocab.fragmentShaderVocabulary, entryType.getString(), desc);
                    }else if(desc.getCategory().endsWith("-geom")){
                        putElement(vocab.geometryShaderVocabulary, entryType.getString(), desc);
                    }else{
                        putElement(vocab.mainVocabulary, entryType.getString(), entryType.getDesc());
                    }                         
                }
            }

            
            marshall(vocab);
            
//            context.generateSchema(new NewClass.MySchemaOutputResolver());
//        } catch (IOException ex) {
//            ex.printStackTrace();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
    private void putElement(HashMap<String, GLSLElementDescriptor[]> map, String key, DescType desc) {
        
        GLSLElementDescriptor[] elements = null;
        
        if(desc.getArguments() != null && desc.getArguments().startsWith("(") && desc.getArguments().endsWith(")")) {
            String[] args = desc.getArguments().replace('(',' ').replace(')', ' ').split(",");
            String[] types= desc.getType().split("\\|");
            
            

            int max = 0;
            for (int i = 0; i < args.length; i++) {
                String[] s = args[i].split("\\|");
                max = Math.max(max, s.length);

            }
            
            
            
            elements = new GLSLElementDescriptor[max];
            for(int i = 0; i < max; i++) {
                
                GLSLElementDescriptor element = new GLSLElementDescriptor();                
                element.type = types[Math.min(i, types.length-1)];
                
                element.arguments = "";
                
                for(int n = 0; n < args.length; n++) {
                    String[] arg = args[n].split("\\|");
                    
                    element.arguments += arg[Math.min(i, arg.length-1)].trim()+", ";
                }
                
                element.arguments = "("+element.arguments.substring(0, element.arguments.length()-2)+")";
                elements[i] = element;
            }
            
        }else{
            elements = new GLSLElementDescriptor[] { new GLSLElementDescriptor() };
            elements[0].arguments = desc.getArguments();
            elements[0].type = desc.getType();
        }
        
        GLSLElementDescriptor.Category cat = convertCategory(desc.getCategory());
        for (GLSLElementDescriptor gLSLElementDescriptor : elements) {
            gLSLElementDescriptor.doc = desc.getDoc();
            gLSLElementDescriptor.tooltip = desc.getTooltip();
            gLSLElementDescriptor.category = cat;
        }
        
        map.put(key, elements);
        
    }
    
    private GLSLElementDescriptor.Category convertCategory(String category) {
        
        if(category == null)
            return GLSLElementDescriptor.Category.KEYWORD;
        
        if(category.contains("build-in-func")) {
            return GLSLElementDescriptor.Category.BUILD_IN_FUNC;
        }else if(category.contains("build-in-var")) {
            return GLSLElementDescriptor.Category.BUILD_IN_VAR;
        }else if(category.contains("qualifier")) {
            return GLSLElementDescriptor.Category.QUALIFIER;
        }else if(category.contains("type")) {
            return GLSLElementDescriptor.Category.TYPE;
        }else if(category.contains("jump")) {
            return GLSLElementDescriptor.Category.JUMP;
        }else if(category.contains("iteration")){
            return GLSLElementDescriptor.Category.ITERATION;
        }else if(category.contains("selection")){
            return GLSLElementDescriptor.Category.SELECTION;
        }else{
            return GLSLElementDescriptor.Category.KEYWORD;
        }
            
    }
    public void marshall(GLSLVocabulary vocab) throws JAXBException, IOException {
        JAXBContext context = JAXBContext.newInstance(GLSLVocabulary.class, GLSLElementDescriptor.class);
        
        Marshaller m = context.createMarshaller();
        JAXBElement root = new JAXBElement(new QName("vocabulary"), GLSLVocabulary.class, vocab);
        
        File file = new File("src/util/GLSLVocabulary2.xml");
        if(!file.exists()) file.createNewFile();

        OutputStream os = new FileOutputStream(file);
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.marshal(vocab, os);
        os.flush();
        os.close();
        
    }

 public static void main(String args[]) {

    new Converter();
    
 }

}
