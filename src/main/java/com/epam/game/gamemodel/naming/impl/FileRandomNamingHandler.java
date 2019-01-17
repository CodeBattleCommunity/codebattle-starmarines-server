package com.epam.game.gamemodel.naming.impl;

import com.epam.game.gamemodel.naming.NamingHandler;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileRandomNamingHandler implements NamingHandler {

    static private Resource file;
    private List<String> names;
    
    public FileRandomNamingHandler() {
        names = new ArrayList<String>();
    }

    @Override
    public String nextName() {
        if (names.size() == 0) {
            loadNames();
        }
        return (names.size() == 0) ? null : names.remove((int) (names.size() * Math.random()));
    }
    
    public void setFile(Resource name){
        file = name;
    }
    
    public Resource getFile(){
        return file;
    }

    private void loadNames(){
        if(file == null){
            return;
        }
        try{
            File f = file.getFile();
            FileReader reader = new FileReader(f);
            BufferedReader read = new BufferedReader(reader);
            String name = read.readLine();
            while(name != null){
                String val = getValue(name);
                if(val != null){
                    names.add(val);
                }
                name = read.readLine();
            }
        } catch (IOException e){
            //
            e.printStackTrace();
        }
    }
    
    private String getValue(String stringFromFile){
        Pattern p = Pattern.compile("^\\s*(\\w+)\\s*(#(.*)){0,1}$");
        Matcher matcher = p.matcher(stringFromFile);
        if(!matcher.matches()){
            return null;
        }
        return matcher.group(1);
    }
}
