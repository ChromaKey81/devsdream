package chromakey.devsdream.block;


import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.Gson;

import net.minecraft.block.AbstractBlock.Properties;

public class RegisterBlocks {

    public static Properties deserializeProperties(JsonObject object) {
        if(!object.has("properties")) {
            throw new JsonSyntaxException("Missing block properties, expected a compound");
        } else {
            Gson gson = new Gson();
            Properties properties = gson.fromJson(object.toString(), Properties.class);
            return properties;
        }
    }

}