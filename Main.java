/* Main class is used to reading the files and passing requests to the Mafia class
 * which is an avl tree subclass. Main also connects WriteHelper class to the Mafia
 * class. WriteHelper prevents unnecessary writing related stuff in Mafia class and makes cleaner.
 */

import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

/* Open read and write streams.Start reading the file. In each line get the request and pass it to the mafia class.
 * When all the requests are performed close the streams.
 */
public class Main {
  public static void main(String[] args) {
    try {
      WriteHelper helper = new WriteHelper(args[1]);
      Mafia mafiaTree = new Mafia(helper);
      File f = new File(args[0]);
      Scanner reader = new Scanner(f);
      if (reader.hasNextLine()) {
        String bossInfoRaw = reader.nextLine();
        String[] bossInfo = bossInfoRaw.split(" ");
        mafiaTree.insert(new Person(bossInfo[0], Double.parseDouble(bossInfo[1])));
      }
      while (reader.hasNextLine()) {
        String memberInfoRaw = reader.nextLine();
        String[] memberInfo = memberInfoRaw.split(" ");
        if (memberInfo[0].equals("MEMBER_IN")) {
          mafiaTree.insert(new Person(memberInfo[1], Double.parseDouble(memberInfo[2])));
        } else if (memberInfo[0].equals("MEMBER_OUT")) {
          mafiaTree.remove(new Person(memberInfo[1], Double.parseDouble(memberInfo[2])));
        } else if (memberInfo[0].equals("INTEL_TARGET")) {
          Person x = new Person(memberInfo[1], Double.parseDouble(memberInfo[2]));
          Person y = new Person(memberInfo[3], Double.parseDouble(memberInfo[4]));
          mafiaTree.intelTarget(x, y);
        } else if (memberInfo[0].equals("INTEL_RANK")) {
          mafiaTree.monitorRanks(new Person(memberInfo[1], Double.parseDouble(memberInfo[2])));
        } else if (memberInfo[0].equals("INTEL_DIVIDE")) {
          mafiaTree.intelDivide();
        }
      }
      reader.close();
      helper.close();
    } catch (FileNotFoundException e) {
      System.out.println("File has not found.");
    }

  }
}