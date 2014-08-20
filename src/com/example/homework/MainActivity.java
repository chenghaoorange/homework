package com.example.homework;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.example.base.BaseApplication;
import com.example.base.BaseHttp;
import com.example.tools.ViewTools;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class MainActivity extends Activity {

	private JSONObject myJsonData;
	private JSONArray rowData = null;
	private String dataUrl = "http://thoughtworks-ios.herokuapp.com/facts.json";
	private int refreshmenuID = 1;
	private Dialog dialog;
	private ListView listView;
	private MyListAdapter adapter = null;

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

			@Override
			@Deprecated
			public void onFailure(Throwable error, String content) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				ViewTools.showLongToast(MainActivity.this, "internet error");
			}
		});
	}

	public void bindData() {
		try {
			getActionBar().setTitle(myJsonData.getString("title"));
			if (adapter == null) {
				adapter = new MyListAdapter();
				listView.setAdapter(adapter);
			} else {
				adapter.notifyDataSetChanged();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private class ViewHolder {
		private RelativeLayout rlItemLayout;
		private TextView tvTitle;
		private TextView tvDescription;
		private ImageView ivPic;
	}

	private class MyListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (rowData == null) {
				return 0;
			}
			return rowData.length();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			View view = convertView;
			if (convertView == null) {
				holder = new ViewHolder();
				view = getLayoutInflater().inflate(R.layout.item_manlist,
						parent, false);
				holder.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
				holder.tvDescription = (TextView) view
						.findViewById(R.id.tvDescription);
				holder.ivPic = (ImageView) view.findViewById(R.id.ivPic);
				holder.rlItemLayout = (RelativeLayout) view
						.findViewById(R.id.rlItemLayout);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			try {
				JSONObject itemdata = rowData.getJSONObject(position);
				holder.tvTitle.setText(itemdata.getString("title"));
				holder.tvDescription.setText(itemdata.getString("description"));
				ImageLoader.getInstance().displayImage(
						itemdata.getString("imageHref"), holder.ivPic,
						BaseApplication.displayImageOptions,
						new ImageLoadingListener() {

							@Override
							public void onLoadingStarted(String arg0, View arg1) {
								// TODO Auto-generated method stub
							}

							@Override
							public void onLoadingFailed(String arg0, View arg1,
									FailReason arg2) {
								// TODO Auto-generated method stub
							}

							@Override
							public void onLoadingComplete(String arg0,
									View arg1, Bitmap bitmap) {
								 DisplayMetrics dm = new DisplayMetrics();
								 getWindowManager().getDefaultDisplay()
								 .getMetrics(dm);
								 holder.tvDescription.setMaxWidth(dm.widthPixels
								 - bitmap.getWidth()
								 - (int) getResources().getDimension(
								 R.dimen.listmargin));
							}

							@Override
							public void onLoadingCancelled(String arg0,
									View arg1) {
								// TODO Auto-generated method stub
							}
						});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return view;
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
