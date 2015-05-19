/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bl4ckbird.ebitm.bitmessage.entities;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author bl4ckbird
 */
public class Inbox {
    @Getter
    @Setter
    List<Message> inboxMessages = new ArrayList<>();
}
