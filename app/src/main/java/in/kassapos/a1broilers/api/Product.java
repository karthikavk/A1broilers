package in.kassapos.a1broilers.api;

public class Product{
	public Integer active,sort;
	public Float rate,minimquantity,increament;
	public java.sql.Timestamp created_date,modified_date;
	public String id,name,description,companyid,categoryid,imagepath,created_by,modified_by;
	public Float qty;
	public  Boolean ischanged=Boolean.FALSE;
	public Integer cutsize=0;

	public Float getAmount(){
		return rate*qty;
	}
}