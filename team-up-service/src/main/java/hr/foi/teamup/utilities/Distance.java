/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.teamup.utilities;

/**
 *
 * @author Tomislav Turek
 */
public class Distance {
    
    public static final double EARTH_RADIUS = 6371;
    
    public static double toRad(double degrees) {
        return degrees * (Math.PI/180);
    }
    
}
