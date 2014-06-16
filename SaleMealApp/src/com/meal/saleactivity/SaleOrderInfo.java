package com.meal.saleactivity;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.salemealapp.R;
import com.meal.action.DealManageAction;
import com.meal.action.GrouponManageAction;
import com.meal.action.MenuConfigAction;
import com.meal.action.SellerManageAction;
import com.meal.activity.BaseActivity;
import com.meal.activity.ipml.AsynThreadImpl;
import com.meal.activity.ipml.UIThreadImpl;
import com.meal.bean.Groupon;
import com.meal.bean.Menu;
import com.meal.bean.Order;
import com.meal.bean.Seller;
import com.meal.dialog.MyProgressDialog;
import com.meal.saleglobal.SaleGlobal;
import com.meal.util.DialogUtil;

public class SaleOrderInfo extends BaseActivity{
	
	ListView detailListView;
	Button acceptSaleButton;
	Button returnSaleButton;
	ImageButton detailReturnButton;
	
	private ArrayList<Long> orderDetailArray;
	List<Map<String, Object>> orderDetailList = new ArrayList<Map<String, Object>>();
	List<Map<String, Object>> orderDetailList1 = new ArrayList<Map<String, Object>>();

	Map<String, Object> orderDetailMap = new HashMap<String, Object>();
	
	private DealManageAction dealManage=DealManageAction.getInstance();
	private MenuConfigAction menuManage=MenuConfigAction.getInstance();
	private GrouponManageAction grouponManage=GrouponManageAction.getInstance();
	
	private MyProgressDialog loginProgressDialog;   //progressbar 1
	
    Seller seller=SaleGlobal.seller;
    Menu menu;
    Groupon groupSelect ;
    Long gid;
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.orderdetail);
		loginProgressDialog = MyProgressDialog.createDialog(this);   //progressbar 2
		
		Intent intent=this.getIntent();

		Bundle bundle=intent.getExtras();
		groupSelect=(Groupon) bundle.getSerializable("group");//获取SaleOrderList传的Object
		gid=groupSelect.getGid();
		
		detailListView=(ListView)findViewById(R.id.orderList);
		
		 initOrderDetailList();

		acceptOrderEventListener(); //开启线程监听接受订单按键，并把订单状态设置为success
		refuseOrderEventListener();//开启线程监听接受订单按键，并把订单状态设置为refuse

		detailReturnButton=(ImageButton)findViewById(R.id.detailReturnButton);
		detailReturnButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
//				Intent intent=new Intent();
//				intent.setClass(SaleOrderInfo.this,SaleOrderList.class);
//				startActivity(intent);
				SaleOrderInfo.this.finish();
			}
		});

	}
	
	private void refuseOrderEventListener(){
		   
	    addClickEventListener(R.id.returnSaleButton, new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				 initialRefuseOrder();
				 startAsynThread("refuseOrderState");
			}
		});	
	}
	
	private void initialRefuseOrder()
	{
		setUIRefreshConfig(new UIThreadImpl() {
			
			@Override
			public void refresh(Message msg) {
				// TODO Auto-generated method stub
				Toast.makeText(SaleOrderInfo.this, "货品缺失，查看其它订单！", Toast.LENGTH_LONG).show();
				Intent intent=new Intent();
				intent.setClass(SaleOrderInfo.this,SaleOrderList.class);
				startActivity(intent);
				
//				SaleOrderInfo.this.finish();
			}
		});
		
		setAsynThreadConfig("refuseOrderState", true, new AsynThreadImpl() {

			@Override
			public Message excute() {
				// TODO Auto-generated method stub
				Message msg = Message.obtain();
				


    			grouponManage.setGrouponStatus(gid,"refuse");

				finishAsynThread("refuseOrderState");

				return msg;
			}

		});
		
	}	
	
	
	private void acceptOrderEventListener(){
		   
	    addClickEventListener(R.id.acceptSaleButton, new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				 initialAcceptOrder();
				 startAsynThread("acceptOrderState");
			}
		});	
	}
	
	private void initialAcceptOrder()
	{
		setUIRefreshConfig(new UIThreadImpl() {
			
			@Override
			public void refresh(Message msg) {
				// TODO Auto-generated method stub
				
				Toast.makeText(SaleOrderInfo.this, "接受订单成功，查看其它订单！", Toast.LENGTH_LONG).show();
				Intent intent=new Intent();
				intent.setClass(SaleOrderInfo.this,SaleOrderList.class);
				startActivity(intent);
				
//				SaleOrderInfo.this.finish();
			}
		});
		
		setAsynThreadConfig("acceptOrderState", true, new AsynThreadImpl() {

			@Override
			public Message excute() {
				// TODO Auto-generated method stub
				Message msg = Message.obtain();
				


    			grouponManage.setGrouponStatus(gid,"success");

				finishAsynThread("acceptOrderState");

				return msg;
			}

		});
		
	}	
	
	
	private void initOrderDetailList()
	{
		loginProgressDialog.show();           //progressbar3
		if(orderDetailList==null)
		{
			orderDetailList=new ArrayList<Map<String, Object>>();
		}
		else 
		{
			orderDetailList.clear();
		}
		
		setUIRefreshConfig(new UIThreadImpl() {
			
			@Override
			public void refresh(Message msg) {
				// TODO Auto-generated method stub
				
				detailListView.setAdapter(new MyAdapter(SaleOrderInfo.this,R.layout.sale_user_detail_row, orderDetailList, 
						new String[]{"saleUserName",  "saeUserTotal"}, new int[]{R.id.goodSaleUserOid, R.id.goodSaleUserTotal}));	
				loginProgressDialog.dismiss();  //progressbar 4
			}
		});
		
		setAsynThreadConfig("getOrderDetailList", true, new AsynThreadImpl() {
			
			@Override
			public Message excute() {
				// TODO Auto-generated method stub
				
				Message msg=Message.obtain();
				
				orderDetailArray=groupSelect.getOrderList();
				
				if(null != orderDetailArray)
				{
					for(int i=0;i<orderDetailArray.size();i++)
					{
						orderDetailMap=new HashMap<String, Object>();
						long tempId=orderDetailArray.get(i);
						if(0 != tempId)
						{
							Order tempOrder = dealManage.getOrderDetail(tempId);
							if (tempOrder != null ) {
								//商品名称列表
								
								HashMap<Long, Integer> mapMenu=new HashMap<Long, Integer>();  //menuList的hashMap
								ArrayList<Long> menuList=tempOrder.getMenuList();
								Long key=0L;
								ArrayList<String> menuResult=new ArrayList<String>();
								
								int length=menuList.size();
								for(int j=0; j< length; j++)
								{
									Long menuNum=menuList.get(j);
									for(Iterator inter=mapMenu.entrySet().iterator(); inter.hasNext();)
							        {
							        	Map.Entry element=(Map.Entry)inter.next();
							        	Object strKey=element.getKey();
							            key=Long.valueOf(String.valueOf(strKey));	    
							            if(key==menuNum)
							            {
							            	break;
							            }
							        }
							    	if(!menuNum.equals(key))
						        	{
						        		mapMenu.put(menuNum, 1);
						        	}
						        	else
						        	{
						        		mapMenu.put(menuNum, mapMenu.get(menuNum)+1); //value
						        	}         
								  }
											
								
								 for(Iterator iter = mapMenu.entrySet().iterator();iter.hasNext();){  //  把mapMenu写入list，然后放入orderDetailMap里显示
							            Map.Entry element = (Map.Entry)iter.next(); 
							            Object strKey = element.getKey();
							            Object strObj = element.getValue();	
							            Long numName=Long.valueOf(String.valueOf(strKey));
							            menu=menuManage.getMenuDetail(numName);
							            String stringName=menu.getName();
							            menuResult.add(stringName+"*"+String.valueOf(strObj));	
								}
								

								
								orderDetailMap.put("saleUserName", "用户订单号:    "+tempOrder.getOid()+ "\n商品列表:\n"+menuResult);
								orderDetailMap.put("saeUserTotal", "用户订单详情:    "+tempOrder.getAddress()+"  "+tempOrder.getPhone()+" \n订单时间:  "+tempOrder.getTime());
								orderDetailList.add(orderDetailMap);
					    	}				
						}
						
					}
				}

				finishAsynThread("getOrderDetailList");
				
				return msg;
			}
		});
		startAsynThread("getOrderDetailList"); 
	}
	
	public class ViewHolder {
		TextView text_itemName;
		TextView text_itemTotal;
	}
	public class MyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
		private int resId;
		private List<Map<String, Object>> listData;
		private String[] from;
		private int[] to;
		private OnClickListener clickListener;

		public MyAdapter(Context context, int resId,
				List<Map<String, Object>> listData, String[] from, int[] to) {

			mInflater = LayoutInflater.from(context);
			this.resId = resId;
			this.listData = listData;
			this.from = from;
			this.to = to;

		}

		// @Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listData.size();
		}

		// @Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return listData.get(position);
		}

		// @Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public void setClickListener(OnClickListener clickListener) {
			this.clickListener = clickListener;

		}

		// @Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			Map<String, Object> viewItem = listData.get(position);

			ViewHolder holder = null;
			Log.i("test", "test");
			if (convertView == null) {

				convertView = mInflater.inflate(resId, null);
				// convertView = mInflater.inflate(R.layout.orderlist_row,
				// null); //载入orderlist_row.xml, inflate把一个View的对象与XML布局文件关联并实例化
				
				holder = new ViewHolder();
				holder.text_itemName = (TextView) convertView
						.findViewById(to[0]);
				holder.text_itemTotal= (TextView) convertView
						.findViewById(to[1]);

				convertView.setTag(holder);

			} else {

				holder = (ViewHolder) convertView.getTag();
			}

			holder.text_itemName.setText((String) viewItem.get(from[0]));
			holder.text_itemTotal.setText((String) viewItem.get(from[1]));
			

			return convertView;
		}

	}
	@Override
	public void setUIRefreshConfig(UIThreadImpl ui) {
		// TODO Auto-generated method stub
		super.setUIRefreshConfig(ui);
	}

	@Override
	public void setAsynThreadConfig(String id, boolean isRepeat,
			AsynThreadImpl asynImpl) {
		// TODO Auto-generated method stub
		super.setAsynThreadConfig(id, isRepeat, asynImpl);
	}

	@Override
	public void startAsynThread(String id) {
		// TODO Auto-generated method stub
		super.startAsynThread(id);
	}

	@Override
	public void finishAsynThread(String id) {
		// TODO Auto-generated method stub
		super.finishAsynThread(id);
	}

	@Override
	public void addClickEventListener(int viewId,
			OnClickListener onClickListener) {
		// TODO Auto-generated method stub
		super.addClickEventListener(viewId, onClickListener);
	}

	@Override
	public void addTouchEventListener(int viewId,
			OnTouchListener onTouceListener) {
		// TODO Auto-generated method stub
		super.addTouchEventListener(viewId, onTouceListener);
	}
	
}
