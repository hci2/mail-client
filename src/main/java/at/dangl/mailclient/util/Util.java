package at.dangl.mailclient.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Util {

    private Util(){
    }

    public static Resource loadFileFromClassPath(String fileNameFromResources) {
        Resource resource = new ClassPathResource("/"+ fileNameFromResources);
        log.info("Loaded file from classpath: {}", resource);
        return resource;
    }

    public static String getStringFromHashMap(HashMap<String, Boolean> hashMap){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Summary: \n\n");
        for (Map.Entry<String, Boolean> entry : hashMap.entrySet()) {
            stringBuilder.append(entry.getKey()+entry.getValue()+"\n");
        }
        return stringBuilder.toString();
    }

}
