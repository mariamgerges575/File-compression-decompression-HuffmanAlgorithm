package org.example;


public class Node implements Comparable<Node>{
    String c;
    byte[] bytes;
    int frequency;
    Node left;
    Node right;

    String code="";
    Node(String c,int frequency){
        this.c=c;
        this.frequency=frequency;
    }
    Node(byte[] bytes,int frequency){
        this.bytes=bytes;
        this.frequency=frequency;
    }

    @Override
    public int compareTo(Node node) {
//        if( this.frequency == node.frequency)
//            return this.c.compareTo(node.c);
//        else
            return this.frequency - node.frequency;
    }
}
