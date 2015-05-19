/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bl4ckbird.ebitm.bitmessage.entities;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author bl4ckbird
 */
public class Address {
      @Getter
    @Setter
    private String chan = "";
     @Getter
    @Setter
    private String address= "";
      @Getter
    @Setter
    private String enabled= "";
       @Getter
    @Setter
    private String stream= "";
      @Getter
    @Setter
    private String label= "";
    
}
