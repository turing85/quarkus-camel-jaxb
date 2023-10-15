package de.turing85.converter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import javax.xml.transform.stream.StreamSource;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

import de.turing85.dto.Foo;
import io.quarkus.arc.Arc;
import io.quarkus.arc.InstanceHandle;
import org.apache.camel.Converter;
import org.apache.camel.StreamCache;

@Converter
@SuppressWarnings("unused")
public class FooConverter {
  private static volatile JAXBContext context;

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
    // @formatter:off
    return getContext()
        .createUnmarshaller()
        .unmarshal(new StreamSource(new StringReader(xmlAsString)), Foo.class)
        .getValue();
    // @formatter:on
  }

  private static JAXBContext getContext() {
    if (Objects.isNull(context)) {
      synchronized (FooConverter.class) {
        if (Objects.isNull(context)) {
          List<InstanceHandle<JAXBContext>> jaxbContexts =
              Arc.container().listAll(JAXBContext.class);
          if (jaxbContexts.isEmpty()) {
            throw new IllegalStateException("No JAXBContext found");
          }
          if (jaxbContexts.size() > 1) {
            throw new IllegalStateException("More than one JAXBContext found");
          }
          context = jaxbContexts.get(0).get();
        }
      }
    }
    return context;
  }

  @Converter
  public static InputStream marshal(Foo foo) throws JAXBException {
    return new ByteArrayInputStream(marshalToString(foo).getBytes(StandardCharsets.UTF_8));
  }

  @Converter
  public static String marshalToString(Foo foo) throws JAXBException {
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    // @formatter:off
    getContext()
        .createMarshaller()
        .marshal(foo, stream);
    // @formatter:on
    return stream.toString();
  }
}
