package org.rspeer.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FilenameUtils;
import org.rspeer.script.Script;
import org.rspeer.ui.Log;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class ProfileManager {

    private final static String AUTHOR =  "Puck";
    private final static String SCRIPT_NAME =  "PuckAIOBuyer";
    private final static String BASE_FOLDER = String.format("\\%s\\%s\\", AUTHOR,SCRIPT_NAME);

    private static String getSavePath(){
        Path directory = Script.getDataDirectory();
        return  directory.getFileName() + BASE_FOLDER;
    }

    public static void saveJson(Profile profile ){
        try (Writer writer = new FileWriter(Script.getDataDirectory() + BASE_FOLDER  + profile.profileName + ".json")) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(profile, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Profile loadProfile(Type type, String fileName){
        try (Reader reader = new FileReader(Script.getDataDirectory() + BASE_FOLDER + fileName)) {
            Gson gson = new GsonBuilder().create();
            return gson.fromJson(reader,type);
        }catch (IOException e) {
            Log.info("I/O Exception");
            return null;
        }
    }

    private static boolean isJson(File file, String name){
        FilenameFilter filter = (dir, name1) -> name1.toLowerCase().endsWith(".json");
        return filter.accept(file,name);
    }

    public static boolean nameExists(String profileName){
        String savePath = getSavePath();
        try {

            List<Path> paths = Files.walk(Paths.get(savePath))
                    .filter(f -> isJson(f.getFileName().toFile(),f.getFileName().toString()))
                    .collect(Collectors.toList());
            for(Path path : paths){
                String name = FilenameUtils.getBaseName(path.getFileName() + "");
                if(name.equalsIgnoreCase((profileName))){
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private static final Type PROFILE_TYPE = new TypeToken<Profile>() {
    }.getType();

    public static Profile[] loadProfiles(){
        String savePath = getSavePath();

        List<Profile> profiles = new ArrayList<>();
        try {
            List<Path> paths = Files.walk(Paths.get(savePath))
                    .filter(f -> isJson(f.getFileName().toFile(),f.getFileName().toString()))
                    .collect(Collectors.toList());


            paths.forEach(path -> {
                Profile profile = loadProfile(PROFILE_TYPE,path.getFileName() + "");
                if(profile != null)
                    profiles.add(profile);
            });
        }catch (IOException ex){

        }
        return profiles.toArray(new Profile[profiles.size()]);
    }

    public static void createBaseIfNotExists(){
        String savePath = getSavePath();
        Path path = Paths.get(savePath);
        if(!Files.exists(path)){
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
