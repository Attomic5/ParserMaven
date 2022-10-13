import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, ParserConfigurationException, TransformerException, SAXException {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        String dataJson = "data.json";

        // создание data.csv
//        String[] employee = "2,Inav,Petrov,RU,23".split(",");
//        try(CSVWriter writer = new CSVWriter(new FileWriter("data.csv", true))){
//            writer.writeNext(employee);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        // csv-json парсер
//        List<Employee> list = parseCSV(columnMapping, fileName);
//        String json = listToJson(list);
//        writeString(json, dataJson);

        // создание data.xml
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder builder = factory.newDocumentBuilder();
//        Document document = builder.newDocument();
//
//        Element staff = document.createElement("staff");
//        document.appendChild(staff);
//
//        Element employee = document.createElement("employee");
//        staff.appendChild(employee);
//        Element id = document.createElement("id");
//        id.appendChild(document.createTextNode("1"));
//        employee.appendChild(id);
//        Element firstName = document.createElement("firstName");
//        firstName.appendChild(document.createTextNode("John"));
//        employee.appendChild(firstName);
//        Element lastName = document.createElement("lastName");
//        lastName.appendChild(document.createTextNode("Smith"));
//        employee.appendChild(lastName);
//        Element country = document.createElement("country");
//        country.appendChild(document.createTextNode("USA"));
//        employee.appendChild(country);
//        Element age = document.createElement("age");
//        age.appendChild(document.createTextNode("25"));
//        employee.appendChild(age);
//
//        Element employee2 = document.createElement("employee");
//        staff.appendChild(employee2);
//        Element id2 = document.createElement("id");
//        id2.appendChild(document.createTextNode("2"));
//        employee2.appendChild(id2);
//        Element firstName2 = document.createElement("firstName");
//        firstName2.appendChild(document.createTextNode("Inav"));
//        employee2.appendChild(firstName2);
//        Element lastName2 = document.createElement("lastName");
//        lastName2.appendChild(document.createTextNode("Petrov"));
//        employee2.appendChild(lastName2);
//        Element country2 = document.createElement("country");
//        country2.appendChild(document.createTextNode("RU"));
//        employee2.appendChild(country2);
//        Element age2 = document.createElement("age");
//        age2.appendChild(document.createTextNode("23"));
//        employee2.appendChild(age2);
//
//        DOMSource domSource = new DOMSource(document);
//        StreamResult streamResult = new StreamResult(new File("data.xml"));
//
//        TransformerFactory transformerFactory = TransformerFactory.newInstance();
//        Transformer transformer = transformerFactory.newTransformer();
//        transformer.transform(domSource, streamResult);

        // xml-json парсер
        String data2Json = "data2.json";

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File("data.xml"));
        Node root = doc.getDocumentElement();

        List<Employee> list = parseXML(root);
        String json = listToJson(list);
        writeString(json, data2Json);

    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> list = null;
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            list = csv.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        String json = gson.toJson(list, listType);
        return json;
    }

    public static void writeString(String json, String dataJson) {
        try (FileWriter file = new FileWriter(dataJson)) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Employee> parseXML(Node node) {
        List<Employee> list = new ArrayList<>();
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node_ = nodeList.item(i);
            if (Node.ELEMENT_NODE == node_.getNodeType() && node_.getNodeName().equals("employee")) {
                Element element = (Element) node_;
                String content = element.getTextContent();
                String newContent = content.trim().replaceAll("\n        ", ",");
                String[] arrayContent = newContent.split(",");
                long id = Long.parseLong(arrayContent[0]);
                int age = Integer.parseInt(arrayContent[4]);
                Employee employee = new Employee(id, arrayContent[1], arrayContent[2], arrayContent[3], age);
                list.add(employee);
            }
            parseXML(node_);
        }
        return list;
    }

}
