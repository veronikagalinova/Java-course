import java.util.ArrayList;

public class ProductList {
	private ArrayList<ProductDetails> item;
	
	public void display() {
		System.out.println("Elements in item:");
		System.out.println(item.size());
		
		for (ProductDetails p : item) {
			System.out.println(p);
		}
	}
}
