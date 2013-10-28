package com.j4jayant.android.mirthclient.utils;

import java.util.List;
import java.util.Map;

import com.example.mirthclient.R;
 
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Activity context;
    private Map<String, List<String>> channelMap;
    private List<String> channelDetail;
 
    public Map<String, List<String>> getChannelMap() {
		return channelMap;
	}

	public void setChannelMap(Map<String, List<String>> channelMap) {
		this.channelMap = channelMap;
	}

	public List<String> getChannelDetail() {
		return channelDetail;
	}

	public void setChannelDetail(List<String> channelDetail) {
		this.channelDetail = channelDetail;
	}

	public ExpandableListAdapter(Activity context, List<String> channelDetail, Map<String, List<String>> channelMap) {
        this.context = context;
        this.channelMap = channelMap;
        this.channelDetail = channelDetail;
    }
       
 
    public Object getChild(int groupPosition, int childPosition) {
    	return this.channelMap.get(channelDetail.get(groupPosition)).get(childPosition);
    }
 
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
        
	public View getChildView(final int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
    	final String field = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();
 
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, null);
        }
 
        String[] channelDetail = field.split(":");
        String fieldName = channelDetail[0];
        String fieldValue = channelDetail[1];
        
        TextView itemFieldPosition = (TextView) convertView.findViewById(R.id.lblDetail);
        itemFieldPosition.setText(fieldName);
        
        TextView itemFieldValue = (TextView) convertView.findViewById(R.id.txtDetail);
        itemFieldValue.setText(fieldValue);
        
        return convertView;
	}
    
    public int getChildrenCount(int groupPosition) {
        return channelMap.get(channelDetail.get(groupPosition)).size();
    }
 
    public Object getGroup(int groupPosition) {
        return channelDetail.get(groupPosition);
    }
 
    public int getGroupCount() {
        return channelDetail.size();
    }
 
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        String channelNameWithStatus = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_item,
                    null);
        }
        
        String[] channelNameWithStatusArray = channelNameWithStatus.split(":");
        String channelId = channelNameWithStatusArray[0];
        String channelName = channelNameWithStatusArray[1];
        String channelStatus = channelNameWithStatusArray[2];
        
        TextView itemChannelId = (TextView) convertView.findViewById(R.id.txtChannelId);
        itemChannelId.setTypeface(null, Typeface.BOLD);
        itemChannelId.setText(channelId);
        
        TextView itemChannelName = (TextView) convertView.findViewById(R.id.txtChannelName);
        itemChannelName.setTypeface(null, Typeface.BOLD);
        itemChannelName.setText(channelName);
        
        ImageView itemChannelStatus = (ImageView) convertView.findViewById(R.id.imgChannelStatus);
        
        if(channelStatus.startsWith("STA"))
        {
        	itemChannelStatus.setImageResource(R.drawable.green);
        }
        else if(channelStatus.startsWith("STO"))
        {
        	itemChannelStatus.setImageResource(R.drawable.red);
        }
        else if(channelStatus.startsWith("PAU"))
        {
        	itemChannelStatus.setImageResource(R.drawable.yellow);
        }
        
        return convertView;
    }
 
    public boolean hasStableIds() {
        return true;
    }
 
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    
}
