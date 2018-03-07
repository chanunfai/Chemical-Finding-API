# CitrineAPI Usage

The API provides three search functions and one write function. It also checks if the user input for validity of chemical's names or element's before writing on file or searching.

All search functions returns a single LinkedList<String> that contains all the search results.

- search(String fileName,double min, double max) : 

ex. Return a LinkedList<String> where the band gap is between 0 and 3.

```java
CitrineAPI.search("data.cvs",0,3);
```

- search(String fileName, String element) : 

ex. Return a LinkedList<String> where the compound contains Gallium

```java
CitrineAPI.search("data.cvs","Gallium");
CitrineAPI.search("data.cvs","Ga");
CitrineAPI.search("data.cvs","gaLLiUm");
```

- search(String fileName, String element, double min, double max) : 

ex. Return all records where the compound contains Gallium and the band gap is between 0 and 3.

```java
CitrineAPI.search("data.cvs","Gallium",0,3);
CitrineAPI.search("data.cvs","Ga",0,3);
CitrineAPI.search("data.cvs","gaLLiUm",0,3);
```
The write function checks for correctness and format of the chemical entered and allows user to append new entry to the end of the file.

- write(String fileName, String chemical, double value, String color) :

ex. Append a new chemical with valid properties to the end of the file.

```java
CitrineAPI.write("data.cvs","Cd1I2" , 3.19 , "White");
```


