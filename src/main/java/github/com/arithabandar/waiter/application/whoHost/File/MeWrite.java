package github.com.arithabandar.waiter.application.whoHost.File;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;

public class MeWrite {

    private static final String FILE_NAME = "i_Know_u.json";
    private static final ObjectMapper mapper = new ObjectMapper();

    public void writeFile(String ip, String pcName) {
        ArrayNode arrayNode = null;
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try {
                arrayNode = (ArrayNode) mapper.readTree(file);
            } catch (IOException e) {
                new Logs(MeWrite.class,e.getMessage());
            }
        } else {
            arrayNode = mapper.createArrayNode();
        }

        assert arrayNode != null;
        for (JsonNode node : arrayNode) {
            if (node.get("Device_IP").asText().equals(ip)) {
                System.out.println("There are same data man");// TODO: 18/7/24 remove
                return;
            }
        }

        ObjectNode newNode = mapper.createObjectNode();
        newNode.put("Device_IP", ip);
        newNode.put("Device_Name", pcName);
        arrayNode.add(newNode);

        try {
            mapper.writeValue(file, arrayNode);
        } catch (IOException e) {
            new Logs(MeWrite.class,e.getMessage());
        }

    }

}
