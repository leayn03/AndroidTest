package com.meal.saleactivity;

import java.io.File;
import java.io.FileNotFoundException;

import com.example.salemealapp.R;
import com.meal.action.SellerManageAction;
import com.meal.activity.BaseActivity;
import com.meal.activity.ipml.AsynThreadImpl;
import com.meal.activity.ipml.UIThreadImpl;
import com.meal.bean.Seller;
import com.meal.dialog.MyProgressDialog;
import com.meal.saleglobal.SaleGlobal;
import com.meal.util.PhotoUtil;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class UpdateSalePicture extends BaseActivity{
	
	Button scanPicture;
	Button returnSavePicture;
	ImageButton updateSalePictureReturnButton;
    private SellerManageAction sellerManage=SellerManageAction.getInstance(); 
    Seller seller=SaleGlobal.seller; 
    String pictureUri=null;
    Uri uri;
	private boolean flagP = false;
	private boolean result = false;
	
	private MyProgressDialog loginProgressDialog;   //progressbar 1

  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.updatesalepicture);
		
		loginProgressDialog = MyProgressDialog.createDialog(this);   //progressbar 2
		
		returnSavePicture=(Button)findViewById(R.id.saveUpdatePictureButton);
		updateSalePictureReturnButton=(ImageButton)findViewById(R.id.updateSalePictureReturnButton);
		scanPicture=(Button)findViewById(R.id.scanPicture);
	
		scanPicture.setOnClickListener(new Button.OnClickListener(){  
            @Override  
            public void onClick(View v) {  
                Intent intent = new Intent();  
                /* 开启Pictures画面Type设定为image */  
                intent.setType("image/*");  
                /* 使用Intent.ACTION_GET_CONTENT这个Action */  
                intent.setAction(Intent.ACTION_GET_CONTENT);   
                /* 取得相片后返回本画面 */  
                startActivityForResult(intent, 1);  
                returnSavePicture.setVisibility(View.VISIBLE);

            }
        });
		
		updateSalePictureReturnButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				UpdateSalePicture.this.finish();
				
			}
		});
		
		returnSavePicture.setOnClickListener(new OnClickListener() {	
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				initial();
				String logoPicture= pictureUri; 
				if( null != logoPicture)
				{					
					Log.i("hello", "what");
					loginProgressDialog.show();     //progressbar 3
					startAsynThread("updatesellerpicture");
				}
				else 
				{
					Toast toast=Toast.makeText(UpdateSalePicture.this, "照片有问题，重新上传！",Toast.LENGTH_LONG);//输入有误，请重新输入
			    	toast.setGravity(Gravity.TOP , 0,180);
			    	toast.show();
				}
				
//				//直接跳转？？？
//				
//				Intent intent=new Intent();   
//				intent.setClass(UpdateSalePicture.this,UpdateSaleInfo.class);
//				startActivity(intent);
				
			}
		});
	}



	private void initial()
	   {
	  //  	loginProgressDialog.show();     //progressbar 3
		   setUIRefreshConfig(new UIThreadImpl(){

			@Override
			public void refresh(Message msg) {
				// TODO Auto-generated method stub
				
				//直接跳转？？？
				loginProgressDialog.dismiss();  //progressbar 4
				Intent intent=new Intent();   
				intent.setClass(UpdateSalePicture.this,UpdateSaleInfo.class);
				startActivity(intent);
				UpdateSalePicture.this.finish();
			}
			   
		   });
		   
		   setAsynThreadConfig("updatesellerpicture",true, new AsynThreadImpl()
		   {

			@Override
			public Message excute() {
				// TODO Auto-generated method stub


				String tmp = null;
				if(flagP)//选择了照片
				{
					String path="";
					String fileName="";
					if(null != uri)
					{
						String realPath=getRealPath(uri);
						File file=new File(realPath);
						
						if(file.exists())
						{
							String str[] = realPath.split("/");
							fileName = str[str.length-1];
							path = str[0];
							for(int i=1 ; i<str.length-1 ; i++)
							{
								path += ("/"+str[i]);
							}
							path += "/";
							Log.e("path", path);
							Log.e("fileName", fileName);
							tmp = sellerManage.uploadLogos(path, fileName);//传照片，获取照片uri
							seller.setLogo(tmp);
						    sellerManage.updateSellerInfo(seller);

						}
					}
				}
				Message msg=Message.obtain();	
				finishAsynThread("updatesellerpicture");

				return msg;
		    	}
			   
		   });
	   }
	   
//	    @Override  
//	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
//	        if (resultCode == RESULT_OK) {  
//	        	uri = data.getData();  
//	        	pictureUri=uri.toString();
//	            Log.e("uri", pictureUri);  
//	            ContentResolver cr = this.getContentResolver();  
//	            try {  
//	                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));  
//	                ImageView imageView = (ImageView) findViewById(R.id.updateSalePicturePreview);  
//	                /* 将Bitmap设定到ImageView */  
//	                imageView.setImageBitmap(bitmap);  
//	                flagP=true;
//	            } catch (FileNotFoundException e) {  
//	                Log.e("Exception", e.getMessage(),e);  
//	            }  
//	        }  
//	        super.onActivityResult(requestCode, resultCode, data);  
//	    } 

	    @Override  
	        protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	            if (resultCode == RESULT_OK) {  
	                uri = data.getData();  
	                Log.e("uri", uri.toString());  
	                pictureUri=uri.toString();
	                ContentResolver cr = this.getContentResolver();  
	                try {  
	                    Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri)); 
	                    String str = getRealPath(uri);
	                    if(bitmap == null )
	                    {
	                     Toast.makeText(UpdateSalePicture.this , getResources().getString(R.string.nonephoto), Toast.LENGTH_SHORT).show();
	                     return;
	                    }
	                    if(str == null)
	                    {
//	                     Toast.makeText(IndividualCenterActivity.this , getResources().getString(R.string.nonephoto), Toast.LENGTH_SHORT).show();
	                     Toast.makeText(UpdateSalePicture.this , "诡异的错误", Toast.LENGTH_SHORT).show();
	                     return;
	                    }
	                    if(str.lastIndexOf(".jpg")!=str.length()-4 && str.lastIndexOf(".jpeg")!=str.length()-4 && str.lastIndexOf(".png")!=str.length()-4)
	                    {
	                     Toast.makeText(UpdateSalePicture.this , getResources().getString(R.string.uploadjpg), Toast.LENGTH_SHORT).show();
	                     return;
	                    }
	                    int size = (bitmap.getRowBytes() * bitmap.getHeight());
	                    Log.e("store",size +"");
	                    if(size <= 1600000)
	                    {
	                   //  Bitmap tmp = PhotoUtil.toRoundCorner(bitmap, 180.0f);
	                     ImageView imageView = (ImageView) findViewById(R.id.updateSalePicturePreview);  
	                     imageView.setImageBitmap(bitmap);  
	                     flagP = true;
	                    }
	                    else
	                    {
	                     Toast.makeText(UpdateSalePicture.this , getResources().getString(R.string.failupdatebigfile), Toast.LENGTH_SHORT).show();
	                    }
	                } catch (FileNotFoundException e) {  
	                    Log.e("Exception", e.getMessage(),e);  
	                }  
	            }  
	            super.onActivityResult(requestCode, resultCode, data);  
	        }   
	    
		private String getRealPath(Uri fileUrl) { 
	        String fileName = null; 
	        Uri filePathUri = fileUrl; 
	        if (fileUrl != null) { 
	            if (fileUrl.getScheme().toString().compareTo("content") == 0) { 
	                // content://开头的uri 
	                Cursor cursor = getContentResolver().query(fileUrl, null, null, 
	                        null, null); 
	                if (cursor != null && cursor.moveToFirst()) { 
	                    int column_index = cursor.getColumnIndexOrThrow("_data"); 
	                    fileName = cursor.getString(column_index); // 取出文件路径 
	                    if (!fileName.startsWith("/mnt")) { 
	                   //     fileName = "/mnt" + fileName; 
	                    } 
	                    cursor.close(); 
	                } 
	            } else if (fileUrl.getScheme().compareTo("file") == 0) { 
	                // file:///开头的uri 
	                fileName = filePathUri.toString(); 
	                fileName = filePathUri.toString().replace("file://", ""); 
	                // 替换file:// 
	                if (!fileName.startsWith("/mnt")) { 
	                    // 加上"/mnt"头 
	                    fileName += "/mnt"; 
	                } 
	            } 
	        } 
	        return fileName; 
	    }

}
