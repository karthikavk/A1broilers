package in.kassapos.a1broilers.api;
public class Deliveryshedule{
	public Integer active,sort;
	public String id,description,companyid;
	public Deliveryshedule(String description){
		this.description=description;
	}
	public Deliveryshedule(){

	}

	@Override
	public String toString() {
		return description;
	}
}