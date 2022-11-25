package in.kassapos.a1broilers.api;
public class Servicearea{
	public Integer active;
	public java.sql.Timestamp created_date;
	public String id,pincode,companyid,created_by;

	public  Servicearea(){

	}
	public  Servicearea(String pincode){
		this.pincode=pincode;
	}

	@Override
	public String toString() {
		return pincode.toString();
	}
}