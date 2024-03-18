package yeonba.be.util;

import java.security.SecureRandom;
import java.util.Random;

public class TemporaryPasswordGenerator {

  private static final int MIN_LENGTH = 8;
  private static final int MAX_LENGTH = 20;

  private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
  private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final String DIGITS = "0123456789";
  private static final String SPECIAL = "~#@!";

  public static String generatePassword(){
    String pattern = ServiceRegex.PASSWORD.getPattern();
    SecureRandom random = new SecureRandom();
    StringBuilder sb = new StringBuilder();

    int length = MIN_LENGTH + random.nextInt(MAX_LENGTH - MIN_LENGTH+1);

    while(true){
      sb.setLength(0);
      for(int i=0; i<length; i++){
        int choice = random.nextInt(4);

        if(choice == 0){
          sb.append(getRandomCharacterFromSrc(LOWER, random));
          continue;
        }

        if(choice==1){
          sb.append(getRandomCharacterFromSrc(UPPER, random));
          continue;
        }

        if(choice==2){
          sb.append(getRandomCharacterFromSrc(DIGITS, random));
          continue;
        }

        sb.append(getRandomCharacterFromSrc(SPECIAL, random));
      }

      String result = sb.toString();
      if(result.matches(pattern)){

        return result;
      }
    }
  }

  private static char getRandomCharacterFromSrc(String src, Random random){

    return src.charAt(random.nextInt(src.length()));
  }
}
