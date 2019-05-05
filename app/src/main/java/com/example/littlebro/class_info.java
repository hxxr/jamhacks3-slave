package com.example.littlebro;

public class class_info {
    public String telephone;
    public String name;

    public class_info(String first){
        name = first;
    }

    public void setTelephone(String number){
        telephone = "+1" + number;
    }

    public static void main(String []args) {
    /*class_info Smith = new class_info("John");

    Smith.setTelephone("8004685865");*/
    }

}
