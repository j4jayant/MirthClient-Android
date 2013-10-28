package com.j4jayant.android.mirthclient;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.example.mirthclient.R;
import com.j4jayant.android.mirthclient.utils.GenericConstants.Action;
import com.j4jayant.android.mirthclient.utils.ExpandableListAdapter;
import com.j4jayant.android.mirthclient.pojo.ChannelItem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;

import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.TextView;

public class DashboardActivity extends Activity  {

	ExpandableListView list;
	
	private volatile Action nextAction = Action.DASHBOARD;
	private volatile String nextActionOnChannel = null;
	
	ArrayList<ChannelItem> channels = null;
    TextView txtErrMessage = null;
    TextView txtStatusMessage = null;
    
    String url = null;
    String username = null;
    String password = null;
    String clientListener = null;
    
    Map<String, List<String>> channelMap = null;
    List<String> groupList = null;
    
    private final Handler handler = new Handler();
    private ExpandableListAdapter expListAdapter = null;
    
    private int INTERVAL = 10000;
    
    DisplayMetrics metrics;
    int width;
    
	@SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);
        
        channels = new ArrayList<ChannelItem>();
        channelMap = new HashMap<String, List<String>>();
        groupList = new ArrayList<String>();
        
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
        txtErrMessage = (TextView) findViewById(R.id.txtErrorMessage);
        txtErrMessage.setText("");
        
        txtStatusMessage = (TextView) findViewById(R.id.txtStatusMessage);
        txtStatusMessage.setText("");
        
        Intent intent = getIntent();
        
        url = intent.getStringExtra("url");
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        clientListener = intent.getStringExtra("clientListener");
        
        StringBuilder sb = new StringBuilder();
        sb.append("Connecting Mirth Server on " + url + "\n");
        sb.append("with user " + username + "\n");
        sb.append("client listener " + clientListener + "\n");
        
        txtStatusMessage.setText(sb.toString());
        
        doRefreshingStuff();
	}
	
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.login, menu);
	        return true;
	    }
	 
	 private void doTheAutoRefresh() {
		    handler.postDelayed(new Runnable() {
		             @Override
		             public void run() {
		                 doRefreshingStuff(); 
		             }
		         }, INTERVAL);
		}
	 
	 private void doRefreshingStuff()
	 {
		 String action = nextAction.toString();
		 String channelId = nextActionOnChannel;
		 
		 String strMirth = "";
		 
		 if(channelId != null)
		 {
			 strMirth = "<Message><url>"+ url +"</url><user>"+ username +"</user><password>" + password + "</password><action>" + action + "</action><channelId>" + channelId + "</channelId></Message>";
		 }
		 else
		 {
			 strMirth = "<Message><url>"+ url +"</url><user>"+ username +"</user><password>" + password + "</password><action>" + action + "</action></Message>";
		 }
		 
	        list = (ExpandableListView) findViewById(R.id.ChannelListView);
	    	registerForContextMenu(list);
	    	
	        try
	        {
	        	HttpClient httpclient = new DefaultHttpClient();
	            HttpPost httppost = new HttpPost(clientListener);
	            
	            StringEntity se = new StringEntity(strMirth, HTTP.UTF_8);
	            
	            se.setContentType("application/xml");
	            httppost.setHeader("Content-Type","application/xml;charset=UTF-8");
	            httppost.setEntity(se); 
	            
	            BasicHttpResponse httpResponse = (BasicHttpResponse) httpclient.execute(httppost);
	            
		        if (httpResponse.getStatusLine().getStatusCode() == 200)
		        {
		        	txtStatusMessage.setText("");
		        	txtErrMessage.setText("");
		        	
		            HttpEntity entity = httpResponse.getEntity();
		            String strXML = EntityUtils.toString(entity);
		            
		            Map<String, List<String>> tmpChannelMap = getMapFromChannelList( getChannelListFromXML(strXML) );
		            
		            if(tmpChannelMap != null && !tmpChannelMap.isEmpty())
		            {
		            	channelMap.clear();
			            groupList.clear();
			            
			            channelMap = tmpChannelMap;
			            
 			            groupList.addAll(channelMap.keySet());
			            
 			           metrics = new DisplayMetrics();
 				        getWindowManager().getDefaultDisplay().getMetrics(metrics);
 				        width = metrics.widthPixels;
 				        
 			           list.setIndicatorBounds(width - GetDipsFromPixel(50), width - GetDipsFromPixel(30));
 			           
			            if(list.getAdapter()==null)
			            {
			            	expListAdapter = new ExpandableListAdapter(
				                    this, groupList, channelMap);
			            	list.setAdapter(expListAdapter);
			            }
		            	else
		            	{	
		            		expListAdapter.setChannelMap(channelMap);
		            		expListAdapter.setChannelDetail(groupList);
		            		expListAdapter.notifyDataSetChanged();
		            	}
		            }
	            	nextAction = Action.DASHBOARD;
	            	nextActionOnChannel = null;
		        }
		        else
		        {
		        	txtErrMessage.setText(httpResponse.getStatusLine().toString());
		        	txtErrMessage.setFocusable(true);
		        }
		        
	        }
	        catch(Exception ex)
	        {
	        	txtErrMessage.setText(ex.getMessage());
	    		txtErrMessage.setFocusable(true);
	        } 
	        
	        doTheAutoRefresh();
	 }
	 
	 public int GetDipsFromPixel(float pixels)
	    {
	     // Get the screen's density scale
	     final float scale = getResources().getDisplayMetrics().density;
	     // Convert the dps to pixels, based on density scale
	     return (int) (pixels * scale + 0.5f);
	    }
	 
	 @Override
	 public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		 super.onCreateContextMenu(menu, v, menuInfo);
	      if (v.getId()==R.id.ChannelListView) {
//	          MenuInflater inflater = getMenuInflater();
//	          inflater.inflate(R.menu.channel_menu, menu);
	          menu.setHeaderTitle("Select Action");
	          
	          // Get the list item position    
	          ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo)menuInfo;

	          // Now you can do whatever.. (Example, load different menus for different items)
	          String groupKey = (String) expListAdapter.getGroup((int)info.id);
	          String[] channelNameWithStatusArray = groupKey.split(":");
	          
	          if(channelNameWithStatusArray.length == 3)
	          {
		 	     String channelStatus = channelNameWithStatusArray[2];
		 	      
		 	     if(channelStatus.startsWith("STA"))
		         {
		 	    	menu.add(0, R.id.menu_stop , 0, R.string.menu_stop);
		 	    	menu.add(0, R.id.menu_pause , 0, R.string.menu_pause);
		         }
		         else if(channelStatus.startsWith("STO"))
		         {
		        	 menu.add(0, R.id.menu_start , 0, R.string.menu_start);
		         }
		         else if(channelStatus.startsWith("PAU"))
		         {
		        	 menu.add(0, R.id.menu_resume , 0, R.string.menu_resume);
			 	     menu.add(0, R.id.menu_stop , 0, R.string.menu_stop);
		         }
		 	     
	          }
	      }
	 }
	 
	 
	 @Override
	 public boolean onContextItemSelected(MenuItem item) {
	       ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item.getMenuInfo();
	       String groupKey = null;
	       switch(item.getItemId()) {
	          case R.id.menu_start:
	        	  groupKey = (String) expListAdapter.getGroup((int)info.id);
	        	  sendStartRequest(getChannelIdForContextMenu(groupKey));
	        	  return true;
	           case R.id.menu_stop:
	        	   groupKey = (String) expListAdapter.getGroup((int)info.id);
	        	   sendStopRequest(getChannelIdForContextMenu(groupKey));
	                 return true;
	           case R.id.menu_pause:
	        	   groupKey = (String) expListAdapter.getGroup((int)info.id);
	        	   sendPauseRequest(getChannelIdForContextMenu(groupKey));
	                 return true;
	           case R.id.menu_resume:
	        	   groupKey = (String) expListAdapter.getGroup((int)info.id);
	        	   sendResumeRequest(getChannelIdForContextMenu(groupKey));
	                 return true;
	           default:
	                 return super.onContextItemSelected(item);
	       }
	 }
	 
	 private String getChannelIdForContextMenu(String groupDetail)
	 {
		 String[] channelNameWithStatusArray = groupDetail.split(":");
	     return channelNameWithStatusArray[0];
	 }
	 
	 private void sendStartRequest(String channelId)
	 {
		 nextAction = Action.START;
		 nextActionOnChannel = channelId;
	 }
	 
	 private void sendStopRequest(String channelId)
	 {
		 nextAction = Action.STOP;
		 nextActionOnChannel = channelId;
	 }
	 
	 private void sendPauseRequest(String channelId)
	 {
		 nextAction = Action.PAUSE;
		 nextActionOnChannel = channelId;
	 }
	 
	 private void sendResumeRequest(String channelId)
	 {
		 nextAction = Action.RESUME;
		 nextActionOnChannel = channelId;
	 }
	 
	 private Map<String, List<String>> getMapFromChannelList(ArrayList<ChannelItem> channels)
	 {
		 Map<String, List<String>> channelMap = new LinkedHashMap<String, List<String>>();
		 
		 for(ChannelItem ci : channels)
		 {
			 List<String> channelDetail = new ArrayList<String>();
			 
			 String key = ci.getChannelId() + ":" + ci.getChannelName() + ":" + ci.getChannelStatus();
			 
			 channelDetail.add("Revision" + ":" + ci.getRevision());
			 
			 channelDetail.add("Deployed" + ":" + ci.getLastDeployed());
			 channelDetail.add("Received" + ":" + ci.getReceived());
			 channelDetail.add("Sent" + ":" + ci.getSent());
			 channelDetail.add("Errored" + ":" + ci.getErrored());
			 channelDetail.add("Queued" + ":" + ci.getQueued());
			 channelDetail.add("Filtered" + ":" + ci.getFiltered());
			 channelDetail.add("Alerted" + ":" + ci.getAlerted());
			 
			 channelMap.put(key, channelDetail);
		 }
		 
		 return channelMap;
	 }
	 
	 private ArrayList<ChannelItem> getChannelListFromXML(String strXML) throws Exception
	 {
		 Document doc = getDomElement(strXML); // getting DOM element
         
		 NodeList nlResult = doc.getElementsByTagName("result");
		 NodeList nlErr = doc.getElementsByTagName("err");

		 String strResult = getElementValue(nlResult.item(0));
		 String strErr = getElementValue(nlErr.item(0));
		 if(strResult == "0")
		 {
			 throw new Exception(strErr);
		 }
			 
         NodeList nl = doc.getElementsByTagName("channel");
         ArrayList<ChannelItem> channels = new ArrayList<ChannelItem>();
         
         // looping through all item nodes <item>
         for (int i = 0; i < nl.getLength(); i++) {
             // creating new HashMap
            ChannelItem channel = new ChannelItem();
            
             Element e = (Element) nl.item(i);
             
             channel.setChannelId(getValue(e, "channelId"));
             channel.setChannelName(getValue(e, "channelName"));
             channel.setChannelStatus(getValue(e, "channelStatus"));
             
             channel.setRevision(Integer.parseInt( getValue(e, "revision") ));
             
             channel.setLastDeployed(getValue(e, "lastDeployed"));
             channel.setReceived(Integer.parseInt(getValue(e, "received")));
             channel.setFiltered(Integer.parseInt(getValue(e, "filtered")));
             channel.setQueued(Integer.parseInt(getValue(e, "queued")));
             channel.setSent(Integer.parseInt(getValue(e, "sent")));
             
             channel.setErrored(Integer.parseInt(getValue(e, "errored")));
             channel.setAltered(Integer.parseInt(getValue(e, "altered")));
             channel.setConnection(getValue(e, "connection"));
             
             channels.add(channel);
         }
         
         return channels;
	 }
	 
	 private Document getDomElement(String xml){
	        Document doc = null;
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        try {
	 
	            DocumentBuilder db = dbf.newDocumentBuilder();
	 
	            InputSource is = new InputSource();
	                is.setCharacterStream(new StringReader(xml));
	                doc = db.parse(is); 
	 
	            } catch (ParserConfigurationException e) {
	            	txtErrMessage.setText(e.getMessage());
	        		txtErrMessage.setFocusable(true);
	                return null;
	            } catch (SAXException e) {
	            	txtErrMessage.setText(e.getMessage());
	        		txtErrMessage.setFocusable(true);
	        		return null;
	            } catch (IOException e) {
	            	txtErrMessage.setText(e.getMessage());
	        		txtErrMessage.setFocusable(true);
	        		return null;
	            }
	                // return DOM
	            return doc;
	    }
	 
	 private String getValue(Element item, String str) {      
		    NodeList n = item.getElementsByTagName(str);        
		    return this.getElementValue(n.item(0));
		}
	 
	 private final String getElementValue( Node elem ) {
         Node child;
         if( elem != null){
             if (elem.hasChildNodes()){
                 for( child = elem.getFirstChild(); child != null; child = child.getNextSibling() ){
                     if( child.getNodeType() == Node.TEXT_NODE  ){
                         return child.getNodeValue();
                     }
                 }
             }
         }
         return "";
  } 
}
