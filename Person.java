/* This class is used to create objects for each mafia member. Each object
 * stores name and gms values. They are compared based on gms values.
*/

public class Person implements Comparable<Person> {
  private String name;
  private double gms;

  public Person(String name, double gms) {
    this.name = name;
    this.gms = gms;
  }

  public String getName() {
    return name;
  }

  public double getGms() {
    return gms;
  }

  public int compareTo(Person o) {
    if (gms > o.getGms()) {
      return 1;
    } else if (o.getGms() == gms) {
      return 0;
    }
    return -1;
  }
}
