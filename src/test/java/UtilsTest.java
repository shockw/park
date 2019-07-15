import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.github.wxpay.sdk.MyConfig;
import com.github.wxpay.sdk.WXPay;

public class UtilsTest {
	public static void main(String[] args) throws Exception {
//		 MyConfig config = new MyConfig();
//	        WXPay wxpay = new WXPay(config);
//
//	        Map<String, String> data = new HashMap<String, String>();
//	        data.put("bill_date", "20190713");
//	        data.put("bill_type", "ALL");
//
//	        try {
//	            Map<String, String> resp = wxpay.downloadBill(data);
//	            System.out.println(resp);
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	        }
		Date d = new Date();
		System.out.println(d.getTime());
	}
	
}
