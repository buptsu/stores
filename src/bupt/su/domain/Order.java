package bupt.su.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
/*`oid` varchar(32) NOT NULL,
`ordertime` datetime DEFAULT NULL,
`total` double DEFAULT NULL,
`state` int(11) DEFAULT NULL,
`address` varchar(30) DEFAULT NULL,
`name` varchar(20) DEFAULT NULL,
`telephone` varchar(20) DEFAULT NULL,
`uid` varchar(32) DEFAULT NULL,*/

public class Order implements Serializable{
	private String oid;
	private double total;
	private int state;//0未支付 1已支付 
	
	private String address;
	private String name;
	private String telephone;
	
	//订单所属用户
	private User user;
	//包含该哪些订单项
	private List<OrderItem> items=new LinkedList<>();
	
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	private Date ordertime;
	public Date getOrdertime() {
		return ordertime;
	}
	public void setOrdertime(Date ordertime) {
		this.ordertime = ordertime;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<OrderItem> getItems() {
		return items;
	}
	public void setItems(List<OrderItem> items) {
		this.items = items;
	}	
}
