package yeonba.be.util;

public enum ServiceRegex {

  EMAIL("[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"),
  PASSWORD("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~#@!]).{8,20}$");

  private final String pattern;

  ServiceRegex(String pattern) {
    this.pattern = pattern;
  }

  public String getPattern() {

    return pattern;
  }
}
