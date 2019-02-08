package service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import static java.lang.Integer.parseInt;


public class myTools{

    /**
     * Convert CSV file to list of strings. every item from the new line
     * get properties from main.properties file.
     * @return
     * @throws IOException
     */

    public static Properties getFileProperties() throws IOException {

        String versionString = null;

        //to load application's properties, we use this class
        Properties mainProperties = new Properties();

        FileInputStream file = null;


        //the base folder is ./, the root of the main.properties file
        String path = "./main.properties";

        //load the file handle for main.properties
        try {
            file = new FileInputStream(path);
            mainProperties.load(file);
            file.close();
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            System.out.println("file main.properties is not found in the program root folder.");
            System.out.println("the new properties file will be created....");
            try (PrintWriter out = new PrintWriter(path)) {
                out.println("// app.version=1.0.0.0");
                out.println("// app.name=Flash ESL by itemIPF page");
                out.println("username=config");
                out.println("userAPIkey=scoxS020_jNzb5tk2aScjGqK");
                out.println("ip=10.0.2.107");
                out.println("port=11097");
                out.println("flashtime=30");
                out.println("Page2Flash=DISCOUNT");
                file = new FileInputStream(path);
                mainProperties.load(file);
                file.close();
            }
        }

        return mainProperties;
    }

    public static String getAppVersion() {

        String versionString = null;

        //to load application's properties, we use this class
        Properties mainProperties = null;

        try {
            mainProperties = getFileProperties();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Нет свойства app.version");
        }

        //retrieve the property we are intrested, the app.version
        versionString = mainProperties.getProperty("app.version");

        return versionString;
    }
    public static String getUserName() throws FileNotFoundException {

        String username = null;

        //to load application's properties, we use this class
        Properties mainProperties = null;
        try {
            mainProperties = getFileProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //retrieve the property we are intrested, the app.version
        username = mainProperties.getProperty("username");

        return username;
    }
    public static int getFlashTime() throws FileNotFoundException {

        int flashtime = 0;

        //to load application's properties, we use this class
        Properties mainProperties = null;
        try {
            mainProperties = getFileProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //retrieve the property we are intrested, the app.version
        try {
            flashtime = parseInt(mainProperties.getProperty("flashtime"));
        } catch (NumberFormatException e) {
            System.out.println("flashtime field must be integer");
        }

        return flashtime;
    }
    public static String getUserAPIKey() throws FileNotFoundException {

        String userAPIkey = null;

        //to load application's properties, we use this class
        Properties mainProperties = null;
        try {
            mainProperties = getFileProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //retrieve the property we are intrested, the app.version
        userAPIkey = mainProperties.getProperty("userAPIkey");

        return userAPIkey;
    }
    public static String getPort() throws FileNotFoundException {

        String port = null;

        //to load application's properties, we use this class
        Properties mainProperties = null;
        try {
            mainProperties = getFileProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //retrieve the property we are intrested, the app.version
        port = mainProperties.getProperty("port");

        return port;
    }
    public static String getIp() throws FileNotFoundException {

        String ip = null;

        //to load application's properties, we use this class
        Properties mainProperties = null;
        try {
            mainProperties = getFileProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //retrieve the property we are intrested, the app.version
        ip = mainProperties.getProperty("ip");

        return ip;
    }
    public static String getPage2Flash() throws FileNotFoundException {

        String Page2Flash = null;

        //to load application's properties, we use this class
        Properties mainProperties = null;
        try {
            mainProperties = getFileProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //retrieve the property we are intrested, the app.version
        Page2Flash = mainProperties.getProperty("Page2Flash");

        return Page2Flash;
    }
    //Import from CSV(TXT) one item per row
    public static List<String> CsvToList(String csvFile) throws FileNotFoundException {

        //String csvFile = "src/items.csv";
        Scanner scanner = null;
        scanner = new Scanner(new File(csvFile));
        List<String> line = new ArrayList<>();
        while (scanner.hasNext()) {
            line.add(ImportCSV.parseLine(scanner.nextLine()).toString());
        }
        scanner.close();
        return line;
    }

}
