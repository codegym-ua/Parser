package ua.codegym;

import org.junit.Assert;
import org.junit.Test;
import ua.codegym.impl.XmlFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static ua.codegym.Event.*;

public class ParserTest {


  EventHandler handler = new EventHandler() {
    @Override
    public void handle(String value) {
      System.out.println(value);
    }
  };
  EventHandler fail = new EventHandler() {
    @Override
    public void handle(String value) {
      Assert.fail();
    }
  };

  @Test
  public void checkElement() {

    AssertHandler startElementHandler = new AssertHandler("a");
    AssertHandler endElementHandler = new AssertHandler("a");

    Parser parser = XmlFactory.newBuilder()
        .on(START_ELEMENT, startElementHandler)
        .on(ATTRIBUTE_NAME, fail)
        .on(ATTRIBUTE_VALUE, fail)
        .on(VALUE, fail)
        .on(END_ELEMENT, endElementHandler)
        .on(ERROR, fail)
        .build();

    parser.parse(toInputStream("<a></a>"));

    startElementHandler.check();
  }

  @Test
  public void checkNestedElements() {

    AssertHandler startElementHandler = new AssertHandler("a", "b", "c", "d");

    Parser parser = XmlFactory.newBuilder()
        .on(START_ELEMENT, startElementHandler)
        .on(ERROR, fail)
        .build();

    parser.parse(toInputStream("<a><b></b><c><d></d></c></a>"));

    startElementHandler.check();
  }

  @Test
  public void checkAttributes() {
    AssertHandler attrHandler = new AssertHandler("x", "y");
    AssertHandler attrValueHandler = new AssertHandler("vx", "vy");

    Parser parser = XmlFactory.newBuilder()
        .on(ATTRIBUTE_NAME, attrHandler)
        .on(ATTRIBUTE_VALUE, attrValueHandler)
        .on(ERROR, fail)
        .build();

    parser.parse(toInputStream("<a x=\"vx\" y = \"vy\" ></ a>"));

    attrHandler.check();
    attrValueHandler.check();
  }

  @Test
  public void checkErrorHandling() {
    AssertHandler errorHandler = new AssertHandler("Xml is not valid. Error at 1:13");

    Parser parser = XmlFactory.newBuilder()
        .on(ERROR, errorHandler)
        .build();

    parser.parse(toInputStream("<a attr=\"abc\"error\"abc\" ></a>"));

    errorHandler.check();
  }

  private InputStream toInputStream(String s) {
    ByteArrayInputStream in = new ByteArrayInputStream(s.getBytes());
    return in;
  }
}
