package com.mengka.springboot.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author huangyy
 * @version cabbage-forward2.0,2018-03-12
 * @since cabbage-forward2.0
 */
@Data
public class CabbageProReq implements Serializable {

    private String type;

    private Integer car;

    private Integer seq;
}
