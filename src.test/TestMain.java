import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.Queue;

import net.sf.json.JSONObject;
import ebank.core.common.util.Amount;
import ebank.core.common.util.Clazz;
import ebank.core.model.domain.GwOrders;


public class TestMain {
	public static enum TO_TRXSTS {WAIT_TRADE,TRADE_FINISHED,TRADE_FAILURE,TRADE_CLOSED};
	
	public static void main(String[] args) {
		try {
			String x="{\"result\":\"true\",\"transCode\":\"91\",\"transIds\":[69,70],\"errorCode\":\"00\"}";
			final JSONObject jo=JSONObject.fromObject(x);
			final Object[] txids=(Object[])jo.getJSONArray("transIds").toArray();
			
			for (int i = 0; i < txids.length; i++) {
				System.out.println(txids[i]);
			}
			
			System.out.println(Amount.getFormatAmount("000001", -2));
			System.out.println(TestMain.TO_TRXSTS.TRADE_CLOSED.ordinal());
			System.out.println(MessageFormat.format("x{0},{1}",new String[]{"ie","hua"}));
			Clazz.Annotation(GwOrders.class, "royalty_parameters", "xie@hna.com^8.00^rer|xd@hna.com^bdd@hna.com^8.00^kkk");
			/*System.out.println("_input_charset".matches("^_([a-zA-Z]+)+_$"));
			
			System.out.println(UUID.randomUUID().toString().replaceAll("-", ""));
			Map<String, Object> mp=new HashMap();
			mp.put("kte", "3333<");
			mp.put("yyy",123);
			RequestUtil.HtmlEscapeMap(mp);
			System.out.println(mp);
			 JSONObject jo=new JSONObject();
			    jo.putAll(mp);
			    System.out.println(jo.toString());
			
			String x="   ";
			System.out.println(Validator.isNull(x));
			*/
			Queue<String> queue = new LinkedList<String>();   

	        queue.offer("2");   

	        queue.offer("3");   

	        queue.offer("1");   

	        System.out.println(queue.size());   

	        String str;   

	        while((str=queue.poll())!=null){   

	            System.out.print(str);   

	        }   
	        
	        
	        String aa = "idx=307?PayFlag=1";
	        
	        System.out.print("===========:"+aa.indexOf("PayFlag")); 
	        String[] bb = aa.split("\\?");
	        String[] cc = bb[1].split("=");
	        System.out.print("===========:"+cc[1]);  
	        
	        
	        String bsd= "2013-03-05 17:03:56";
	        String dfd =  bsd.substring(0, 10);
	        System.out.print("99999999999999:"+dfd); 
	        
	        
	        System.out.println("123456456:"+dfd.replaceAll("-", "")); 
	        
	        String xx = "<MessageData><CMBCB2B><Base><Version>1.0</Version><SignFlag>1</SignFlag><Language>GB2312</Language></Base><ResHeader><ServerTime>20130415101800</ServerTime></ResHeader><DataBody><MerchantTrnxNo>231304150012387542</MerchantTrnxNo><TrnxCode>P006</TrnxCode><ResultNotifyURL>null</ResultNotifyURL><Trnx><TrnxNo>231304150012387542</TrnxNo><OrderNo>100000000001504-0415102226282-0</OrderNo><TrnxType>BF01</TrnxType><TrnxCreateTime>20130415101618</TrnxCreateTime><TrnxDate>Mon Apr 15 00:00:00 CST 2013</TrnxDate><TrnxAmount>0.05</TrnxAmount><BuyerFeeAmount>null</BuyerFeeAmount><SellerFeeAmount>11.01</SellerFeeAmount><PayerAmount>0.05</PayerAmount><PayeeAmount>0.05</PayeeAmount><BankFeeAmount>null</BankFeeAmount><BourseFeeAmount>null</BourseFeeAmount><Status>0</Status></Trnx></DataBody></MessageData><SignData><Signature-Algorithm>SHA1withRSA</Signature-Algorithm><Signature>hIx3+YyRlhIO1Jc2ABIdVW5uAo4cZgBHOGKZVD9tlh2wQatARXYorm8ZOWzd/tPzz3wwHpH4mmDuUt0sJNPsWExSbLPqr9papUA6PRhfD/sgy9AxL2i/2bfHhtzxk//Z/kNgm51K+lBAu7EocfrlDppmDPjNeuGEuU5UBGVG2P8=</Signature><BankCertificate>MIIDqjCCAxOgAwIBAgIEP+vHoDANBgkqhkiG9w0BAQUFADApMQswCQYDVQQGEwJDTjEaMBgGA1UEChMRQ0ZDQSBPcGVyYXRpb24gQ0EwHhcNMDYxMjI3MDY1MzQyWhcNMDgxMjI3MDcyMzQyWjBmMQswCQYDVQQGEwJDTjEaMBgGA1UEChMRQ0ZDQSBPcGVyYXRpb24gQ0ExDTALBgNVBAsTBENNQkMxEjAQBgNVBAsTCUN1c3RvbWVyczEYMBYGA1UEAxQPOTJjbWJjQDAwMDAwMDAxMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDZfvEjA4u56u0aKfhIYwm6xsrZBKsRhiikE2YoiQ9XqUkAmPNKBgE9NvN5BPEVXEHRMweXtYRLBLdCmPP/nkJ7cCFKuVMPsvRWk4RHOrDspAzPQqhbM66IEDam1a+I2lR8B6kmMveBu5IMqvG+bsMS3oHAPR32Wm4FuvieKIp3eQIDAQABo4IBoDCCAZwwCwYDVR0PBAQDAgXgMCsGA1UdEAQkMCKADzIwMDYxMjI3MDY1MzQyWoEPMjAwODEyMjcwNzIzNDJaMBEGCWCGSAGG+EIBAQQEAwIFoDApBglghkgBhvhCAQIEHBYaaHR0cHM6Ly9TSVRFX05BTUUvY2RhLWNnaS8wSwYJYIZIAYb4QgEDBD4WPGNsaWVudGNnaS5leGU/YWN0aW9uPWNoZWNrUmV2b2NhdGlvbiYmQ1JMPWNuPUNSTDE3ODgmc2VyaWFsPTAfBgNVHREEGDAWgRR3YW5ndGluZ0BjbWJjLmNvbS5jbjBOBgNVHR8ERzBFMEOgQaA/pD0wOzELMAkGA1UEBhMCQ04xGjAYBgNVBAoTEUNGQ0EgT3BlcmF0aW9uIENBMRAwDgYDVQQDEwdDUkwxNzg4MB8GA1UdIwQYMBaAFMMnxjZoyCdlJIevkadLJjMC5RrpMB0GA1UdDgQWBBRElMUdhslGUAzD2INiYSm0UAbIPzAJBgNVHRMEAjAAMBkGCSqGSIb2fQdBAAQMMAobBFY2LjADAgOoMA0GCSqGSIb3DQEBBQUAA4GBAHtlC5eatxZAsHZcmz2kHPxT9pim4cMyOXB+aVGbxrejUly9bUXDPwrAk2qxLtPfbh+ndnwPsxP/+SNtfIok0NbO+LzM2npqA0G2WbyLnjtuFQINMtof+7G4LUvpEXQpJ0WHZO8manwSQv/Tm3AWxUwH8khn12MZk3a/xz7QVHgv</BankCertificate></SignData></CMBCB2B>";
	        
	        
	        String xx1 = "<CMBCB2B1><MessageData><Base><Version>1.0</Version><SignFlag>1</SignFlag><Language>GB2312</Language></Base><ResHeader><ServerTime>20130415101800</ServerTime></ResHeader><DataBody><MerchantTrnxNo>231304150012387542</MerchantTrnxNo><TrnxCode>P006</TrnxCode><ResultNotifyURL>null</ResultNotifyURL><Trnx><TrnxNo>231304150012387542</TrnxNo><OrderNo>100000000001504-0415102226282-0</OrderNo><TrnxType>BF01</TrnxType><TrnxCreateTime>20130415101618</TrnxCreateTime><TrnxDate>Mon Apr 15 00:00:00 CST 2013</TrnxDate><TrnxAmount>0.05</TrnxAmount><BuyerFeeAmount>null</BuyerFeeAmount><SellerFeeAmount>11.01</SellerFeeAmount><PayerAmount>0.05</PayerAmount><PayeeAmount>0.05</PayeeAmount><BankFeeAmount>null</BankFeeAmount><BourseFeeAmount>null</BourseFeeAmount><Status>0</Status></Trnx></DataBody></MessageData><SignData><Signature-Algorithm>SHA1withRSA</Signature-Algorithm><Signature>hIx3+YyRlhIO1Jc2ABIdVW5uAo4cZgBHOGKZVD9tlh2wQatARXYorm8ZOWzd/tPzz3wwHpH4mmDuUt0sJNPsWExSbLPqr9papUA6PRhfD/sgy9AxL2i/2bfHhtzxk//Z/kNgm51K+lBAu7EocfrlDppmDPjNeuGEuU5UBGVG2P8=</Signature><BankCertificate>MIIDqjCCAxOgAwIBAgIEP+vHoDANBgkqhkiG9w0BAQUFADApMQswCQYDVQQGEwJDTjEaMBgGA1UEChMRQ0ZDQSBPcGVyYXRpb24gQ0EwHhcNMDYxMjI3MDY1MzQyWhcNMDgxMjI3MDcyMzQyWjBmMQswCQYDVQQGEwJDTjEaMBgGA1UEChMRQ0ZDQSBPcGVyYXRpb24gQ0ExDTALBgNVBAsTBENNQkMxEjAQBgNVBAsTCUN1c3RvbWVyczEYMBYGA1UEAxQPOTJjbWJjQDAwMDAwMDAxMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDZfvEjA4u56u0aKfhIYwm6xsrZBKsRhiikE2YoiQ9XqUkAmPNKBgE9NvN5BPEVXEHRMweXtYRLBLdCmPP/nkJ7cCFKuVMPsvRWk4RHOrDspAzPQqhbM66IEDam1a+I2lR8B6kmMveBu5IMqvG+bsMS3oHAPR32Wm4FuvieKIp3eQIDAQABo4IBoDCCAZwwCwYDVR0PBAQDAgXgMCsGA1UdEAQkMCKADzIwMDYxMjI3MDY1MzQyWoEPMjAwODEyMjcwNzIzNDJaMBEGCWCGSAGG+EIBAQQEAwIFoDApBglghkgBhvhCAQIEHBYaaHR0cHM6Ly9TSVRFX05BTUUvY2RhLWNnaS8wSwYJYIZIAYb4QgEDBD4WPGNsaWVudGNnaS5leGU/YWN0aW9uPWNoZWNrUmV2b2NhdGlvbiYmQ1JMPWNuPUNSTDE3ODgmc2VyaWFsPTAfBgNVHREEGDAWgRR3YW5ndGluZ0BjbWJjLmNvbS5jbjBOBgNVHR8ERzBFMEOgQaA/pD0wOzELMAkGA1UEBhMCQ04xGjAYBgNVBAoTEUNGQ0EgT3BlcmF0aW9uIENBMRAwDgYDVQQDEwdDUkwxNzg4MB8GA1UdIwQYMBaAFMMnxjZoyCdlJIevkadLJjMC5RrpMB0GA1UdDgQWBBRElMUdhslGUAzD2INiYSm0UAbIPzAJBgNVHRMEAjAAMBkGCSqGSIb2fQdBAAQMMAobBFY2LjADAgOoMA0GCSqGSIb3DQEBBQUAA4GBAHtlC5eatxZAsHZcmz2kHPxT9pim4cMyOXB+aVGbxrejUly9bUXDPwrAk2qxLtPfbh+ndnwPsxP/+SNtfIok0NbO+LzM2npqA0G2WbyLnjtuFQINMtof+7G4LUvpEXQpJ0WHZO8manwSQv/Tm3AWxUwH8khn12MZk3a/xz7QVHgv</BankCertificate></SignData></CMBCB2B1>";
	        
	        
	        String xx2 = "<MessageData><Base><Version>1.0</Version><SignFlag>1</SignFlag><Language>GB2312</Language></Base><ResHeader><ServerTime>20130415101800</ServerTime></ResHeader><DataBody><MerchantTrnxNo>231304150012387542</MerchantTrnxNo><TrnxCode>P006</TrnxCode><ResultNotifyURL>null</ResultNotifyURL><Trnx><TrnxNo>231304150012387542</TrnxNo><OrderNo>100000000001504-0415102226282-0</OrderNo><TrnxType>BF01</TrnxType><TrnxCreateTime>20130415101618</TrnxCreateTime><TrnxDate>Mon Apr 15 00:00:00 CST 2013</TrnxDate><TrnxAmount>0.05</TrnxAmount><BuyerFeeAmount>null</BuyerFeeAmount><SellerFeeAmount>11.01</SellerFeeAmount><PayerAmount>0.05</PayerAmount><PayeeAmount>0.05</PayeeAmount><BankFeeAmount>null</BankFeeAmount><BourseFeeAmount>null</BourseFeeAmount><Status>0</Status></Trnx></DataBody></MessageData><SignData><Signature-Algorithm>SHA1withRSA</Signature-Algorithm><Signature>hIx3+YyRlhIO1Jc2ABIdVW5uAo4cZgBHOGKZVD9tlh2wQatARXYorm8ZOWzd/tPzz3wwHpH4mmDuUt0sJNPsWExSbLPqr9papUA6PRhfD/sgy9AxL2i/2bfHhtzxk//Z/kNgm51K+lBAu7EocfrlDppmDPjNeuGEuU5UBGVG2P8=</Signature><BankCertificate>MIIDqjCCAxOgAwIBAgIEP+vHoDANBgkqhkiG9w0BAQUFADApMQswCQYDVQQGEwJDTjEaMBgGA1UEChMRQ0ZDQSBPcGVyYXRpb24gQ0EwHhcNMDYxMjI3MDY1MzQyWhcNMDgxMjI3MDcyMzQyWjBmMQswCQYDVQQGEwJDTjEaMBgGA1UEChMRQ0ZDQSBPcGVyYXRpb24gQ0ExDTALBgNVBAsTBENNQkMxEjAQBgNVBAsTCUN1c3RvbWVyczEYMBYGA1UEAxQPOTJjbWJjQDAwMDAwMDAxMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDZfvEjA4u56u0aKfhIYwm6xsrZBKsRhiikE2YoiQ9XqUkAmPNKBgE9NvN5BPEVXEHRMweXtYRLBLdCmPP/nkJ7cCFKuVMPsvRWk4RHOrDspAzPQqhbM66IEDam1a+I2lR8B6kmMveBu5IMqvG+bsMS3oHAPR32Wm4FuvieKIp3eQIDAQABo4IBoDCCAZwwCwYDVR0PBAQDAgXgMCsGA1UdEAQkMCKADzIwMDYxMjI3MDY1MzQyWoEPMjAwODEyMjcwNzIzNDJaMBEGCWCGSAGG+EIBAQQEAwIFoDApBglghkgBhvhCAQIEHBYaaHR0cHM6Ly9TSVRFX05BTUUvY2RhLWNnaS8wSwYJYIZIAYb4QgEDBD4WPGNsaWVudGNnaS5leGU/YWN0aW9uPWNoZWNrUmV2b2NhdGlvbiYmQ1JMPWNuPUNSTDE3ODgmc2VyaWFsPTAfBgNVHREEGDAWgRR3YW5ndGluZ0BjbWJjLmNvbS5jbjBOBgNVHR8ERzBFMEOgQaA/pD0wOzELMAkGA1UEBhMCQ04xGjAYBgNVBAoTEUNGQ0EgT3BlcmF0aW9uIENBMRAwDgYDVQQDEwdDUkwxNzg4MB8GA1UdIwQYMBaAFMMnxjZoyCdlJIevkadLJjMC5RrpMB0GA1UdDgQWBBRElMUdhslGUAzD2INiYSm0UAbIPzAJBgNVHRMEAjAAMBkGCSqGSIb2fQdBAAQMMAobBFY2LjADAgOoMA0GCSqGSIb3DQEBBQUAA4GBAHtlC5eatxZAsHZcmz2kHPxT9pim4cMyOXB+aVGbxrejUly9bUXDPwrAk2qxLtPfbh+ndnwPsxP/+SNtfIok0NbO+LzM2npqA0G2WbyLnjtuFQINMtof+7G4LUvpEXQpJ0WHZO8manwSQv/Tm3AWxUwH8khn12MZk3a/xz7QVHgv</BankCertificate></SignData>";
	        
	        
	        
//	        String[] idxValue=xx.split("\\?");
//	        String payresult=idxValue[1].split("=")[1];
			
			System.out.println("===payresult=======:"+xx.indexOf("<CMBCB2B>"));  
			System.out.println("===45353453543=======:"+xx1.indexOf("<CMBCB2B>"));  
			System.out.println("===8888888888888=======:"+xx2.indexOf("<CMBCB2B>"));  
 
	       
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
	}

}
