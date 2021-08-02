package com.PersonalLancer.ecommerceapps.Api;


import com.PersonalLancer.ecommerceapps.Model.ListproductItem;
import com.PersonalLancer.ecommerceapps.Model.ModelProfilUser;

public class JSONResponse {

    private ModelProfilUser[] datauser;

    public ModelProfilUser[] getDatauser() { return datauser; }

    private ListproductItem[] listproduct;

    public ListproductItem[] getListproduct() {
        return listproduct;
    }



}
