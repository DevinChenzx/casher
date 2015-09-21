/*
 * @Id: MerchantOrder.java 18:00:00 2006-2-17
 * 
 * @author xiexh
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core.domain;

import java.util.ArrayList;
import java.util.List;

import ebank.core.common.Validator;
import ebank.core.model.domain.GwGoods;
import ebank.core.model.domain.GwLgOptions;
import ebank.core.model.domain.GwLogistic;
import ebank.core.model.domain.GwOrders;
import ebank.core.model.domain.GwSuborders;
import ebank.core.model.domain.TradePrecard;

/**
 * @author xiexh
 * Description: 商户订单（xml解析使用)
 * 
 */
public class MerchantOrder implements java.io.Serializable{
	/**
	 * 序列化
	 */
	private static final long serialVersionUID = -1057262692208157624L;
	private GwOrders orders;
	private TradePrecard tradePrecard;
	
	
	private List<GwGoods> goodsList=new ArrayList<GwGoods>();
	
	private List<GwSuborders> subOrdersList=new ArrayList<GwSuborders>();
	
	
	private GwLogistic logistic;
	private List<GwLgOptions> lgoptionList=new ArrayList<GwLgOptions>();
	private String version;
	private String sortstr;
	
	@Validator(nullable=false)
	private String sign;
	
	@Validator(regx="DSA|RSA|MD5")
	private String sign_type;
	
	
	
	public String getSign_type() {
		return sign_type;
	}
	public void setSign_type(String signType) {
		sign_type = signType;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getSortstr() {
		return sortstr;
	}
	public void setSortstr(String sortstr) {
		this.sortstr = sortstr;
	}
	public GwOrders getOrders() {
		return orders;
	}
	public void setOrders(GwOrders orders) {
		this.orders = orders;
	}
	public List<GwGoods> getGoodsList() {
		return goodsList;
	}	
	public GwLogistic getLogistic() {
		return logistic;
	}
	public void setLogistic(GwLogistic logistic) {
		this.logistic = logistic;
	}
	public List<GwLgOptions> getLgoptionList() {
		return lgoptionList;
	}	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public void setGoodsList(List<GwGoods> goodsList) {
		this.goodsList = goodsList;
	}
	public void setLgoptionList(List<GwLgOptions> lgoptionList) {
		this.lgoptionList = lgoptionList;
	}
	public TradePrecard getTradePrecard() {
		return tradePrecard;
	}
	public void setTradePrecard(TradePrecard tradePrecard) {
		this.tradePrecard = tradePrecard;
	}
	
	public List<GwSuborders> getSubOrdersList() {
		return subOrdersList;
	}
	public void setSubOrdersList(List<GwSuborders> subOrdersList) {
		this.subOrdersList = subOrdersList;
	}
	
}
