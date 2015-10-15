/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author romainchigar
 */
public class ByteArrayWrapper {
    
    public static byte[] wrap(byte[] array) {
        int i=0;
        byte[] result;
        while (array[i] != 0)
            i++;
        result = new byte[i];
        for (int j=0 ; j<i ; j++)
            result[j] = array[j];
        return result;
    }
}
