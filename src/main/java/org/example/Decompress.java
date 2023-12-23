package org.example;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;

public class Decompress {
    HashMap<String, Integer> frequencies = new HashMap<>();
    PriorityQueue<Node> pq = new PriorityQueue<>();
    int n;
    public void decompressFile(String filePath){
        File file=new File(filePath);
        long fileLen=file.length();
        long counter=16;
        int lastIndOf = filePath.lastIndexOf("\\") + 1;
        String[] split =filePath.split("\\.");
        String outputFile = filePath.substring(0, lastIndOf) +"extracted."+ split[2]+"."+split[3];

//        int bufferSize = 20; // Adjust the buffer size as needed
        try (InputStream inputStream = new FileInputStream(filePath)) {

            byte[] buffer = new byte[4];
            int bytesRead=inputStream.read(buffer);
//            System.out.println(bytesRead);
            n=ByteBuffer.wrap(buffer).getInt();
//            System.out.println(n);
            buffer = new byte[4];
            bytesRead=inputStream.read(buffer);
            int unique=ByteBuffer.wrap(buffer).getInt();
            int k=0;
//            System.out.println(unique);
//            int bufferSize = 2048-(2048%(n+4));
//            buffer = new byte[2048-(2048%(n+4))];
            buffer=new byte[n+4];
            while ( k<unique  && (bytesRead = inputStream.read(buffer)) != -1 ) {
                counter+=bytesRead;
                    byte[] arr = Arrays.copyOfRange(buffer, 0, n);
                    StringBuilder nBytes = new StringBuilder();
                    for (byte b : arr) {
                        nBytes.append(b);
                        nBytes.append(":");
                    }
//                    arr = ;
                int intValue=ByteBuffer.wrap(Arrays.copyOfRange(buffer, n, n+4)).getInt();
//                int intValue = ((buffer[n+0] & 0xFF) << 24) |
//                        ((buffer[n+1] & 0xFF) << 16) |
//                        ((buffer[n+2] & 0xFF) << 8) |
//                        (buffer[n+3] & 0xFF);
                    frequencies.put(nBytes.toString(),intValue);
//                System.out.println(nBytes+": "+intValue);
                    k++;
            }
            buffer = new byte[4];
            bytesRead=inputStream.read(buffer);
            int itsOrder=ByteBuffer.wrap(buffer).getInt();
            buffer = new byte[4];
            bytesRead=inputStream.read(buffer);
            int howLong=ByteBuffer.wrap(buffer).getInt();
//            System.out.println("kkkkk "+itsOrder);
//            System.out.println(howLong);
            int c=0;
            for (String key : frequencies.keySet()) {
//                System.out.println("order"+ key);
                if(c==itsOrder){
//                    System.out.println(key);
                    String[] splitBytesStrings = key.split(":");
                    byte[] splitBytes = new byte[howLong];
                    for (int i = 0; i < howLong; i++) {
                        splitBytes[i] = Byte.parseByte(splitBytesStrings[i]);
                    }
                    pq.add(new Node(splitBytes, frequencies.get(key)));
                }
                else {
//                    System.out.println("ji "+key);
                    String[] splitBytesStrings = key.split(":");
                    byte[] splitBytes = new byte[n];
                    for (int i = 0; i < n; i++) {
                        splitBytes[i] = Byte.parseByte(splitBytesStrings[i]);
                    }
                    pq.add(new Node( splitBytes, frequencies.get(key)));
                }
                c++;
            }
            Node root = null;
            while (pq.size() > 1) {
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
            ///////////////////////////////////////
//            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
//            FileOutputStream  = new FileOutputStream(outputFile);
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
            ArrayList<Byte> lastTwo=new ArrayList<>();

            Node n=root;
            int bit=0;
            StringBuilder acc = new StringBuilder();
            int bufferSize = 2048 - (2048 % this.n);
//            int bufferSize = 40;

            byte[] bufferxx = new byte[  bufferSize ];

            while ((bytesRead = inputStream.read(bufferxx)) != -1) {
                for (int i = 0; i < bytesRead ; i ++) {
                    if(counter>=fileLen-2){
                        lastTwo.add(bufferxx[i]);
                        continue;
                    }
                    counter++;
                    byte b = bufferxx[i];
                    String convertedBinaryString = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
                    acc.append(convertedBinaryString);

                    while(bit<acc.length() ){
                        if(n.left==null || n.right==null){
                            outputStream.write(n.bytes);
                            acc =new StringBuilder(acc.substring(bit));
                            n=root;
                            bit=0;
                        }
                        else if(bit< acc.length() && acc.charAt(bit)=='0'){
                            n=n.left;
                            bit++;
                        }
                        else if(bit< acc.length() && acc.charAt(bit)=='1'){
                            n=n.right;
                            bit++;
                        }
                    }
                    if(n.left==null || n.right==null){
                        outputStream.write(n.bytes);
                        acc =new StringBuilder(acc.substring(bit));
                        n=root;
                        bit=0;
                    }
                }
//                lastTwo[oneOrTwo]=;

                //handle last byte
                outputStream.flush();
                if(bytesRead==2 ){
                    lastTwo.add(buffer[0]);
                    lastTwo.add(buffer[1]);

                }
                if (bytesRead==1){
                    lastTwo.add(buffer[0]);
                }
            }
//            System.out.println(lastTwo.get(0));
            int x=lastTwo.get(1);
            int y=acc.length();
            String convertedBinaryString = String.format("%8s", Integer.toBinaryString(lastTwo.get(0) & 0xFF)).replace(' ', '0');
//            bit--;
            acc.append(convertedBinaryString);
            while(bit<(x+y) && bit<acc.length()){
                if(n.left==null ||n.right==null){
                    outputStream.write(n.bytes);
                    acc =new StringBuilder(acc.substring(bit));
                    n=root;
                    bit=0;
                }
                else if(acc.charAt(bit)=='0'){
                    n=n.left;
                    bit++;
                }
                else if(acc.charAt(bit)=='1'){
                    n=n.right;
                    bit++;
                }
            }
            if(n.left==null ||n.right==null){
                outputStream.write(n.bytes);
            }

//            while (acc.length() > 0) {
//                System.out.println(acc.length());
//                for(int bit=0; bit<acc.length(); bit++){
//                    if(n.left==null || n.right==null){
//                        outputStream.write(n.bytes);
//                        acc =new StringBuilder(acc.substring(bit));
//                        n=root;
//                    }
//                    else if(acc.charAt(bit)=='0'){
//                        n=n.left;
//                    }
//                    else if(acc.charAt(bit)=='1'){
//                        n=n.right;
//                    }
//                }
//            }
            outputStream.flush();
            outputStream.close();


            //////////////////////finish writing////////////////////////

//            for (String key : frequencies.keySet()) {
//                System.out.println(key+": "+frequencies.get(key));
//                // System.out.println("Key: " + key + ", Value: " + frequencies.get(key));
//            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    void prefix(Node root) {
        if (root.left == null || root.right == null) {
//            System.out.println(root.c+" "+root.code);
            return;
        }
        root.left.code += root.code + '0';
        prefix(root.left);
        root.right.code += root.code + '1';
        prefix(root.right);
    }
}
