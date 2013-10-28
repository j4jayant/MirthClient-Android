package com.j4jayant.android.mirthclient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.example.mirthclient.R;
import com.j4jayant.android.mirthclient.utils.PropertiesHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {

	EditText txtUrl = null;
	EditText txtUserName = null;
	EditText txtPassword = null;
	EditText txtClientListener = null;
	TextView txtErrMessage = null;
	CheckBox chkSave = null;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        txtErrMessage = (TextView) findViewById(R.id.txtErrorMessage);
    	txtErrMessage.setText("");
    	
        txtUrl = (EditText) findViewById(R.id.txtUrl);
		txtUserName = (EditText) findViewById(R.id.txtUserName);
		txtPassword = (EditText) findViewById(R.id.txtPassword);
		txtClientListener = (EditText) findViewById(R.id.txtClientListener);
		
		chkSave = (CheckBox) findViewById(R.id.chkSave);
		chkSave.setEnabled(false);
		
		Map<String, String> propMap = null;
		try {
			propMap = PropertiesHandler.getProperties();
		} catch (IOException e) {
			//e.printStackTrace();
			txtErrMessage.setText("Unable to load properties, error: " + e.getMessage());
		}
		
		if(propMap != null && propMap.size() > 0)
		{
			txtUrl.setText(propMap.get(PropertiesHandler.url_prop_name));
			txtUserName.setText(propMap.get(PropertiesHandler.user_prop_name));
			txtPassword.setText(propMap.get(PropertiesHandler.password_prop_name));
			txtClientListener.setText(propMap.get(PropertiesHandler.ep_prop_name));
		}
		else
		{
			txtUrl.setText("https://192.168.1.121:8443");
			txtUserName.setText("admin");
			txtPassword.setText("admin");
			txtClientListener.setText("http://192.168.1.121:8011/mirthadmin/");
		}
		
		addTextChangedListener();
    }
	
	private void addTextChangedListener()
	{
		txtUrl.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				chkSave.setEnabled(true);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {			}
        	
        });
		
		txtUserName.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				chkSave.setEnabled(true);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {			}
        	
        });
		
		txtPassword.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				chkSave.setEnabled(true);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {			}
        	
        });
		
		txtClientListener.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				chkSave.setEnabled(true);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {			}
        	
        });
	}
	
	public void onClick_btnLogin(View V)
    {
		txtErrMessage.setText("");
    	
    	String url = txtUrl.getText().toString();
    	String username = txtUserName.getText().toString();
    	String password = txtPassword.getText().toString();
    	String clientListener = txtClientListener.getText().toString();
    	
    	if(chkSave.isChecked())
    	{
    		Map<String, String> propMap = new HashMap<String, String>();
    		
    		propMap.put(PropertiesHandler.url_prop_name, url);
    		propMap.put(PropertiesHandler.user_prop_name, username);
    		propMap.put(PropertiesHandler.password_prop_name, password);
    		propMap.put(PropertiesHandler.ep_prop_name, clientListener);
    		
    		try {
				PropertiesHandler.setProperties(propMap);
			} catch (IOException e) {
				//e.printStackTrace();
				txtErrMessage.setText("Unable to save properties, error: " + e.getMessage());
			}
    	}
    	
    	try
    	{
				
				Intent intent = new Intent(this, DashboardActivity.class);
				
				intent.putExtra("url", url);
	        	intent.putExtra("username", username);
	        	intent.putExtra("password", password);
	        	intent.putExtra("clientListener", clientListener);
	        	
	        	startActivity(intent);
				
    	}
    	catch(Exception ex)
    	{
    		txtErrMessage.setText(ex.getMessage());
    		txtErrMessage.setFocusable(true);
    	}
    }
    
}
