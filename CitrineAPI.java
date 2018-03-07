import java.lang.StringBuilder;
import java.io.*;
import java.util.*;
import javafx.util.Pair;

public class CitrineAPI{
/**
 * This function allows user to add chemical and its properties to the end of a specific file.
 * <p>
 * The function also checks if the user entered a valid chemical name formatted in a special format.
 * <ul>
 *  <li> No number is followed after a single element chemical </li>
 *  <li> Every element in a compound is followed by the number of element(s) presents in the compound (even if it has only 1 element) </li>
 *  <li> It does not check the ironic states correctness of the chemical.
 * </ul>
 * </p>
 * @param fileName Name of the file that user desires to operate on
 * @param chemical To add a chemical to the file in write function
 * @param value The band gap of the chemical
 * @param color The color of the chemical
 */
    public static void write(String fileName, String chemical,double value, String color){
        BufferedWriter bw = null;
        FileWriter fw = null;

        // begin : User input exceptions handling
        Pair<Hashtable<String,String>,HashSet<String>> dictionaries = loadDictionary("elements.txt");
        Hashtable<String,String> nameDictionary = dictionaries.getKey();
        HashSet<String> symbolDictionary = dictionaries.getValue();
        checkValidChemical(chemical,symbolDictionary);

        if (value < 0){
            throw new IllegalArgumentException("Please enter a value greater than 0");
        }
        if (chemical.length() == 0){
            throw new IllegalArgumentException("Please enter a chemical name for entry");
        }
        if (color.length() == 0){
            throw new IllegalArgumentException("Please enter the color of the chemical");
        }
        // end : User input exceptions handling
        
        try{ //adding chemical, property and values to the cvs file
            StringBuilder sb = new StringBuilder();
            sb.append(chemical);
            sb.append(',');
            sb.append("Band gap");
            sb.append(',');
            sb.append(String.valueOf(value));
            sb.append(',');
            sb.append("Color");
            sb.append(',');
            sb.append(color);
            sb.append(System.getProperty("line.separator"));

            File file = new File(fileName);

            fw = new FileWriter(file.getAbsoluteFile(), true);
		    bw = new BufferedWriter(fw);
        
            bw.write(sb.toString());
    
            bw.close();
            fw.close();
        } // end try
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        } //end catch
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
        } // end catch
        
    }
/**
 * This function allows user to search all chemicals that contains a certain element.
 * <p>
 * The function takes the input of the name or the symbol of the element, and returns a list of matched result in List<String>.
 * </p>
 * @param fileName Name of the file that user desires to operate on
 * @param element To search chemicals by its element (i.e. Carbon, carbon,C)
 * @return List<String> Search results
 */
    public static List<String> search(String fileName, String element){
        //begin : Check valid user input
        Pair<Hashtable<String,String>,HashSet<String>> dictionaries = loadDictionary("elements.txt");
        Hashtable<String,String> nameDictionary = dictionaries.getKey();
        HashSet<String> symbolDictionary = dictionaries.getValue();
        checkValidElement(element, nameDictionary, symbolDictionary);
        if (element.length() > 2){
            element = element.toLowerCase();
            element = nameDictionary.get(element);
        } // end if

        if (element.length() == 0){
            throw new IllegalArgumentException("Please enter an element to search");
        }
        //end : Check valid user input
        String line = null;
        String seperator = ",";
        List<String> searchResult = new LinkedList<>();
        
        try { // searching the file to see if the chemical contains the user desired element.
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            line = bufferedReader.readLine();
            while((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(seperator); // data[0] = chemical name , data[1] = Property 1 name, data[2] = Property 1 value,  data[3] = Property 2 name, data[4] = Property 2 value
                if (data[0].charAt(data[0].length()-1) < '0' || data[0].charAt(data[0].length()-1) > '9'){ // if the chemical name does not end with a number : i.e. single element chemical name
                    if (element.equals(data[0])){
                        searchResult.add(line);
                    }
                }
                if (element.length() == 1){ // for Single character symbol elements
                    for(int i = 0 ; i < data[0].length() - 1; i++){
                        // Searching for the String where its character is followed by a number
                        if (data[0].charAt(i) == element.charAt(0) && (data[0].charAt(i+1) > '0' && data[0].charAt(i+1) <= '9')){ 
                            searchResult.add(line);
                            break;
                        }
                    }
                }else if (element.length() == 2){ // for double character symbol element
                    if (data[0].contains(element)){
                        searchResult.add(line);
                    }
                }
            }   
            bufferedReader.close();
        }//end try
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        } // end catch
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
        } // end catch
        return searchResult;
    }
/**
 * This function allows user to search all chemicals that band gap of a certain range.
 * <p>
 * The function takes the input of range(min,max) that the users desire, and return all results that matches the input range.
 * </p>
 * @param fileName Name of the file that user desires to operate on
 * @param min Min of the search range
 * @param max Max of the search range
 * @return List<String> Search results
 */
    public static List<String> search(String fileName, double min, double max){
        // begin : check valid user input
        if (max < min){
            throw new IllegalArgumentException("Please enter a valid range");
        }
        if (min < 0){
            throw new IllegalArgumentException("Please enter a ranger greater than 0");
        }
        // end : check balid user input
        String line = null;
        String seperator = ",";
        List<String> searchResult = new LinkedList<>();
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            line = bufferedReader.readLine();
            while((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(seperator); // data[0] = chemical name , data[1] = Property 1 name, data[2] = Property 1 value,  data[3] = Property 2 name, data[4] = Property 2 value
                if (Double.parseDouble(data[2]) >= min && Double.parseDouble(data[2]) <= max){
                    searchResult.add(line);
                }
            }   
            bufferedReader.close();
        } // end try
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        } // end catch
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
        } // end catch
        return searchResult;
    }
/**
 * This function allows user to search all chemicals that contains a certain element as well as the band gap range
 * <p>
 * The function takes the input of the name or the symbol of the element (i.e. C,Carbon,carbon), that also matches the band gap range, and returns a list of matched result in List<String> 
 * </p>
 * @param fileName Name of the file that user desires to operate on
 * @param element To search chemicals by its element (i.e. Carbon, carbon,C)
 * @param min Min of the search range
 * @param max Max of the search range
 * @return List<String> Search results
 */

    public static List<String> search(String fileName, String element, double min, double max){
        // begin : check valid user input
        if (max < min){
            throw new IllegalArgumentException("Please enter a valid range");
        }
        if (element.length() == 0){
            throw new IllegalArgumentException("Please enter an element to search");
        }
        // if (min < 0){ 
        //     throw new IllegalArgumentException("Please enter a ranger greater than 0");
        // }
        // end : check valid user input
        String seperator = ",";
        List<String> searchResult = new LinkedList<>();
        searchResult = search(fileName,element);
        Iterator itr = searchResult.iterator();
        while(itr.hasNext()) {
            String[] data = ((String) itr.next()).split(seperator); // data[0] = chemical name , data[1] = Property 1 name, data[2] = Property 1 value,  data[3] = Property 2 name, data[4] = Property 2 value
            if (Double.parseDouble(data[2]) < min || Double.parseDouble(data[2]) > max){
                itr.remove(); // removing results that do not fit the range
            }
        }// end while
        return searchResult;
    }

    // This function checks if the Chemical the user entered follows the correct naming
    private static void checkValidChemical(String chemical,HashSet<String> symbolDictionary){ // function for checking if a chemical is a valid input
        String[] splitChemical = chemical.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)"); // splitting numbers and other characters // [A-Z][a-z]?|\\d?
        // begin : special case for Single element chemical
        if (splitChemical.length == 1 && symbolDictionary.contains(splitChemical[0])){
            return;
        }else if (splitChemical.length == 1 && !symbolDictionary.contains(splitChemical[0])){
            throw new IllegalArgumentException("Please enter a valid element name");
        }
        // end : special case for Single element chemical

        // begin : general cases
        if (splitChemical.length%2 == 1){
            throw new IllegalArgumentException("Please enter chemical in format of element#element#");
        }
        for(int i = 0 ; i < splitChemical.length/2 ; i++){
            if (!symbolDictionary.contains(splitChemical[i*2])){
                throw new IllegalArgumentException("Please enter a valid chemical name");
            }
            try{
                if(Integer.parseInt(splitChemical[i*2+1])<=0){
                    throw new IllegalArgumentException("Please enter a valid chemical name"); 
                };
            }catch(IllegalStateException e){
                throw new IllegalArgumentException("Please enter a valid chemical name");
            }
        } // end for
        // end : general cases
    }

    // this function returns a Pair of element name dictionary (HashTable) <Key = name, Value = Symbol>, and a HashSet<Key = Symbol> for finding element symbols.
    private static Pair<Hashtable<String,String>,HashSet<String>> loadDictionary(String dictionaryFileName){ // function for loading dictionaries for element names and symbols
        Hashtable<String,String> nameDictionary = new Hashtable<>();
        HashSet<String> symbolDictionary = new HashSet<>();
        try {
            FileReader fileReader = new FileReader(dictionaryFileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String atomicNumber = new String();
            while((atomicNumber = bufferedReader.readLine()) != null) {
                String elementSymbol = bufferedReader.readLine();
                String elementName = bufferedReader.readLine();
                nameDictionary.put(elementName.toLowerCase(),elementSymbol);
                symbolDictionary.add(elementSymbol);
            }   
            bufferedReader.close();
        } // end try
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                dictionaryFileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + dictionaryFileName + "'");                  
        }
        return new Pair<Hashtable<String,String>,HashSet<String>> (nameDictionary,symbolDictionary);
    }
    // This function checks if the user enters a valid element for searching
   private static void checkValidElement(String element, Hashtable<String,String> nameDictionary, HashSet<String> symbolDictionary){ // function for checking if the element is valid
        if (element.length() > 2){
            element = element.toLowerCase();
        }
        if (!symbolDictionary.contains(element) && !nameDictionary.containsKey(element)){
            throw new IllegalArgumentException("Please enter a valid element name / symbol");
        }
    }
 }