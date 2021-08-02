package com.PersonalLancer.ecommerceapps.Model;

import java.util.List;

public class ModelProduct{
	private List<ListproductItem> listproduct;

	public void setListproduct(List<ListproductItem> listproduct){
		this.listproduct = listproduct;
	}

	public List<ListproductItem> getListproduct(){
		return listproduct;
	}
}