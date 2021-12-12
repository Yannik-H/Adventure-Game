package random;

import java.util.Random;

/**
 * This class is help to generate a random integer.
 */
public class RandomIntegerGenerator {

  private Random random;
  private int lowerBound;
  private int upperBound;

  /**
   * Constructor for random number with random seed.
   * @param lowerBound  The lower bound of random number.
   * @param upperBound  The upper bound of random number.
   * @param randomSeed  The random seed for java.util.Random.
   */
  public RandomIntegerGenerator(int lowerBound, int upperBound, int randomSeed)
          throws IllegalArgumentException {
    if (lowerBound > upperBound) {
      throw new IllegalArgumentException("The lower bound can not be larger than upper bound.");
    }
    random = new Random(randomSeed);
    this.lowerBound = lowerBound;
    this.upperBound = upperBound;
  }

  /**
   * Constructor for random number without random seed.
   * @param lowerBound  The lower bound of random number.
   * @param upperBound  The upper bound of random number.
   */
  public RandomIntegerGenerator(int lowerBound, int upperBound) throws IllegalArgumentException {
    if (lowerBound > upperBound) {
      throw new IllegalArgumentException("The lower bound can not be larger than upper bound.");
    }
    random = new Random();
    this.lowerBound = lowerBound;
    this.upperBound = upperBound;
  }

  public int getRandomInt() {
    return lowerBound + random.nextInt(upperBound + 1 - lowerBound);
  }

  public void setUpperBound(int upperBound) {
    this.upperBound = upperBound;
  }

}
