package org.example;

import java.time.LocalDateTime;

import javax.ws.rs.ext.ContextResolver;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;

public class RegisterSerializersObjectMapper implements ContextResolver<ObjectMapper> {
	
	private ObjectMapper mapper;


	public RegisterSerializersObjectMapper() {
		System.out.println("Creating custom object mapper to include localdatetimeserializers");
		//try to register it here so it should not require annotations on our objects
		mapper = new ObjectMapper();
		SimpleModule serializersModule = new SimpleModule("[De]Serializers module", Version.unknownVersion());
		serializersModule.addSerializer(LocalDateTime.class, new  LocalDateTimeSerializer());
		serializersModule.addDeserializer(LocalDateTime.class, new  LocalDateTimeDeserializer());
		mapper.registerModule(serializersModule);
	}
	

	public ObjectMapper getContext(Class<?> type) {
		return mapper;
	}

}
