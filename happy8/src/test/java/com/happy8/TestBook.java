package com.happy8;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.happy8.args.AddTableReqArgs;
import com.happy8.args.ConsumeCouponReqArgs;
import com.happy8.args.CouponItem;
import com.happy8.args.OrderItem;
import com.happy8.args.QueryTableItem;
import com.happy8.args.UpdateTableInfoArgs;
import com.happy8.args.UpdateTablePicsReqArgs;
import com.happy8.args.UserLevelArgs;

public class TestBook {
	public static void main(String[] args) {
//		ApproveClubReqArgs args1 = new ApproveClubReqArgs();
//		args1.setClubId(123);
//		args1.setAction(1);
//		args1.setUserId("13500000000");
		
//		RateClubArgs args1 = new RateClubArgs();
//		args1.setClubId(123);
//		args1.setRateValue(5);
//		args1.setUserId("13500000000");
		
//		AddCouponReqArgs req = new AddCouponReqArgs();
//		req.setType(1);
//		req.setDisCount(85);
//		req.setValue(10.5);
//		req.setStartAmout(100.5);
//		AddCouponRspArgs req = new AddCouponRspArgs();
//		UUID guid = UUID.randomUUID(); 
//		req.setCouponId(guid.toString());
//		CouponItem req = new CouponItem();
//		UUID guid = UUID.randomUUID(); 
//		req.setCouponId(guid.toString());
//		req.setType(1);
//		req.setDisCount(85);
//		req.setValue(10.5);
//		req.setStartAmout(100.5);
//		
//		List<CouponItem> reqRes = new ArrayList();
//		reqRes.add(req);
		ArrayList<QueryTableItem> reqRes =new ArrayList<QueryTableItem>();
		QueryTableItem item = new QueryTableItem();
		item.setOrderStatus(1);
		item.setPrice(12.5f);
		item.setSignature("tet");
		item.setType(2);
		item.setTableId(23);
		item.setTableName("麻将");
		item.setUserId("13500000000");
		reqRes.add(item);
		String str = JSON.toJSONString(reqRes);
		
		System.out.println(str);
	}
}
