package com.rsupport.android.push.service;

import java.util.ArrayList;
import java.util.StringTokenizer;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PushRegister {
	private final String PREF_PUSH_TOPIC_LIST = "pref_push_topic_list";
	private final String KEY_PUSH_TOPIC_LIST = "key_push_topic_list";
	private final String SEPARATOR = ";";
	private Context context = null;
	public PushRegister(Context context){
		this.context = context;
	}
	
	public void register(String topic){
		SharedPreferences pref = context.getSharedPreferences(PREF_PUSH_TOPIC_LIST, Context.MODE_PRIVATE);
		Editor e = pref.edit();
		
		StringBuffer topicBuffer = new StringBuffer(pref.getString(KEY_PUSH_TOPIC_LIST, ""));
		if(contain(topicBuffer.toString(), topic) == false){
			topicBuffer.append(topic).append(SEPARATOR);
			e.putString(KEY_PUSH_TOPIC_LIST, topicBuffer.toString());
			e.commit();
		}
	}
	
	public void unRegist(String topic){
		SharedPreferences pref = context.getSharedPreferences(PREF_PUSH_TOPIC_LIST, Context.MODE_PRIVATE);
		String list = pref.getString(KEY_PUSH_TOPIC_LIST, "");
		if(contain(list, topic) == true){
			list.replace(topic + SEPARATOR, "");
			Editor e = pref.edit();
			e.putString(KEY_PUSH_TOPIC_LIST, list);
			e.commit();
		}
	}
	
	public String[] getRegistedList(){
		SharedPreferences pref = context.getSharedPreferences(PREF_PUSH_TOPIC_LIST, Context.MODE_PRIVATE);
		String list = pref.getString(KEY_PUSH_TOPIC_LIST, "");
		
		if(list.equals("") == false){
			ArrayList<String> arrayList = new ArrayList<String>();
			StringTokenizer st = new StringTokenizer(list, SEPARATOR);
			while(st.hasMoreElements()){
				String topic = st.nextToken();
				arrayList.add(topic);
			}
			return arrayList.toArray(new String[arrayList.size()]);
		}
		return null;
	}
	
	private boolean contain(String src, String topic){
		if(src.contains(topic + SEPARATOR)){
			return true;
		}
		return false;
	}

	public void unRegistAll() {
		SharedPreferences pref = context.getSharedPreferences(PREF_PUSH_TOPIC_LIST, Context.MODE_PRIVATE);
		Editor e = pref.edit();
		e.clear();
		e.commit();
	}
}
