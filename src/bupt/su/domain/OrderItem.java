package bupt.su.domain;

public class OrderItem {
	private String itemid;
	private Product product;
	private Integer count;
	private double subtotal;
	//订单项所属的订单
	private Order order;
	
	public String getItemid(){
		return itemid;
	}
	public void setItemid(String itemid) {
		this.itemid = itemid;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public double getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	
	
}
