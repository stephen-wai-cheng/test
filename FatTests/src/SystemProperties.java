import java.io.FileInputStream;
import java.util.*;

public class SystemProperties {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		displayServletRequestProperties();
		//displaySystemProperties();
	}
	private static void displayServletRequestProperties() throws Exception {
        Properties p = new Properties(System.getProperties());
        p.load(new FileInputStream(
        	"C:/Development/Java/apache-tomcat-7.0.32/webapps/cs/WEB-INF/classes/ServletRequest.properties")
        );

        // set the system properties
        System.setProperties(p);

		SortedMap<?, ?> sortedSystemProperties = new TreeMap(System.getProperties());
		Set<?> keySet = sortedSystemProperties.keySet();
		Iterator<?> iterator = keySet.iterator();
		while (iterator.hasNext()) {
			String propertyName = (String) iterator.next();
			String propertyValue = System.getProperties().getProperty(propertyName);
			System.out.println(propertyName + " : " + propertyValue);
		}
        // System.getProperties().list(System.out);		
	}
	
	private static void displaySystemProperties(){
		Properties props = System.getProperties();
	    Enumeration<?> e = props.propertyNames();

	    while (e.hasMoreElements()) {
	      String key = (String) e.nextElement();
	      System.out.println(key + " : " + props.getProperty(key));
	    }
	}

	
}
