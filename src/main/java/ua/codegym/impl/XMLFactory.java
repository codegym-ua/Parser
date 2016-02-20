package ua.codegym.impl;

import ua.codegym.Event;
import ua.codegym.EventHandler;
import ua.codegym.Parser;

import java.util.HashMap;
import java.util.Map;

public class XmlFactory {

  Map<Event, EventHandler> handlers = new HashMap<>();

  public static XmlFactory newBuilder() {
    return new XmlFactory();
  }

  public XmlFactory on(Event event, EventHandler handler) {
    checkNotNull(event, "EVENT could not be NULL.");
    checkNotNull(handler, "HANDLER could not be NULL.");

    handlers.put(event, handler);
    return this;
  }

  public Parser build() {
    return new XmlParser(handlers);
  }

  public static void checkNotNull(Object o, String msg) {
    if (o == null) {
      throw new IllegalArgumentException(msg);
    }
  }
}
