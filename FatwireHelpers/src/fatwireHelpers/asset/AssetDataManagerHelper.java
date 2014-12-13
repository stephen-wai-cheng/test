package fatwireHelpers.asset;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import com.fatwire.assetapi.common.AssetAccessException;
import com.fatwire.assetapi.data.AssetData;
import com.fatwire.assetapi.data.AssetDataManager;
import com.fatwire.assetapi.def.AssetTypeDefManager;
import com.fatwire.system.Session;
import com.fatwire.system.SessionFactory;

public class AssetDataManagerHelper {
	private static volatile AssetDataManagerHelper instance = null;
	private static volatile Session ses = null;
	
    public static AssetDataManagerHelper getInstance(Map<String, String> configSettings) {
        if (instance == null) {
            synchronized (AssetDataManagerHelper.class) {
                // Double check
                if (instance == null) {
                    instance = new AssetDataManagerHelper();
					Properties props = System.getProperties();
					props.setProperty("cs.dburl", configSettings.get("cs.dburl"));
					props.setProperty("cs.dbdriver", configSettings.get("cs.dbdriver"));
					props.setProperty("cs.dbuid", configSettings.get("cs.dbuid"));
					props.setProperty("cs.dbpwd", configSettings.get("cs.dbpwd"));
					props.setProperty("cs.installDir", configSettings.get("cs.installDir"));
					ses = SessionFactory.newSession(
							configSettings.get("cs.uid"),
							configSettings.get("cs.pwd"));
                }
            }
        }
        return instance;
    }
	
	public AssetDataManager getManager() {
		AssetDataManager mgr = (AssetDataManager) ses.getManager(AssetDataManager.class.getName());
		return mgr;
	}
	
	public AssetTypeDefManager getAssetTypeDefManager() {
		AssetTypeDefManager mgr = (AssetTypeDefManager) ses.getManager(AssetTypeDefManager.class.getName());
		return mgr;
	}

}
