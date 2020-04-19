package org.codegladiator;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * A potential solution of a
 * <a href="https://www.techgig.com/practice/question/Y0Y2NndVR2FYTUFxZElLRkhRYzdPQT09">Practice Problem</a>
 */
public class EarthquakeInAfrica
{
  private final Map<Integer, City> graph;
  private final Scanner            scanner;

  /**
   * Sets the scanner to read the data for the problem.
   */
  public EarthquakeInAfrica(final Scanner scanner)
  {
    // Ensure that the scanner has been specified to read the
    // data required for the problem.
    if (scanner == null)
    {
      throw new NullPointerException("Argument [scanner] cannot be null.");
    }
    graph = new HashMap<>();

    this.scanner = scanner;
  }

  /**
   * <p>
   * Initializes solution processing.
   * </p>
   *
   * <p>
   * The problem asked the best unaffected city from earthquake to establish
   * the relief camp in such a way, so that all affected cities are at minimum
   * distance from that city.
   * </p>
   *
   * <p>
   * In order to solve the problem, Each unaffected city is considered as
   * source and shortest distance was found to all remaining cities using
   * {@code Dijixtra's} algorithm, then the city with shortest distance to all
   * affected city was considered as final city to establish the base camp.
   * </p>
   */
  public int solve(final ExecutionTypeEnum executionType)
  {
    // Setup the information provided for the problem
    setup();

    switch (executionType)
    {
      case PARALLEL:
      {
        final long startTimeForParallelExecution = System.currentTimeMillis();
        final int result = processUsingMultiThreading();
        System.out.println("Parallel Execution took:" + (System.currentTimeMillis() - startTimeForParallelExecution) + " milliseconds");
        return result;
      }
      case SEQUENTIAL:
      {
        final long startTimeForLinearExecution = System.currentTimeMillis();
        final int result = processSequentially();
        System.out.println("Sequential Execution took:" + (System.currentTimeMillis() - startTimeForLinearExecution) + " milliseconds");
        return result;
      }
      default:
        // Do nothing
        break;
    }

    return 0;
  }

  /**
   * Processes the problem using multiple threads.
   */
  private int processUsingMultiThreading()
  {
    if (!graph.isEmpty())
    {
      final SortedSet<Integer> shortestPaths = new TreeSet<>();
      // A pool of threads is created.
      final ExecutorService executorService = Executors.newCachedThreadPool();

      for (Map.Entry<Integer, City> node : graph.entrySet())
      {
        if (!node.getValue().isAffected())
        {
          final Runnable runnable = () -> {
            final Map<City, Integer> buffer = findShortPathToAllCities(node.getValue());
            int farthestCityDistance = 0;
            for (Map.Entry<City, Integer> entry : buffer.entrySet())
            {
              if (entry.getKey().isAffected()
                  && farthestCityDistance < entry.getValue())
              {
                farthestCityDistance = entry.getValue();
              }
            }

            synchronized (shortestPaths)
            {
              shortestPaths.add(farthestCityDistance);
            }
          };

          executorService.execute(runnable);
        }
      }

      executorService.shutdown();
      try
      {
        executorService.awaitTermination(1, TimeUnit.MINUTES);
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }

      return shortestPaths.first() * 2;
    }

    return 0;
  }

  /**
   * Processes iterations sequentially.
   */
  private int processSequentially()
  {
    if (!graph.isEmpty())
    {
      int shortestPath = Integer.MAX_VALUE;
      for (Map.Entry<Integer, City> node : graph.entrySet())
      {
        if (!node.getValue().isAffected())
        {
          final Map<City, Integer> buffer = findShortPathToAllCities(node.getValue());

          int farthestCityDistance = 0;
          for (Map.Entry<City, Integer> entry : buffer.entrySet())
          {
            if (entry.getKey().isAffected()
                && farthestCityDistance < entry.getValue())
            {
              farthestCityDistance = entry.getValue();
            }
          }

          if (shortestPath > farthestCityDistance)
          {
            shortestPath = farthestCityDistance;
          }
        }
      }

      return shortestPath * 2;
    }

    return 0;
  }

  /**
   * <p>
   * Finds the shortest distance to all cities from the specified source node.
   * </p>
   * <p>
   * A well known algorithm is used to find the shortest path to all nodes
   * has been used, this algorithm is famous with the name {@code Digixtra's}
   * algorithm.
   * </p>
   *
   * @param source The source {@link City} from which we need to find the
   *               shortest path to all cities.
   * @return A {@link Map} containing cities and their shortest path from the
   * source {@link City}.
   */
  private Map<City, Integer> findShortPathToAllCities(final City source)
  {
    final Map<City, Integer> buffer = new HashMap<>();

    if (source != null)
    {
      final Stack<City> remainingCities = new Stack<>();

      source.getRoads()
            .forEach(road -> {
              final City city = road.getTarget(source);

              remainingCities.push(city);

              buffer.put(city, road.getDistance());
            });

      while (!remainingCities.empty())
      {
        processCities(remainingCities, buffer);
      }
    }

    return buffer;
  }

  /**
   * Processes cities to find the shortest path to their adjacent cities.
   *
   * @param cities A {@link Stack} of remaining cities to process.
   * @param buffer A buffer of processed cities.
   */
  private void processCities(final Stack<City> cities, final Map<City, Integer> buffer)
  {
    if (!cities.empty())
    {
      final City currentCity = cities.pop();

      currentCity.getRoads()
                 .forEach(road -> {

                   final City targetCity = road.getTarget(currentCity);
                   final int totalDistance = buffer.get(currentCity) + road.getDistance();

                   if (!buffer.containsKey(targetCity))
                   {
                     cities.push(targetCity);
                     buffer.put(targetCity, totalDistance);
                   }
                   else if (buffer.get(targetCity) > totalDistance)
                   {
                     cities.push(targetCity);
                     buffer.put(targetCity, totalDistance);
                   }
                 });

    }
  }

  /**
   * <p>
   * Set up the data for finding the best non-affected city to establish a
   * relief camp.
   * </p>
   *
   * <p>
   * The following method also creates an undirected graph where, cities and
   * roads. Each city has label or name to identify it uniquely whereas, on
   * the other hand the roads are bi-directional links between cities with
   * a distance between them.
   * </p>
   */
  private void setup()
  {
    final int cities = scanner.nextInt();
    for (int city = 1; city <= cities; city++)
    {
      graph.put(city, new City(city));
    }
    final int roads = scanner.nextInt();
    final int affectedCities = scanner.nextInt();

    // Read and mark the affected cities.
    for (int i = 0; i < affectedCities; i++)
    {
      final int affectedCity = scanner.nextInt();

      graph.get(affectedCity).setAffected(true);
    }

    // Create roads between cities.
    for (int i = 0; i < roads; i++)
    {
      final int source = scanner.nextInt();
      final int destination = scanner.nextInt();
      final int distance = scanner.nextInt();

      new Road(graph.get(source), graph.get(destination), distance);
    }
  }

  /**
   * Represents a city.
   */
  private final class City
  {
    private       boolean   affected;
    private final int       label;
    private       Set<Road> roads;

    /**
     * Sets the label for the city.
     *
     * @param label The label for the city.
     */
    private City(final int label)
    {
      if (label <= 0)
      {
        throw new IllegalArgumentException("Argument label must be greater than zero.");
      }

      this.label = label;
    }

    /**
     * Adds a road to this city.
     *
     * @param road A road to this city.
     */
    private void addRoad(final Road road)
    {
      if (road != null)
      {
        if (roads == null)
        {
          roads = new HashSet<>();
        }

        roads.add(road);
      }
    }

    /**
     * Gets the city label.
     *
     * @return The city label.
     */
    public final int getLabel()
    {
      return label;
    }

    /**
     * Gets roads for this city.
     *
     * @return A {@link Set} of {@link Road}s.
     */
    public final Set<Road> getRoads()
    {
      return roads == null ? Collections.emptySet() : Collections.unmodifiableSet(roads);
    }

    /**
     * Gets whether this city is affected by earthquake.
     *
     * @return {@literal true} if the city is effected by earthquake,
     * {@literal false} otherwise.
     */
    public final boolean isAffected()
    {
      return affected;
    }

    /**
     * Sets whether this city is affected by earthquake.
     *
     * @param affected Whether this city is affected by earthquake.
     */
    public final void setAffected(final boolean affected)
    {
      this.affected = affected;
    }
  }

  /**
   * A physical geographical connection between two geographical entities, such
   * as {@link City}.
   */
  private final class Road
  {
    private final City destination;
    private final int  distance;
    private final City source;

    /**
     * Sets the source, destination and distance between two cities.
     *
     * @param source      The source city.
     * @param destination The destination city.
     * @param distance    The physical distance between source and destination.
     */
    private Road(final City source, final City destination, final int distance)
    {
      // Ensure that destination city has been specified.
      if (destination == null)
      {
        throw new NullPointerException("Argument destination must not be null");
      }
      // Ensure that source city has been specified.
      if (source == null)
      {
        throw new NullPointerException("Argument source must not be null");
      }
      // Ensure that source and destination cities are different.
      if (source.equals(destination))
      {
        throw new IllegalArgumentException("Argument source and destination can not be same.");
      }

      if (distance <= 0)
      {
        throw new IllegalArgumentException("Argument distance must be greater than zero.");
      }

      this.destination = destination;
      this.distance = distance;
      this.source = source;

      destination.addRoad(this);
      source.addRoad(this);
    }

    /**
     * Gets the destination city.
     *
     * @return The destination city.
     */
    public final City getDestination()
    {
      return destination;
    }

    /**
     * Distance between source and destination cities.
     *
     * @return Distance between source and destination cities.
     */
    public final int getDistance()
    {
      return distance;
    }

    /**
     * Gets the source city.
     *
     * @return The source city.
     */
    public final City getSource()
    {
      return source;
    }

    /**
     * Gets the target city from the specified city for this road.
     *
     * @param city The source city.
     * @return The target city.
     */
    public final City getTarget(final City city)
    {
      return isSource(city)
             ? getDestination()
             : getSource();
    }

    /**
     * Gets whether the specified city is marked as source city for this
     * road.
     *
     * @param city The city to check.
     * @return {@literal true} if the specified city is marked as source
     * city for this road, {@literal false} otherwise.
     */
    public boolean isSource(final City city)
    {
      return city != null && city.equals(source);
    }
  }

  /**
   * Execution method for this problem.
   */
  public enum ExecutionTypeEnum
  {
    PARALLEL, SEQUENTIAL;
  }
}
