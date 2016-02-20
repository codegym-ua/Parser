package ua.codegym;

import org.junit.Assert;

import static org.hamcrest.CoreMatchers.is;

class AssertHandler implements EventHandler {

  private String[] values;
  private int count = 0;

  public AssertHandler(String... values) {
    this.values = values;
  }

  @Override
  public void handle(String value) {
    if (count < values.length) {
      Assert.assertThat(value, is(values[count++]));
    } else {
      Assert.fail(String.format("Unexpected value. [%s]", value));
    }
  }

  public void check() {
    if (count != values.length) {
      Assert.fail(String.format("Unhandled values! [%s]", values[count]));
    }
  }
}