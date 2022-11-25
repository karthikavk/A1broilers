package in.kassapos.a1broilers.api;

import java.util.ArrayList;
import java.util.List;

public class ResponseInfo {

	private Boolean isError = Boolean.FALSE; // error status
	private String output; // output text.
	private List<String> errors; // error informations.
	private List<String> errorDatas; // error datas.
    public void set(String output,Boolean iserr){
    	this.isError=iserr;
    	this.output=output;
    }
	/**
	 * @return the isError
	 */
	public Boolean getIsError() {
		return isError;
	}

	/**
	 * @param isError
	 *            the isError to set
	 */
	public void setIsError(Boolean isError) {
		this.isError = isError;
	}

	/**
	 * @return the output
	 */
	public String getOutput() {
		return output;
	}

	/**
	 * @param output
	 *            the output to set
	 */
	public void setOutput(String output) {
		this.output = output;
	}

	/**
	 * @return the errors
	 */
	public List<String> getErrors() {
		if(errors==null){
			errors=new ArrayList<String>();
		}
		return errors;
	}

	/**
	 * @param errors
	 *            the errors to set
	 */
	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	/**
	 * @return the errorDatas
	 */
	public List<String> getErrorDatas() {
		return errorDatas;
	}

	/**
	 * @param errorDatas
	 *            the errorDatas to set
	 */
	public void setErrorDatas(List<String> errorDatas) {
		this.errorDatas = errorDatas;
	}

}