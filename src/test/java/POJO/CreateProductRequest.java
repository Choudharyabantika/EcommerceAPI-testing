package POJO;

import java.util.List;

public class CreateProductRequest {
	private List<OrderDetails> orders;

	public List<OrderDetails> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderDetails> orders) {
		this.orders = orders;
	}
	
}
