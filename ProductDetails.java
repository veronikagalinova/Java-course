
public class ProductDetails {
	private String group;
	private String name;
	private String ndbno;
	
	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getNdbno() {
		return ndbno;
	}


	public void setNdbno(String ndbno) {
		this.ndbno = ndbno;
	}


	@Override
	public String toString() {
		return "ProductDetailsDTO [group=" + group + ", name=" + name + ", ndbno=" + ndbno + "]";
	}

}
