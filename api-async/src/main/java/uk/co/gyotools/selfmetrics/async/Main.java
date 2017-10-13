package uk.co.gyotools.selfmetrics.async;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ratpack.func.Action;
import ratpack.guice.Guice;
import ratpack.handling.RequestLogger;
import ratpack.server.RatpackServer;
import ratpack.server.ServerConfig;
import ratpack.server.ServerConfigBuilder;
import uk.co.gyotools.selfmetrics.async.config.MainModule;
import uk.co.gyotools.selfmetrics.async.handler.AssetHandler;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class Main {

    public static void main(String[] args) throws Exception {
        ServerConfigBuilder serverConfig = ServerConfig
                .builder()
                .development(false)
                .port(8080)
//                .props(Main.class.getResource("/config/default.properties"))
//                .onError(error -> log.warn("Unable to load override properties"))
//                .props(Paths.get("/", "opt", "dcm", "conf", "override.properties"))
                .onError(Action.throwException());
//                .sysProps("config.")
        RatpackServer.start(server -> server
                .serverConfig(config -> ServerConfig.embedded())
                .registry(Guice.registry(b ->
                        b.module(MainModule.class)
                                .add(createDefaultObjectMapper())
                ))
                .handlers(chain -> { chain
                        .all(RequestLogger.ncsa())
                        .prefix("assets", subChain -> subChain.all(AssetHandler.class));
                })
        );
    }

    private static ObjectMapper createDefaultObjectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss\'Z\'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        objectMapper.setDateFormat(df);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());
        JsonFactory factory = objectMapper.getFactory();
        factory.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);
        factory.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        return objectMapper;
    }
}
