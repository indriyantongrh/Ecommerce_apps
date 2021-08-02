package com.PersonalLancer.ecommerceapps.Model;

public class ListproductItem{
	private String kondisi;
	private String harga;
	private String berat;
	private String merchant;
	private String id;
	private String deskripsi;
	private String namaProduct;
	private String stock;
	private String gambar;

	public void setKondisi(String kondisi){
		this.kondisi = kondisi;
	}

	public String getKondisi(){
		return kondisi;
	}

	public void setHarga(String harga){
		this.harga = harga;
	}

	public String getHarga(){
		return harga;
	}

	public void setBerat(String berat){
		this.berat = berat;
	}

	public String getBerat(){
		return berat;
	}

	public void setMerchant(String merchant){
		this.merchant = merchant;
	}

	public String getMerchant(){
		return merchant;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setDeskripsi(String deskripsi){
		this.deskripsi = deskripsi;
	}

	public String getDeskripsi(){
		return deskripsi;
	}

	public void setNamaProduct(String namaProduct){
		this.namaProduct = namaProduct;
	}

	public String getNamaProduct(){
		return namaProduct;
	}

	public void setStock(String stock){
		this.stock = stock;
	}

	public String getStock(){
		return stock;
	}

	public void setGambar(String gambar){
		this.gambar = gambar;
	}

	public String getGambar(){
		return gambar;
	}
}
