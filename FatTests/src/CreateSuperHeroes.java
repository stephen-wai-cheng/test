import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.fatwire.assetapi.common.AssetAccessException;
import com.fatwire.assetapi.data.AssetData;
import com.fatwire.assetapi.data.AssetDataManager;
import com.fatwire.assetapi.data.AssetId;
import com.fatwire.assetapi.data.AttributeData;
import com.fatwire.assetapi.data.BlobObject;
import com.fatwire.assetapi.data.MutableAssetData;
import com.fatwire.assetapi.query.ConditionFactory;
import com.fatwire.assetapi.query.OpTypeEnum;
import com.fatwire.assetapi.query.Query;
import com.fatwire.assetapi.query.SimpleQuery;
import com.openmarket.xcelerate.asset.AssetIdImpl;

import fatwireHelpers.asset.AssetDataManagerHelper;


public class CreateSuperHeroes {
	private static AssetDataManager assetDataManager;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		createSuperAssets();
		
		createSuperAssociations();
		
		System.out.println("Done!!");
	}

	private static void createSuperAssociations() {
		createSuperAssociation("Superman","x-ray vision");
		createSuperAssociation("Superman","Super strength");
		createSuperAssociation("Superman","Able to fly");
		createSuperAssociation("The Atom","Super strength");
		createSuperAssociation("Dr. Manhattan","Telepathic");
		createSuperAssociation("Dr. Manhattan","Precognition");
		createSuperAssociation("Dr. Manhattan","Mind control");
		createSuperAssociation("The Flash","Super speed");
	}

	private static void createSuperAssets() {
		List<String> heroes = Arrays.asList(
				"Superman",
				"The Atom",
				"Batgirl",
				"Batman",
				"Beast Boy",
				"Black Canary",
				"Captain Marvel",
				"Dr. Manhattan",
				"The Flash",
				"Green Arrow",
				"Green Lantern",
				"Huntress",
				"Wonder Woman");
		List<String> superPowers = Arrays.asList(
				"x-ray vision",
				"Super strength",
				"Super speed",
				"Telepathic",
				"Able to fly",
				"Able to breath under water",
				"Telekinesis",
				"Mutation",
				"Power mimicry or absorption",
				"Invisibility",
				"Precognition",
				"Mind control",
				"Teleportation");
		assetDataManager = AssetDataManagerHelper.getManager();
		for (String hero : heroes){
			CreateSuperHeroAsset("SuperHero", hero);
		}
		
		for (String power : superPowers){
			CreateSuperHeroAsset("SuperPower", power);
		}
	}

	private static void createSuperAssociation(String superHero, String superPower) {
		AssetId parentAssetId = new AssetIdImpl( "SuperHero", getHeroId(superHero));
		
		Query query = new SimpleQuery("SuperPower", null,
				ConditionFactory.createCondition("name", OpTypeEnum.EQUALS, superPower),
				Collections.singletonList("name"));		
		try {
			if (assetDataManager.read(query).iterator().hasNext()){
				AssetData assetData = assetDataManager.read(query).iterator().next();
				assetData.getParents().add(parentAssetId);
				List<AssetData> sAssets = new ArrayList<AssetData>();
				sAssets.add(assetData);
				assetDataManager.update( sAssets );
			}
		} catch (AssetAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Long getHeroId(String superHero) {
		AssetId assetId = null;
		Query query = new SimpleQuery("SuperHero", null,
				ConditionFactory.createCondition("name", OpTypeEnum.EQUALS, superHero),
				Collections.singletonList("name"));
		try {
			if (assetDataManager.read(query).iterator().hasNext()){
				AssetData data = assetDataManager.read(query).iterator().next();
				assetId = data.getAssetId();
			}
		} catch (AssetAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return assetId.getId();
	}

	private static void CreateSuperHeroAsset(String assetType, String assetValue) {
		Query query = new SimpleQuery(assetType, null,
				ConditionFactory.createCondition("name", OpTypeEnum.EQUALS, assetValue),
				Collections.singletonList("name"));

		try {
			if (!assetDataManager.read(query).iterator().hasNext()){
				MutableAssetData d = assetDataManager.newAssetData(assetType, "");
				d.getAttributeData("name").setData(assetValue);
				d.getAttributeData("description").setData(assetValue);
				d.getAttributeData("Publist").setData(Arrays.asList("tests"));
				assetDataManager.insert( Arrays.<AssetData>asList(d));
			}
		} catch (AssetAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
