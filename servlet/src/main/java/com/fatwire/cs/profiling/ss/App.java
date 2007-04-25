package com.fatwire.cs.profiling.ss;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class App {

    /**
     * @param args
     */
    public static void main(String[] args) {
        List<String> queue = new ArrayList<String>();
        //http://www.rezidorparkinn.com
        //queue.add("/cs/ContentServer?c=Page&language=en&pagename=ParkInn%2FPage%2FparkinnFrontpage&ft_ss=true");
        //queue.add("/cs/ContentServer?c=Page&childpagename=FirstSiteII%2FFSIILayout&cid=1118867611403&pagename=FSIIWrapper&ft_ss=true");
        
        queue.add("/cs/ContentServer?pagename=RadissonSAS/Page/rsasFrontpage&c=Page&cid=1047389398279&language=en&ft_ss=true");
        /*
         *         queue.add("/cs/ContentServer?pagename=RadissonSAS/Page/rsasFrontpage&c=Page&cid=1047389398279&language=de&ft_ss=true");
        queue.add("/cs/ContentServer?pagename=RadissonSAS/Page/rsasFrontpage&c=Page&cid=1047389398279&language=fr&ft_ss=true");
        queue.add("/cs/ContentServer?pagename=RadissonSAS/Page/rsasFrontpage&c=Page&cid=1047389398279&language=fi&ft_ss=true");
        queue.add("/cs/ContentServer?pagename=RadissonSAS/Page/rsasFrontpage&c=Page&cid=1047389398279&language=no&ft_ss=true");
        queue.add("/cs/ContentServer?pagename=RadissonSAS/Page/rsasFrontpage&c=Page&cid=1047389398279&language=se&ft_ss=true");
        */
        
        Set<String> urlsDone = new HashSet<String>();
        URLReaderService reader = new URLReaderService(queue);
        reader.setHostname("radium.nl.fatwire.com");
        reader.setPort(9400);
        reader.setPath("/cs/");
        reader.setUrlsDone(urlsDone);
        reader.start();
        try {
            reader.waitForCompletion();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        for (String s : urlsDone){
            System.out.println(s);
        }
        System.exit(0);
        
        
        

    }

}
