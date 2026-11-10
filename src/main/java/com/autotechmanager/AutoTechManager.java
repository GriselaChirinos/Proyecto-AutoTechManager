/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to license this license
 */

package com.autotechmanager;

import Vista.FrmPrincipal;

/**
 *
 * @author 50496
 */
public class AutoTechManager {

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmPrincipal().setVisible(true);
            }
        });
    }
}