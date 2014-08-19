package com.example.homework;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.base.BaseHttp;
import com.example.tools.ViewTools;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MainActivity extends Activity {

	private JSONObject myJsonData;
	private JSONArray rowData = null;
	private String dataUrl = "http://thoughtworks-ios.herokuapp.com/facts.json";
	private int refreshmenuID = 1;
	private Dialog dialog;
	private ListView listView;
	private MyListAdapter adapter=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listView = (ListView) findViewById(R.id.listview);
		getData();
	}

	public void getData() {
		BaseHttp client = new BaseHttp(MainActivity.this);
		dialog = ViewTools.showLoading(MainActivity.this, getResources()
				.getString(R.string.dialog_title),
				getResources().getString(R.string.dialog_message));
		client.get(dataUrl, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String content) {
				try {
					myJsonData = new JSONObject(content);
					rowData = myJsonData.getJSONArray("rows");
					System.out.println(content);
					bindData();
					dialog.dismiss();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void bindData() {
		try {
			getActionBar().setTitle(myJsonData.getString("title"));
			if(adapter==null){
				adapter =new MyListAdapter();
				listView.setAdapter(adapter);
			}else{
				adapter.notifyDataSetChanged();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private class MyListAdapter extends BaseAdapter {
		private class ViewHolder {
			private TextView tvTitle;
			private TextView tvDescription;
			private ImageView ivPic;
		}
		@Override
		public int getCount() {
			if(rowData==null){
				return 0;
			}
			return rowData.length();
		}

		@Override
		public Object getItem(int position) {
			if (rowData != null && rowData.length() > 0) {
				try {
					return rowData.get(position);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			if (rowData != null && rowData.length() > 0) {
				return position;
			}
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup group) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater mInflater = (LayoutInflater) MainActivity.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = mInflater.inflate(R.layout.item_manlist, null,
						false);
				holder.tvTitle = (TextView) convertView
						.findViewById(R.id.tvTitle);
				holder.tvDescription = (TextView) convertView
						.findViewById(R.id.tvDescription);
				holder.ivPic = (ImageView) convertView
						.findViewById(R.id.ivPic);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			try {
				JSONObject itemdata =rowData.getJSONObject(position); 
				holder.tvTitle.setText(itemdata.getString("title"));
				holder.tvDescription.setText(itemdata.getString("description"));
				ImageLoader.getInstance().displayImage(
						itemdata.getString("imageHref"),
						holder.ivPic);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return convertView;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuItem refresh = menu.add(0, refreshmenuID, 0, "刷新");
		refresh.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == refreshmenuID) {
			getData();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
