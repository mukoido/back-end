package com.brainacad;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

import java.nio.file.Files;
import java.nio.file.Paths;


public class MyJsonValidator {

    public static ProcessingReport validateJson(String json, String jsonSchemaPach) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String jsonSchema = new String(Files.readAllBytes(Paths.get(jsonSchemaPach)));
        JsonNode schemaNode = mapper.readTree(jsonSchema);
        JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
        JsonNode currJson =mapper.readTree(json);
        JsonSchema schema = factory.getJsonSchema(schemaNode);
        return schema.validate(currJson);
    }

}
