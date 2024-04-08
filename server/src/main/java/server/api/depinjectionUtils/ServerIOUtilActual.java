package server.api.depinjectionUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import commons.Conversion;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class ServerIOUtilActual implements ServerIOUtil {

    @Override
    public boolean createFileStructure() {
        boolean changeStatus = false;
        String folder = getDataFolder();
        File file = new File(folder);
        if (!file.isDirectory()) {
            file.mkdir();
            changeStatus = true;
            System.out.println("Created data folder");
        }
        folder = getLanguagesFolder();
        file = new File(folder);
        if (!file.isDirectory()) {
            file.mkdir();
            changeStatus = true;
            System.out.println("Created languages folder");
        }
        folder = getCurrencyCacheFile();
        file = new File(folder);
        try {
            if (file.createNewFile()) {
                changeStatus = true;
                // Set up basic json structure
                System.out.println("Created currency cache file");
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return changeStatus;
    }

    @Override
    public void writeConversionObjects(File file, List<Conversion> conversionList) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(file, conversionList);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Conversion> readConversionObjects(File file) {
        createFileStructure();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(file, new TypeReference<>() {});
        } catch (Exception e){
            return new ArrayList<>();
        }
    }

    @Override
    public String read(File file) {
        try {
            Path path = Paths.get(file.getPath());
            byte[] bytes = Files.readAllBytes(path);
            return new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void write(String string, File file) {
        createFileStructure();
        try {
            file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(string);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean fileExists(File file) {
        return file.exists();
    }

    @Override
    public void writeJson(JsonObject object, File file){
        try (FileWriter fileWriter = new FileWriter(file.getPath())) {
            Gson gson = new Gson();
            gson.toJson(object, fileWriter);
            System.out.println("JSON file updated successfully.");
        } catch (IOException e) {
            throw new RuntimeException("Error writing JSON object to file", e);
        }
    }
    @Override
    public boolean deleteFile(File file){
        if(fileExists(file)){
            file.delete();
        }
        return false;
    }

    @Override
    public boolean createNewFile(File newfile){
        try {
            return newfile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
