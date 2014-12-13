package fatwireHelpers.tests.asset;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fatwire.assetapi.common.AssetAccessException;
import com.fatwire.assetapi.data.AssetData;
import com.fatwire.assetapi.data.AssetDataManager;
import com.fatwire.assetapi.data.AssetId;
import com.fatwire.assetapi.data.AttributeData;
import com.fatwire.assetapi.data.BlobObject;
import com.fatwire.assetapi.data.BlobObjectImpl;
import com.fatwire.assetapi.def.AssetTypeDef;
import com.fatwire.assetapi.def.AssetTypeDefManager;
import com.fatwire.assetapi.def.AttributeDef;
import com.fatwire.assetapi.query.Condition;
import com.fatwire.assetapi.query.ConditionFactory;
import com.fatwire.assetapi.query.OpTypeEnum;
import com.fatwire.assetapi.query.Query;
import com.fatwire.assetapi.query.SimpleQuery;
import com.openmarket.xcelerate.asset.AssetIdImpl;

import fatwireHelpers.asset.AssetDataManagerHelper;

public class AssetDataManagerTests {
	private static AssetDataManagerHelper admMgr;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		admMgr = AssetDataManagerHelper.getInstance(
			new HashMap<String, String>() {{
			   put("cs.uid", "fwadmin");
			   put("cs.pwd", "xceladmin");
			   put("cs.dburl", "jdbc:jtds:sqlserver://localhost:1433;instanceName=SQLEXPRESS;DatabaseName=Fatwire;");
			   put("cs.dbdriver", "net.sourceforge.jtds.jdbc.Driver");
			   put("cs.dbuid", "Fatwire");
			   put("cs.dbpwd", "Fatwire");
			   put("cs.installDir", "C:\\fatwire");			   
			}}
		);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void ShouldCreateAssetDataManagerHelper() {
		AssetDataManager mgr = admMgr.getManager();
		assertNotNull(mgr);
	}

	@Test
	public final void DataManagerHelper_Should_HelpReadBlobData()
			throws Exception {
		AssetDataManager mgr = admMgr.getManager();
		Query query = new SimpleQuery("CSElement", null,
				ConditionFactory.createCondition("id", OpTypeEnum.EQUALS,
						"1387007373693"), Collections.singletonList("url"));
		for (AssetData data : mgr.read(query)) {
			AttributeData docAttr = data.getAttributeData("url");
			BlobObject fileObj = (BlobObject) docAttr.getData();
			byte[] d = new byte[fileObj.getBinaryStream().available()];
			fileObj.getBinaryStream().read(d);
			System.out.println("id = " + data.getAssetId());
			System.out.println("file name = " + fileObj.getFilename());
			System.out.println("file size = " + d.length);
			System.out.println("");
			System.out.write(d);
		}
	}

	@Test
	public final void DataManagerHelper_Should_HelpWriteBlobData() throws Exception {
		List<AssetData> sAssets = new ArrayList<AssetData>();
		AssetDataManager mgr = admMgr.getManager();
		
		Iterable<AssetData> assets = mgr.read( Arrays.<AssetId>asList(
				new AssetIdImpl( "CSElement", 1387007373693L))
				);
		
		for (AssetData assetData : assets) {
			AttributeData docAttr = assetData.getAttributeData("url");
			BlobObject currentBlob = (BlobObject) docAttr.getData();
			BlobObject fileObj = new BlobObjectImpl(currentBlob.getFilename(), null, dummyCsElement());
			assetData.getAttributeData( "url" ).setData( fileObj );
			sAssets.add(assetData);
		}
		
		mgr.update(sAssets);
		System.out.println("CSElement updated!!");
	}

	@Test
	public final void AssetDataManagerHelperShouldHelpMakeQuery() {
		// testReadAssetAttribute("Content_C", "FSII Article", "FSIIHeadline");
		testReadAssetAttribute("CSElement", null, "url");
	}

	@Test
	public final void AssetDataManagerHelperShouldHelpReadAssetDefinitions()
			throws AssetAccessException {
		// testReadAssetDefinitions("Document_C", "FSII Document");
		// testReadAssetDefinitions("Content_C", "FSII Article");
		testReadAssetDefinitions("CSElement", null);
		testReadAssetDefinitions("HelloArticle", null);
	}

	private void testReadAssetAttribute(String assetType, String subType,
			String attribute) {
		AssetDataManager mgr = admMgr.getManager();
		Query q = new SimpleQuery(assetType, subType, null,
				Collections.singletonList(attribute));

		try {
			for (AssetData data : mgr.read(q)) {
				System.out.println(data.getAttributeData(attribute).getData());
			}
		} catch (AssetAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void testReadAssetDefinitions(String assetType, String subType) {
		AssetTypeDefManager mgr = admMgr.getAssetTypeDefManager();
		AssetTypeDef defMgr = null;
		try {
			defMgr = mgr.findByName(assetType, subType);
		} catch (AssetAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("");
		System.out
				.println("Asset type description: " + defMgr.getDescription());
		System.out.println("");
		for (AttributeDef attrDef : defMgr.getAttributeDefs()) {
			System.out.println("Attribute name: " + attrDef.getName());
			System.out.println("Attribute description: "
					+ attrDef.getDescription());
			System.out.println("is required: " + attrDef.isDataMandatory());
			System.out.println("Attribute type: " + attrDef.getType());
			System.out.println("");
		}
	}

	private byte[] dummyCsElement() {
		StringBuilder builder = new StringBuilder();
		builder.append("<%@ taglib prefix=\"cs\" uri=\"futuretense_cs/ftcs1_0.tld\"\r\n");
		builder.append("%><%@ taglib prefix=\"asset\" uri=\"futuretense_cs/asset.tld\"\r\n");
		builder.append("%><%@ taglib prefix=\"assetset\" uri=\"futuretense_cs/assetset.tld\"\r\n");
		builder.append("%><%@ taglib prefix=\"commercecontext\" uri=\"futuretense_cs/commercecontext.tld\"\r\n");
		builder.append("%><%@ taglib prefix=\"ics\" uri=\"futuretense_cs/ics.tld\"\r\n");
		builder.append("%><%@ taglib prefix=\"listobject\" uri=\"futuretense_cs/listobject.tld\"\r\n");
		builder.append("%><%@ taglib prefix=\"render\" uri=\"futuretense_cs/render.tld\"\r\n");
		builder.append("%><%@ taglib prefix=\"searchstate\" uri=\"futuretense_cs/searchstate.tld\"\r\n");
		builder.append("%><%@ taglib prefix=\"siteplan\" uri=\"futuretense_cs/siteplan.tld\"\r\n");
		builder.append("%><%@ page import=\"COM.FutureTense.Interfaces.*,\r\n");
		builder.append("                   COM.FutureTense.Util.ftMessage,\r\n");
		builder.append("                   COM.FutureTense.Util.ftErrors\"\r\n");
		builder.append("%><cs:ftcs><%-- tests/dummy\r\n");

		builder.append("INPUT\r\n");

		builder.append("OUTPUT\r\n");

		builder.append("--%>\r\n");
		builder.append("<%-- Record dependencies for the SiteEntry and the CSElement --%>\r\n");
		builder.append("<ics:if condition='<%=ics.GetVar(\"seid\")!=null%>'><ics:then><render:logdep cid='<%=ics.GetVar(\"seid\")%>' c=\"SiteEntry\"/></ics:then></ics:if>\r\n");
		builder.append("<ics:if condition='<%=ics.GetVar(\"eid\")!=null%>'><ics:then><render:logdep cid='<%=ics.GetVar(\"eid\")%>' c=\"CSElement\"/></ics:then></ics:if>\r\n");

		Calendar cal = Calendar.getInstance();
		cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMMYY HH:mm:ss");
		builder.append(sdf.format(cal.getTime()) + " : updated CSElement!!!!!<br />");
		
		builder.append("</cs:ftcs>\r\n");
		
		return builder.toString().getBytes();
	}
}
