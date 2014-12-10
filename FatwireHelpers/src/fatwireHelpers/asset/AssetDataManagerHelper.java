package fatwireHelpers.asset;

import java.util.Collections;
import java.util.Properties;

import com.fatwire.assetapi.common.AssetAccessException;
import com.fatwire.assetapi.data.AssetData;
import com.fatwire.assetapi.data.AssetDataManager;
import com.fatwire.assetapi.def.AssetTypeDefManager;
import com.fatwire.system.Session;
import com.fatwire.system.SessionFactory;

public class AssetDataManagerHelper {
	public static AssetDataManager getManager() {
		setFatwireProperties();
		Session ses = SessionFactory.newSession("fwadmin", "xceladmin");
		AssetDataManager mgr = (AssetDataManager) ses.getManager(AssetDataManager.class.getName());
		return mgr;
	}
	
	public static AssetTypeDefManager getAssetTypeDefManager() {
		setFatwireProperties();
		Session ses = SessionFactory.newSession("fwadmin", "xceladmin");
		AssetTypeDefManager mgr = (AssetTypeDefManager) ses.getManager(AssetTypeDefManager.class.getName());
		return mgr;
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
