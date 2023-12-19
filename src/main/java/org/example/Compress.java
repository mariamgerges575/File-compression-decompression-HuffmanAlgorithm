package org.example;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Compress {
    HashMap<String, Integer> frequencies = new HashMap<>();
    PriorityQueue<Node> pq=new PriorityQueue<>();
    public void compressFile(String filePath,int n){

        File file=new File(filePath);
        try {
            byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
            int fileLenInBytes= (int) file.length();
            for(int i = 0; i < fileLenInBytes; i+=n){
                byte[] arr = Arrays.copyOfRange(fileBytes, i, Math.min(fileLenInBytes,i+n));
                StringBuilder nBytes = new StringBuilder();
                for (byte b : arr) {
                    nBytes.append((char)b);
                }
                if (!frequencies.containsKey(nBytes.toString())) {
                    frequencies.put(nBytes.toString(), 1);
                } else
                    frequencies.put(nBytes.toString(),frequencies.get(nBytes.toString())+1);
            }
            for (String key : frequencies.keySet()) {
                pq.add(new Node(key,frequencies.get(key)));
                System.out.println("Key: " + key + ", Value: " + frequencies.get(key));
            }
            Node root=null;
            while(pq.size()>1){
                Node x = pq.poll();
                Node y = pq.poll();
                Node f = new Node("-", x.frequency + y.frequency);
                f.left = x;
                f.right = y;
                root = f;
                pq.add(f);
            }
            pq.poll();
            prefix(root);
//            for(Node x :pq){
//                System.out.println(x.c+": "+x.code);
//            }
            printNodes(root);
//            String path = "C:\\Users\\mariam\\Desktop\\HuffmanAlgorithm\\src\\main\\java\\org\\example\\file.txt";
            int lastIndOf=filePath.lastIndexOf("\\")+1;
            String outputFile= filePath.substring(0,lastIndOf)+"20011880."+n+"" +"."+
                    filePath.substring(lastIndOf)+".hc";

            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
            ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
            buffer.putInt(n);
            byte[] bytes = buffer.array();
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
            File f=new File(outputFile);
            System.out.println("jhj"+f.length());
            byte[] fileBytesn = Files.readAllBytes(Paths.get(outputFile));
            System.out.println(fileBytesn.length);
            String ss= String.valueOf(fileBytesn[3]);
            System.out.println(ss);
            int i=Integer.parseInt(ss);
            System.out.println(i+1);


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        for (String key : frequencies.keySet()) {
//            System.out.println("Key: " + key + ", Value: " + frequencies.get(key));
//        }

    }
    void prefix(Node root){
        if (root.left == null || root.right == null ) {
            pq.add(root);
            return;
        }
        root.left.code+=root.code+'0';
        prefix(root.left);
        root.right.code+=root.code+'1';
        prefix(root.right);
    }
    void printNodes(Node root){
        if (root.left == null || root.right == null ) {
            System.out.println(root.code);
            return;
        }
//        assert root.left != null;
        printNodes(root.left);
        printNodes(root.right);
    }

}
