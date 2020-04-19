package org.codegladiator;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

/**
 * Unit test cases for {@link EarthquakeInAfrica}.
 */
public class EarthquakeInAfricaTest
{
  /**
   * Tests that the first test case gives the correct result while executing in
   * parallel.
   */
  @Test
  public void testCase1WithParallelExecution()
  {
    final File file = new File(EarthquakeInAfricaTest.class.getClassLoader().getResource("test-data/test-case1.txt").getFile());

    try
    {
      final Scanner scanner = new Scanner(file);

      final EarthquakeInAfrica obj = new EarthquakeInAfrica(scanner);
      assertEquals(6, obj.solve(EarthquakeInAfrica.ExecutionTypeEnum.PARALLEL));
    }
    catch (final FileNotFoundException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Tests that the first test case gives the correct result while executing
   * sequentially.
   */
  @Test
  public void testCase1WithSequentialExecution()
  {
    final File file = new File(EarthquakeInAfricaTest.class.getClassLoader().getResource("test-data/test-case1.txt").getFile());

    try
    {
      final Scanner scanner = new Scanner(file);

      final EarthquakeInAfrica obj = new EarthquakeInAfrica(scanner);
      assertEquals(6, obj.solve(EarthquakeInAfrica.ExecutionTypeEnum.SEQUENTIAL));
    }
    catch (final FileNotFoundException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Tests that the second test case gives the correct result while executed in
   * parallel.
   */
  @Test
  public void testCase2WithParallelExecution()
  {
    final File file = new File(EarthquakeInAfricaTest.class.getClassLoader().getResource("test-data/test-case2.txt").getFile());

    try
    {
      final Scanner scanner = new Scanner(file);

      final EarthquakeInAfrica obj = new EarthquakeInAfrica(scanner);
      assertEquals(4, obj.solve(EarthquakeInAfrica.ExecutionTypeEnum.PARALLEL));
    }
    catch (final FileNotFoundException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Tests that the second test case gives the correct result while executed
   * sequentially.
   */
  @Test
  public void testCase2WithSequentialExecution()
  {
    final File file = new File(EarthquakeInAfricaTest.class.getClassLoader().getResource("test-data/test-case2.txt").getFile());

    try
    {
      final Scanner scanner = new Scanner(file);

      final EarthquakeInAfrica obj = new EarthquakeInAfrica(scanner);
      assertEquals(4, obj.solve(EarthquakeInAfrica.ExecutionTypeEnum.SEQUENTIAL));
    }
    catch (final FileNotFoundException e)
    {
      e.printStackTrace();
    }
  }
}
