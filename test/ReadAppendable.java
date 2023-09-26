import java.io.IOException;

/**
 * Get the content written to Appendable.
 */
public class ReadAppendable implements Appendable {
  private String content = "";

  @Override
  public Appendable append(CharSequence csq) throws IOException {
    this.content += csq.toString();
    return this;
  }

  @Override
  public Appendable append(CharSequence csq, int start, int end) throws IOException {
    this.content += csq.subSequence(start, end).toString();
    return this;
  }

  @Override
  public Appendable append(char c) throws IOException {
    this.content += String.valueOf(c);
    return this;
  }
  
  @Override
  public String toString() {
    return this.content;
  }
}
