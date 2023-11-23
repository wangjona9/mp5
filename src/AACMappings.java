import java.util.List;
import structures.AssociativeArray;
import structures.KeyNotFoundException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Implementation of the AACCategories
 * class for the AAC
 * 
 * @author Jonathan Wang
 *
 */
public class AACMappings { // maps filenames to categories
  // top level category is category of all categories

  static String filename;
  AssociativeArray<String, AACCategories> contents;
  AACCategories currentCategory; // set curr Category
  AACCategories home;
  AACCategories topCategory;
  AACCategories curr;

  public AACMappings(String filename) {
    AACMappings.filename = filename;
    this.contents = new AssociativeArray<>();
    this.home = new AACCategories("home");
    this.currentCategory = this.home;
    this.curr = this.home;

    String[] line = new String[2];
    try {
      File newObj = new File(filename);
      Scanner keyboard = new Scanner(newObj);
      while (keyboard.hasNextLine()) {
        String str = keyboard.nextLine();
        if (!(str.charAt(0) == '>')) {
          line = str.split(" ");
          this.contents.set(line[0], new AACCategories(line[1]));

          this.home.addItem(line[0], line[1]);
          try {
            this.currentCategory = this.contents.get(line[0]);
          } catch (KeyNotFoundException e) {
            System.err.println("Error: The key was not found");
          }

        } else {
          line = str.split(" ");
          line[0] = line[0].substring(1, line[0].length());
          this.currentCategory.addItem(line[0], line[1]);
        }
      }
      keyboard.close();
      this.currentCategory = this.home;
    } catch (FileNotFoundException e) {
      System.err.println("Error: The file was not found");
    }

  }

  void add(String imageLoc, String text) {
    this.currentCategory.addItem(imageLoc, text);
  }

  public String getCurrentCategory() {
    if (currentCategory == null) {
      return "";
    } else {
      return currentCategory.name;
    }
  }

public String[] getImageLocs() {
  return this.currentCategory.getImages();
}

  public String getText(String imageLoc) {
    try {
      if (isCategory(imageLoc)) {
        this.currentCategory = this.contents.get(imageLoc); // Update current category
        return this.currentCategory.name; // Return category name
      } else {
        return this.currentCategory.getText(imageLoc); // Return text associated with the image
      }
    } catch (KeyNotFoundException e) {
      e.printStackTrace();

    }
    return imageLoc;
  }

  boolean isCategory(String imageLoc) {
    return imageLoc.contains("clothing") || imageLoc.contains("food"); // is the key associated with value?
  }

  void reset() {
    this.contents.remove("");
  }

  void writeToFile(String filename) {
    try {
      PrintWriter pen = new PrintWriter(new FileWriter(filename));

      for (String imageLoc : getImageLocs()) {
        if (isCategory(imageLoc)) {
          pen.println(imageLoc + " " + getText(imageLoc));
        } else {
          pen.println("> " + imageLoc + " " + getText(imageLoc));
        }
      }

      pen.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}