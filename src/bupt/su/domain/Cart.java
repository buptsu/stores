package bupt.su.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Cart implements Serializable{
	//购物车项的集合 便于删除单个购物车项
	private Map<String, CartItem> map=new LinkedHashMap<String, CartItem>();
	//总金额
	private double total=0.0;
	//获取购物车项
	public Collection<CartItem> getItems(){
		return map.values();
	}
	//添加到购物车
	public void add2Cart(CartItem item){
		//判断购物车中有无改商品
		String pid = item.getProduct().getPid();
		//1.1 有   购买数量变为之前数量和此次添加数量之和
		if(map.containsKey(pid)){
			CartItem cartItem = map.get(pid);
			cartItem.setCount(cartItem.getCount()+item.getCount());
		}else{
			//无
			map.put(pid, item);
		}
		//设置总金额  当前总金额加上当前购物项的金额
		total+=item.getSubtotal();
		
	}
	//从购物车删除
	public void removeFromCart(String id){
		//从集合中移除
		CartItem item = map.remove(id);
		//修改总金额
		total-=item.getSubtotal();
	}
	//清空购物车
	public void clearCart(){
		//清空 总金额置零
		map.clear();
		total=0.0;
	}
	public Map<String, CartItem> getMap() {
		return map;
	}

	public void setMap(Map<String, CartItem> map) {
		this.map = map;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
	
}
