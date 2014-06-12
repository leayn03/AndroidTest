package com.meal.saleactivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.salemealapp.R;
import com.meal.action.GrouponManageAction;
import com.meal.action.SellerManageAction;
import com.meal.activity.BaseActivity;
import com.meal.activity.ipml.AsynThreadImpl;
import com.meal.activity.ipml.UIThreadImpl;
import com.meal.bean.Groupon;
import com.meal.bean.Seller;
import com.meal.dialog.MyProgressDialog;
import com.meal.saleglobal.SaleGlobal;

public class SaleOrderList extends BaseActivity {

	private ArrayList<Object> orderDataArray;
	List<Map<String, Object>> orderList = new ArrayList<Map<String, Object>>();

	Map<String, Object> ordermap = new HashMap<String, Object>();

	ListView orderListview;
	ImageButton orderReturnButton;
	private GrouponManageAction grouponManage = GrouponManageAction	.getInstance(); // 初始化实例
	private SellerManageAction sellerManage = SellerManageAction.getInstance(); //初始化

	Seller seller = SaleGlobal.seller;
	long numberSeller=0L;
	
	private MyProgressDialog loginProgressDialog;   //progressbar 1
	
	ArrayList<Long> orderListDetailArray=new ArrayList<Long>();
	
	ArrayList<Groupon> groupListDetailArray=new ArrayList<Groupon>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orderlist);
		loginProgressDialog = MyProgressDialog.createDialog(this);   //progressbar 2
	
		 if(SaleGlobal.seller!=null)
		    {
			 numberSeller=SaleGlobal.seller.getSid();	
		    }
		
		orderListview = (ListView) findViewById(R.id.listView);

		initOrderList();


		 orderReturnButton=(ImageButton)findViewById(R.id.OrderReturnButton);
		 //返回按钮监听
		 orderReturnButton.setOnClickListener(new OnClickListener() {
		
		 @Override
		 public void onClick(View v) {
		 // TODO Auto-generated method stub
	     SaleOrderList.this.finish();
		 }
		 });

	}

	private void initOrderList() {

		if (orderList == null) {
			orderList = new ArrayList<Map<String, Object>>();
		} else {
			orderList.clear();
		}
		loginProgressDialog.show();           //progressbar3
		setUIRefreshConfig(new UIThreadImpl() {

			@Override
			public void refresh(Message msg) {
				// TODO Auto-generated method stub
				
				orderListview.setAdapter(new MyAdapter(SaleOrderList.this, R.layout.orderlist_row,
						orderList, new String[] { "itemNumber", "itemTime",
								"itemButton" }, new int[] { R.id.itemNumber,
								R.id.itemTime, R.id.itemButton }));
				loginProgressDialog.dismiss();  //progressbar 4
			}

		});

		setAsynThreadConfig("getSellerOrderList", true, new AsynThreadImpl() {

			@Override
			public Message excute() {
				// TODO Auto-generated method stub

				Message msg = Message.obtain();

				orderDataArray = grouponManage.getSellerGrouponList(String.valueOf(numberSeller));
				//只有状态为complete的订单才显示
				if(null != orderDataArray)
				{
					for (int i = 0; i < orderDataArray.size(); i++) {
						ordermap = new HashMap<String, Object>();

						Groupon tempOrder = (Groupon) orderDataArray.get(i);

						if (null != tempOrder && tempOrder.getStatus().equals("complete")) {
							long temp = tempOrder.getGid();
							ordermap.put("itemNumber", "团购订单号：" + temp);
							ordermap.put("itemTime", "订单时间：" + tempOrder.getTime());
							ordermap.put("itemButton", "查看详情");
							orderListDetailArray.add(i,tempOrder.getGid());//传gid
							groupListDetailArray.add(tempOrder);  //传groupon
							orderList.add(ordermap);
						}
					}
				}

				finishAsynThread("getSellerOrderList"); // 完成

				return msg;

			}
		});
		startAsynThread("getSellerOrderList"); // 开启线程

		

	}

	public class ViewHolder {
		TextView text_itemNum;
		TextView text_itemTime;
		Button btn_itemButton;
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
				holder.text_itemNum = (TextView) convertView
						.findViewById(to[0]);
				holder.text_itemTime = (TextView) convertView
						.findViewById(to[1]);
				holder.btn_itemButton = (Button) convertView
						.findViewById(to[2]);

				convertView.setTag(holder);

			} else {

				holder = (ViewHolder) convertView.getTag();
			}

			holder.text_itemNum.setText((String) viewItem.get(from[0]));
			holder.text_itemTime.setText((String) viewItem.get(from[1]));
			holder.btn_itemButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//Bundle bundle=new Bundle();
				//	bundle.putLong("gid", orderListDetailArray.get(position));
					Bundle bundleGroupon=new Bundle();
					bundleGroupon.putSerializable("group", groupListDetailArray.get(position));
				
					Intent intent = new Intent();
				//	intent.putExtras(bundle);
					intent.putExtras(bundleGroupon);
					intent.setClass(SaleOrderList.this, SaleOrderInfo.class);
					startActivity(intent);

				}
			});

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