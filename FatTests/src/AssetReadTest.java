import java.util.*;

import com.fatwire.assetapi.common.AssetAccessException;
import com.fatwire.assetapi.data.AssetData;
import com.fatwire.assetapi.data.AssetDataManager;
import com.fatwire.assetapi.query.Query;
import com.fatwire.assetapi.query.SimpleQuery;
import com.fatwire.system.Session;
import com.fatwire.system.SessionFactory;

public class AssetReadTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		setFatwireProperties();
		testFatwire();
	}

	private static void testFatwire() {
		Session ses = SessionFactory.newSession("fwadmin", "xceladmin");
		AssetDataManager mgr = (AssetDataManager) ses.getManager(AssetDataManager.class.getName());
		Query q = new SimpleQuery("Content_C", "FSII Article", null,
		Collections.singletonList("FSIIHeadline") );
		try {
			for( AssetData data : mgr.read( q ) )
			{
				System.out.println( data.getAttributeData("FSIIHeadline").getData() );
			}
		} catch (AssetAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Done!!");
	}

	private static void setFatwireProperties() {
		Properties props = System.getProperties();
		props.setProperty("cs.dburl", "jdbc:jtds:sqlserver://localhost:1433;instanceName=SQLEXPRESS;DatabaseName=Fatwire;");
		props.setProperty("cs.dbdriver", "net.sourceforge.jtds.jdbc.Driver");
		props.setProperty("cs.dbuid", "Fatwire");
		props.setProperty("cs.dbpwd", "Fatwire");
		props.setProperty("cs.installDir", "C:\\fatwire");
	}

}
