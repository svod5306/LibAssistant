package com.example.libassistant;

public class AdditionalMyInfo {
    private String serviceName;
    private int serviceIcon;
    public AdditionalMyInfo(String name,int icon){
        serviceName=name;
        serviceIcon=icon;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setServiceIcon(int serviceIcon) {
        this.serviceIcon = serviceIcon;
    }

    public String getServiceName() {
        return serviceName;
    }

    public int getServiceIcon() {
        return serviceIcon;
    }
}
