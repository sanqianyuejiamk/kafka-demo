package com.mengka.springboot.controller;

import java.util.Date;

/**
 * @author huangyy
 * @date 2018/02/27.
 */
public class Taa {

    public static void main(String[] args){

        String address = "1869F";
        String address_t = "51A00FA2";
        int charInt2 = Integer.parseInt(address, 16);
        System.out.println("----------, charInt2 = "+charInt2);

        System.out.println(new Date().getTime());
    }
}
