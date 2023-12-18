package org.example;


public class Node implements Comparable<Node>{
    String c;
    int frequency;
    Node left;
    Node right;

    String code="";
    Node(String c,int frequency){
        this.c=c;
        this.frequency=frequency;
    }

    @Override
    public int compareTo(Node node) {
        return this.frequency - node.frequency;
    }
}
