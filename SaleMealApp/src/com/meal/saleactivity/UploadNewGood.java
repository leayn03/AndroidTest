package com.meal.saleactivity;

import java.io.File;
import java.io.FileNotFoundException;

import com.example.salemealapp.R;
import com.meal.action.MenuConfigAction;
import com.meal.activity.BaseActivity;
import com.meal.activity.ipml.AsynThreadImpl;
import com.meal.activity.ipml.UIThreadImpl;
import com.meal.bean.Global;
import com.meal.bean.Menu;
import com.meal.bean.Seller;
import com.meal.dialog.MyProgressDialog;
import com.meal.saleglobal.SaleGlobal;
import com.meal.util.DialogUtil;
import com.meal.util.SysUtil;

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

public class UploadNewGood extends BaseActivity{

	private MenuConfigAction menuConfig = MenuConfigAction.getInstance();
	Menu menu=null;
	Seller seller = SaleGlobal.seller;
	ImageButton returnButton;
	Button scanPicture;
	ImageView uploadNewGoodPicture;
	private MyProgressDialog loginProgressDialog;   //progressbar 1
	private boolean flagP = false;
	
	EditText newGoodName;
	EditText newGoodPrice;
	EditText newGoodSale;
	
	Uri uri;
    String pictureUri = null;
    String tmp=null;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.uploadnewgood);
		
	   	loginProgressDialog = MyProgressDialog.createDialog(this);   //progressbar 2
         returnButton=(ImageButton)findViewById(R.id.uploadNewGoodReturnButton);
		 scanPicture=(Button)findViewById(R.id.openGallery);
		
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

            }
        });
 		
 		
		//监听返回键
        returnButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(UploadNewGood.this,SaleInfoActivity.class);
				startActivity(intent);
			}
		});
	
		addEventListener(); 
	}
	
	
	private void addEventListener(){
    
	    addClickEventListener(R.id.saveNewGood, new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
		       initial();
		       loginProgressDialog.show();     //progressbar 3
		       startAsynThread("uploadnewgood");	  
			}
			
		});
		
	}
	
private void initial()
{
	setUIRefreshConfig(new UIThreadImpl() {
		
		@Override
		public void refresh(Message msg) {
			// TODO Auto-generated method stub
			
			if(0==msg.arg2)
			{
				Toast.makeText(UploadNewGood.this, "上传成功，可继续添加！",
						 Toast.LENGTH_LONG).show();
			}
			if(1==msg.arg2)
			{
	    		   Toast.makeText(UploadNewGood.this, "折扣价格必须在0--1 之间，请重新输入！",
							 Toast.LENGTH_LONG).show();
			}
			if(2==msg.arg2)
			{
		    	Toast.makeText(UploadNewGood.this, "输入有误，请重新输入！温馨提示：小数点后只能输入两位",
						 Toast.LENGTH_LONG).show();
			}

			//清空editText
			newGoodName=(EditText)findViewById(R.id.inputTheGoodName); //新商品名称
		    newGoodPrice=(EditText)findViewById(R.id.inputTheGoodPrice); //新商品价格
		    newGoodSale=(EditText)findViewById(R.id.inputTheGoodSale); //新商品名称
		    uploadNewGoodPicture=(ImageView)findViewById(R.id.uploadNewGoodPicture);
		    
		    
		    newGoodName.setText("");
		    newGoodPrice.setText("");
		    newGoodSale.setText("");
		    uploadNewGoodPicture.setImageResource(R.drawable.defaultgood);
		    
     	    loginProgressDialog.dismiss();  //progressbar 4
			}

	});
	
	setAsynThreadConfig("uploadnewgood", true, new AsynThreadImpl() {

		@Override
		public Message excute() {
			// TODO Auto-generated method stub
			
		   Message msg=Message.obtain();
		   
			newGoodName=(EditText)findViewById(R.id.inputTheGoodName); //新商品名称
		    newGoodPrice=(EditText)findViewById(R.id.inputTheGoodPrice); //新商品价格
		    newGoodSale=(EditText)findViewById(R.id.inputTheGoodSale); //新商品名称
		    
			String newGoodNameString=newGoodName.getText().toString(); 
			String string1=newGoodPrice.getText().toString();
            String string2=newGoodSale.getText().toString();
		    Double  newGoodPriceDouble = 0.0;
		    Double  newGoodSaleDouble = 0.0;


	
			if(flagP)
			{
				String path="";
				String fileName="";
				if(null !=uri)
				{
					String realPath=getRealPath(uri);
					File file= new File(realPath);
					
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
						tmp = menuConfig.uploadMenuPhoto(path, fileName);	
						
					}
				}
			}
		    
		    if(null != newGoodNameString && newGoodNameString.length() !=0 && isRightPrice(string1) && isRightPrice(string2))
		    {
			      newGoodPriceDouble=Double.valueOf(string1);//获取价格double型
			      newGoodSaleDouble=Double.valueOf(string2);//获取折扣double型
		    	   if(newGoodSaleDouble<=1 && newGoodSaleDouble>=0 && newGoodPriceDouble>=0) //折扣和价格均可为0
		    	   {
		   			menu= new Menu(newGoodNameString, tmp, newGoodPriceDouble, newGoodSaleDouble);
				    menu.setSid(seller.getSid());
				    msg.obj = menuConfig.addMenu(menu);
				   
				    msg.arg2=0;
				   
		    	   }
		    	   else
		    	   {
		    		   msg.arg2=1;

		    	   }
		    }
		    else
		    {
		    	msg.arg2=2;

		    }

			
			
			finishAsynThread("uploadnewgood");

			return msg;
		}

	});
	
}
@Override  
protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
    if (resultCode == RESULT_OK) {  
    	uri = data.getData();  
    	pictureUri=uri.toString();
        Log.e("uri", pictureUri);  
        ContentResolver cr = this.getContentResolver();  
        try {  
            Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));  
            ImageView imageView = (ImageView) findViewById(R.id.uploadNewGoodPicture);  
            /* 将Bitmap设定到ImageView */  
            imageView.setImageBitmap(bitmap);  
           flagP=true;
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

private boolean isRightPrice(String price)//数字正确输入
{
	boolean isRight = false;
	int count=0;
	int countDot=0;

	if(price == null || price.equals("")) //输入不能为空
	{
		return isRight;
	}

	for(int i=0 ; i<price.length() ; i++)
	{
			char c = price.charAt(i);
			if((c>='0' && c<='9') || c=='.')  //或者
		{
	
		}
		else
		{
			return isRight;
		}
	}
	
	for(int i=0 ; i<price.length() ; i++)
	{
		char c = price.charAt(i);

		if(c=='.')
		{
			count=count+1;
		}
	}
	for(int i=0 ; i<price.length() ; i++)
	{
		char c = price.charAt(i);
		if(c=='.')
		{
			countDot=price.length()-i-1;
		}
	}
	
	if(count>1 || countDot>2)
	{
		return isRight;
	}

	isRight = true;
	return isRight;
}


}
