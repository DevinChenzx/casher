package ebank.web.controller;

import beartool.Md5Encrypt;
import ebank.core.NoticeService;
import ebank.core.OrderService;
import ebank.core.UserService;
import ebank.core.bank.BankServiceFactory;
import ebank.core.common.ServiceException;
import ebank.core.common.util.Amount;
import ebank.core.common.util.CryptUtil;
import ebank.core.domain.BankOrder;
import ebank.core.domain.MerchantOrder;
import ebank.core.model.domain.CmCustomerChannel;
import ebank.core.model.domain.GwChannel;
import ebank.core.model.domain.GwOrders;
import ebank.core.model.domain.GwViewUser;
import ebank.web.common.util.PartnerInterface;
import ebank.web.common.util.RequestUtil;
import ebank.web.common.util.Validator;
import ebank.web.common.util.XSerialize;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Key;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class UnionPayController
  implements Controller
{
  private Log log = LogFactory.getLog(getClass());
  private UserService userService;
  private BankServiceFactory bsf;
  private String localDomain;
  private NoticeService notifyService;
  private PartnerInterface parterInterface;
  private OrderService orderService;
  private static Key key = XSerialize.getKey(null);

  public void setOrderService(OrderService orderService) {
    this.orderService = orderService;
  }

  public void setNotifyService(NoticeService notifyService) {
    this.notifyService = notifyService;
  }

  public String getLocalDomain() {
    return this.localDomain;
  }

  public void setLocalDomain(String localDomain) {
    this.localDomain = localDomain;
  }

  public void setParterInterface(PartnerInterface parterInterface) {
    this.parterInterface = parterInterface;
  }

  public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse rep)
    throws Exception
  {
    req.setCharacterEncoding("GBK");
    this.log.debug("request from url pay:" + RequestUtil.getIpAddr(req));
    String partner = req.getParameter("merchantNo");
    GwViewUser user = this.userService.getViewUser(partner, "online");
    String charset = req.getParameter("charset");
    String out_trade_no = req.getParameter("transId");
    try
    {
      if ((user == null) || (user.getMstate() != 1)) {
        throw new ServiceException("00004", 
          "商户不存在");
      }
      if (!"normal".equals(user.getStatus())) {
        throw new ServiceException("00003", 
          "商户状态不正常");
      }

      String seller_email = req.getParameter("sellerEmail");

      String subject = "unionPay";

      String amount = req.getParameter("totalFee");

      String notifyUrl = req.getParameter("notifyUrl");

      String body = "unionPay";

      String transType = req.getParameter("transType");

      String partnerkey = user.getMd5Key();

      String sign = req.getParameter("sign");

      String signStr = out_trade_no;

      if (!sign.equals(
        Md5Encrypt.md5(signStr + partnerkey, charset))) {
        throw new ServiceException("00002", 
          "签名失败");
      }

      List<CmCustomerChannel> channelList = this.userService
        .findUserChannelList(user.getUserId()+"");
      String svcCode = null;
      for (CmCustomerChannel channel : channelList) {
        if ("UNIONMOBILE".equals(channel.getBank_code())) {
          svcCode = "2928";
          break;
        }

      }

      if (svcCode == null) {
        throw new ServiceException("00003", 
          "服务未开通");
      }

      if ("createOrder".equals(transType))
      {
        Map sPara = new HashMap();
        sPara.put("service", "unionmobilepay");
        sPara.put("payment_type", "1");
        sPara.put("merchant_ID", partner);
        sPara.put("seller_email", seller_email);
        sPara.put("notify_url", notifyUrl);

        sPara.put("charset", charset);
        sPara.put("order_no", out_trade_no);
        sPara.put("title", subject);
        sPara.put("body", body);
        sPara.put("total_fee", amount);
        sPara.put("sign", sign);
        sPara.put("sign_type", "MD5");

        MerchantOrder morder = this.parterInterface
          .getMerchantOrderByService(sPara);

        morder.getOrders().setIps(RequestUtil.getIpAddr(req));

        if (Validator.isNull(morder.getOrders().getSeller_id())) {
          morder.getOrders().setSeller_id(partner);
        }

        GwViewUser seller = this.userService.getViewUserWithIdAndName(morder
          .getOrders().getSeller_id(), morder.getOrders()
          .getSeller_name());
        if (seller != null) {
          if (Validator.isNull(morder.getOrders().getSeller_id())) {
            morder.getOrders().setSeller_id(
              String.valueOf(seller.getCustomer_no()));
          }
          if (Validator.isNull(morder.getOrders().getSeller_name())) {
            morder.getOrders().setSeller_name(
              seller.getLogin_recepit());
          }

          if (Validator.isNull(morder.getOrders().getSeller_remarks()))
            morder.getOrders().setSeller_remarks(
              seller.getUserAlias());
        }
        else {
          throw new ServiceException(
            "00004", "商户不存在");
        }

        String orderid = this.orderService.tx_savePostOrder(morder);

        GwOrders order = this.orderService.findOrderByPk(orderid);

        HashMap map = new HashMap();
        map.put("_orderId", orderid);
        map.put("_persistence", XSerialize.serialize(
          order, key));
        map.put("_id", CryptUtil.encrypt(order
          .getId()));

        Map merchantjsonMap = new HashMap();
        merchantjsonMap.put("amount", amount);
        merchantjsonMap.put("transType", "createOrder");

        map.put("merchantjson", CryptUtil.encrypt(
          JSONObject.fromObject(merchantjsonMap).toString()));

        GwChannel channel = this.userService.getChannel(
          "UNIONMOBILE", "2", order.getAmount());
        int channelid = (int)channel.getId();
        map.put("_channelToken", 
          CryptUtil.randomcrypt(new int[] { channelid }));

        String result = this.notifyService.tx_responseNotice("utf-8", 
          this.localDomain + "/Ebank", getNameValuePair(map));
        rep.setContentType("text/xml;charset=UTF-8");

        JSONObject ret = JSONObject.fromObject(result);
        String retSignStr = ret.getString("result") + out_trade_no;
        String retSign = Md5Encrypt.md5(retSignStr + user.getMd5Key(), 
          charset);
        ret.accumulate("signature", retSign);
        ret.accumulate("transId", out_trade_no);
        rep.getWriter().write(ret.toString());
        rep.getWriter().close();
        return null;
      }

      respException(new ServiceException(
        "00005", "交易类型错误"), rep, user, 
        out_trade_no, charset);
      return null;
    }
    catch (Exception e) {
      e.printStackTrace();
      if ((e instanceof ServiceException)) {
        e.printStackTrace();
        respException((ServiceException)e, rep, user, out_trade_no, 
          charset);
      }
    }
    return null;
  }

  private void respException(ServiceException e, HttpServletResponse resp, GwViewUser user, String requestId, String charset)
  {
    resp.setContentType("text/xml;charset=UTF-8");
    try {
      if (user == null) {
        resp.getWriter().write(
          "{\"result\":\"" + e.getEventID() + "\",\"msg\":\"" + 
          e.getMessage() + "\",\"requestId\":\"" + 
          requestId + "\"}");
        resp.getWriter().close();
      } else {
        String signStr = e.getEventID() + requestId;
        String sign = Md5Encrypt.md5(signStr + user.getMd5Key(), 
          charset);
        resp.getWriter().write(
          "{\"resultSignature\":\"" + sign + "\",\"result\":\"" + 
          e.getEventID() + "\",\"msg\":\"" + 
          e.getMessage() + "\",\"requestId\":\"" + 
          requestId + "\"}");
        resp.getWriter().close();
      }
    } catch (IOException e1) {
      e1.printStackTrace();
    }
  }

  private BankOrder MapOrder2Bank(GwOrders order) {
    BankOrder bx = new BankOrder();
    bx.setAmount(Amount.getFormatAmount(String.valueOf(order.getAmount()), 
      -2));
    bx.setOrdernum(order.getOrdernum());
    bx.setCurrency(order.getCurrency());
    bx.setPayment_type(order.getOrder_type());
    return bx;
  }

  public String BuildMysign(Map sArray, String key, String charset) {
    String prestr = CreateLinkString(sArray);
    prestr = prestr + key;
    String mysign = Md5Encrypt.md5(prestr, charset);
    return mysign;
  }

  public NameValuePair[] getNameValuePair(Map<String, Object> bean) {
    List x = new ArrayList();
    for (Iterator iterator = bean.keySet().iterator(); iterator
      .hasNext(); )
    {
      String type = (String)iterator.next();
      x.add(new NameValuePair(type, String.valueOf(bean.get(type))));
    }
    Object[] y = x.toArray();
    NameValuePair[] n = new NameValuePair[y.length];
    System.arraycopy(y, 0, n, 0, y.length);
    return n;
  }

  public static Map<String, Object> ParaFilter(Map sArray)
  {
    List keys = new ArrayList(sArray.keySet());
    Map sArrayNew = new HashMap();

    for (int i = 0; i < keys.size(); i++) {
      String key = (String)keys.get(i);
      String value = (String)sArray.get(key);

      if ((!"".equals(value)) && (value != null) && 
        (!key.equalsIgnoreCase("sign")) && 
        (!key.equalsIgnoreCase("sign_type")))
      {
        sArrayNew.put(key, value);
      }
    }
    return sArrayNew;
  }

  public static String CreateLinkString(Map params)
  {
    List keys = new ArrayList(params.keySet());
    Collections.sort(keys);

    String prestr = "";

    for (int i = 0; i < keys.size(); i++) {
      String key = (String)keys.get(i);
      String value = (String)params.get(key);

      if (i == keys.size() - 1)
        prestr = prestr + key + "=" + value;
      else {
        prestr = prestr + key + "=" + value + "&";
      }
    }

    return prestr;
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  public void setBsf(BankServiceFactory bsf) {
    this.bsf = bsf;
  }
}