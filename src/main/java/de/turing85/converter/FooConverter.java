package de.turing85.converter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.xml.transform.stream.StreamSource;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import de.turing85.dto.Foo;
import io.quarkus.arc.Arc;
import io.quarkus.arc.InstanceHandle;
import org.apache.camel.Converter;
import org.apache.camel.StreamCache;

@Converter
@SuppressWarnings("unused")
public class FooConverter {
  private FooConverter() {
    throw new UnsupportedOperationException("This class cannot be instantiated");
  }

  @Converter
  public static Foo unmarshal(StreamCache xmlAsStream) throws IOException, JAXBException {
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    xmlAsStream.writeTo(stream);
    return unmarshalFromString(stream.toString());
  }

  @Converter
  public static Foo unmarshalFromString(String xmlAsString) throws JAXBException {
    return getContext().createUnmarshaller()
        .unmarshal(new StreamSource(new StringReader(xmlAsString)), Foo.class).getValue();
  }

  private static JAXBContext getContext() {
    List<InstanceHandle<JAXBContext>> jaxbContexts = Arc.container().listAll(JAXBContext.class);
    if (jaxbContexts.isEmpty()) {
      throw new IllegalStateException("No JAXBContext found");
    }
    if (jaxbContexts.size() > 1) {
      throw new IllegalStateException("More than one JAXBContext found");
    }
    return jaxbContexts.get(0).get();
  }

  @Converter
  public static InputStream marshall(Foo foo) throws JAXBException {
    return new ByteArrayInputStream(marshalToString(foo).getBytes(StandardCharsets.UTF_8));
  }

  @Converter
  public static String marshalToString(Foo foo) throws JAXBException {
    Marshaller marshaller = getContext().createMarshaller();
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    marshaller.marshal(foo, stream);
    return stream.toString();
  }
}
