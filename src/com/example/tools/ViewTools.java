package com.example.tools;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ViewTools {

	private static String OK="确定";
	private static String CANCEL="取消";
	private static String PROMPT ="提示信息";
	private static AlertDialog.Builder builders;
	public static AlertDialog alertDialog;
	/**
	 * 显示带标题的alert提示信息
	 * @param context
	 * @param title
	 * @param message
	 */
	public static void showMessage(Context context,String title,String message){
		builders = new AlertDialog.Builder(context);
		if(title!=null){
			builders.setTitle(title);
		}else{
			builders.setTitle(PROMPT);
		}
		builders.setMessage(message);
		builders.setPositiveButton(OK, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		alertDialog = builders.create();
		alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		hideAlertDialog();
		alertDialog.show();
	}
	
	/**
	 * 显示带标题的选择提示信息
	 * @param context
	 * @param title
	 * @param message
	 */
	public static void showSelect(Context context,String title,String[] items,OnClickListener listener){
		builders = new AlertDialog.Builder(context);
		if(title!=null){
			builders.setTitle(title);
		}else{
			builders.setTitle(PROMPT);
		}
		builders.setItems(items, listener);
		hideAlertDialog();
		alertDialog = builders.show();
	}
	/**
	 *  显示不带标题的alert提示信息
	 * @param context
	 * @param message
	 */
	public static void showMessage(Context context,String message){
		builders = new AlertDialog.Builder(context);
		builders.setTitle(PROMPT);
		builders.setMessage(message);
		builders.setPositiveButton(OK, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		hideAlertDialog();
		alertDialog = builders.show();
	}
	
	/**
	 * 显示带标题，确认按钮的alert提示信息
	 * @param context
	 * @param title
	 * @param message
	 * @param btnText
	 * @param listener
	 */
	public static void showMessage(Context context,String title,String message,String btnText,DialogInterface.OnClickListener listener){
		builders = new AlertDialog.Builder(context);
		if(title!=null){
			builders.setTitle(title);
		}else{
			builders.setTitle(PROMPT);
		}
		builders.setMessage(message);
		builders.setPositiveButton(btnText,listener);
		hideAlertDialog();
		alertDialog = builders.show();
	}
	
	/**
	 * 显示带标题，确认，取消按钮的alert提示信息
	 * @param context
	 * @param title
	 * @param message
	 * @param btnText
	 * @param listener
	 */
	public static void showConfirm(Context context,String title,String message,String okText,String cancelText,DialogInterface.OnClickListener okListener,DialogInterface.OnClickListener cancelListener){
		builders = new AlertDialog.Builder(context);
		if(title!=null){
			builders.setTitle(title);
		}else{
			builders.setTitle(PROMPT);
		}
		if(cancelText==null){
			cancelText = CANCEL;
		}
		builders.setMessage(message);
		builders.setPositiveButton(okText,okListener);
		builders.setNegativeButton(cancelText, cancelListener);
		hideAlertDialog();
		alertDialog = builders.show();
	}
	
	/**
	 * 获取一个分割线
	 */
	public static TextView getDivider(Context view , int height){
		TextView tvDivider =new TextView(view);
		LayoutParams paramsDivider = new LayoutParams(LayoutParams.FILL_PARENT,height);
		tvDivider.setLayoutParams(paramsDivider);
		tvDivider.setBackgroundResource(android.R.drawable.divider_horizontal_dark);
		return tvDivider;
	}
	/**
	 * 显示短时间的toast
	 * @param context
	 * @param message
	 */
	public static void showShortToast(Context context,String message){
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 显示长时间的toast
	 * @param context
	 * @param message
	 */
	public static void showLongToast(Context context,String message){
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}
	
	/**
	 * 显示正在加载提示信息
	 * @param context
	 * @param message
	 */
	public static ProgressDialog showLoading(Context context,String title,String message){
		return ProgressDialog.show(context, title, message,true,true);
	}
	
	/**
	 * 显示分享信息
	 * @param context
	 * @param message
	 */
	public static void showShare(Context context,String content){
		Intent intent =new Intent(Intent.ACTION_SEND);
		intent.setType("image/*");
		intent.putExtra(Intent.EXTRA_TEXT, content);
		intent.putExtra(Intent.EXTRA_SUBJECT, "故事梦分享");
		context.startActivity(intent);
	}
	
	/**
	 * 隐藏弹出的对话框
	 */
	public static void hideAlertDialog(){
		if(alertDialog!=null && alertDialog.isShowing()){
			alertDialog.dismiss();
			alertDialog=null;
		}
	}
	
	
	public static void setSpinnerBg(Spinner spinner,int width,int height){ 
		spinner.setBackgroundDrawable(ViewTools.getSpinnerBg(200,60));
		spinner.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_UP){
					v.setBackgroundDrawable(ViewTools.getSpinnerBg(200,60));
				}else{
//					v.setBackgroundResource(R.drawable.shape_sp_bg_selected);
				}
				return false;
			}
		});
	}
	/**
	 * 获取spinner的背景
	 * @return
	 */
	private static BitmapDrawable getSpinnerBg(int width,int height){
		Bitmap bitmap =Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
        //Shader.TileMode三种模式   
        //REPEAT:沿着渐变方向循环重复   
        //CLAMP:如果在预先定义的范围外画的话，就重复边界的颜色   
        //MIRROR:与REPEAT一样都是循环重复，但这个会对称重复   
		drawRect(canvas,width, height);
	    drawTriangle(canvas,width, height);
	    return new BitmapDrawable(bitmap);
	}
	private static void drawRect(Canvas canvas,int width,int height){
		Paint mPaint = new Paint();
		LinearGradient mRectShader=new LinearGradient(0,0,100,100,
                new int[]{Color.parseColor("#1383d5"),Color.parseColor("#0088ff"),},   
                null,Shader.TileMode.CLAMP);
		mPaint.setAntiAlias(true);  
		mPaint.setShader(mRectShader);
        mPaint.setStyle(Paint.Style.FILL);
        RectF rectF = new RectF(new Rect(0, 0,width,height));  
        canvas.drawRoundRect(rectF,10,10, mPaint); 
	}
	
	private static void drawTriangle(Canvas canvas,int width,int height){
		Paint mPaint = new Paint();
		LinearGradient mTriangleShader=new LinearGradient(width-40,10,width-10,height-10,
                new int[]{Color.WHITE,Color.parseColor("#0088ff")},   
                new float[]{0,90f},Shader.TileMode.CLAMP);
		mPaint.setShader(mTriangleShader);
 	    mPaint.setStyle(Paint.Style.STROKE);//设置空心
 	    mPaint.setStyle(Paint.Style.FILL);
	    Path path=new Path();
	    path.moveTo(width-40,10);
	    path.lineTo(width-10,10);
	    path.lineTo(width-25,height-10);
	    path.close();
	    canvas.drawPath(path,mPaint);
	}
	
	
}
