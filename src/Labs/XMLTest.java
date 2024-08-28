package Labs;

import java.util.*;

interface XMLComponent {
    void addAttribute(String attribute, String value);
    void print(int indentLevel);
}

class XMLLeaf implements XMLComponent {
    private String tagName;
    private String value;
    private Map<String, String> attributes;

    public XMLLeaf(String tagName, String value) {
        this.tagName = tagName;
        this.value = value;
        this.attributes = new LinkedHashMap<>();
    }

    @Override
    public void addAttribute(String attribute, String value) {
        attributes.put(attribute, value);
    }

    @Override
    public void print(int intentLevel) {
        printIndent(intentLevel);
        System.out.print("<" + tagName);
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            System.out.print(" " + entry.getKey() + "=\"" + entry.getValue() + "\"");
        }
        System.out.println(">" + value + "</" + tagName + ">");
    }
    public void printIndent(int level){
        for(int i=0;i< level;i++){
            System.out.print("\t");
        }
    }
}
class XMLComposite implements XMLComponent {
    private String tagName;
    private List<XMLComponent> components;
    private Map<String, String> attributes;

    public XMLComposite(String tagName) {
        this.tagName = tagName;
        this.components = new ArrayList<>();
        this.attributes = new LinkedHashMap<>();
    }

    public void addComponent(XMLComponent component) {
        components.add(component);
    }

    @Override
    public void addAttribute(String attribute, String value) {
        attributes.put(attribute, value);
    }

    @Override
    public void print(int intentLevel) {
        printIndent(intentLevel);
        System.out.print("<" + tagName);
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            System.out.print(" " + entry.getKey() + "=\"" + entry.getValue() + "\"");
        }
        System.out.println(">");

        for (XMLComponent component : components) {
            component.print(intentLevel + 1);
        }
        printIndent(intentLevel);
        System.out.println("</" + tagName + ">");
    }
    public void printIndent(int level){
        for(int i=0;i<level;i++){
            System.out.print("\t");
        }
    }
}

public class XMLTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = sc.nextInt();
        XMLComponent component = new XMLLeaf("student", "Trajce Trajkovski");
        component.addAttribute("type", "redoven");
        component.addAttribute("program", "KNI");

        XMLComposite composite = new XMLComposite("name");
        composite.addComponent(new XMLLeaf("first-name", "trajce"));
        composite.addComponent(new XMLLeaf("last-name", "trajkovski"));
        composite.addAttribute("type", "redoven");
        //composite.addAttribute("program", "KNI");

        if (testCase == 1) {
            component.print(0);
        } else if (testCase == 2) {
            composite.print(0);
        } else if (testCase == 3) {
            XMLComposite main = new XMLComposite("level1");
            main.addAttribute("level", "1");
            XMLComposite lvl2 = new XMLComposite("level2");
            lvl2.addAttribute("level", "2");
            XMLComposite lvl3 = new XMLComposite("level3");
            lvl3.addAttribute("level", "3");
            lvl3.addComponent(component);
            lvl2.addComponent(lvl3);
            lvl2.addComponent(composite);
            lvl2.addComponent(new XMLLeaf("something", "blabla"));
            main.addComponent(lvl2);
            main.addComponent(new XMLLeaf("course", "napredno programiranje"));

            main.print(0);
        }
    }
}

