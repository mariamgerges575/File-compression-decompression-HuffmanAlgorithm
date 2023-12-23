package org.example;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Compress {
    HashMap<String, Integer> frequencies = new HashMap<>();
    PriorityQueue<Node> pq = new PriorityQueue<>();
    HashMap<String, String> codes = new HashMap<>();

    public void compressFile(String filePath, int n) {
        int bufferSize = 2048-(2048%n); // Adjust the buffer size as needed
//        int bufferSize = 40; // Adjust the buffer size as needed

        try (InputStream inputStream = new FileInputStream(filePath)) {
            byte[] buffer = new byte[bufferSize];
            int bytesRead;
            int howLong=-1;
            String specialKey="";
            String specialKeyP="";
            System.out.println("start frequency");
            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                System.out.println("bytessssssssssssss"+bytesRead);
                for (int i = 0; i < bytesRead; i += n) {
                    byte[] arr = Arrays.copyOfRange(buffer, i, Math.min(bytesRead, i + n));

                    StringBuilder nBytes = new StringBuilder();
                    for (byte b : arr) {
                        nBytes.append(b);
                        nBytes.append(":");
                    }
                    if(arr.length<n){
//                        System.out.println("ya rrrrrrrrrrrrrrrrb"+nBytes);
                        specialKeyP=nBytes.toString();
                       for(int j=arr.length;j<n;j++){
                           nBytes.append(0);
                           nBytes.append(":");
                       }
                       specialKey=nBytes.toString();
                       howLong=arr.length;
                    }
                    if (!frequencies.containsKey(nBytes.toString())) {
//                        System.out.println("keys "+nBytes);
                        frequencies.put(nBytes.toString(), 1);
                    } else
                        frequencies.put(nBytes.toString(), frequencies.get(nBytes.toString()) + 1);
                }

            }
            System.out.println("end frequency");
            System.out.println("start huffman");
            inputStream.close();
            for (String key : frequencies.keySet()) {
                if(key.equals(specialKey)){
                    pq.add(new Node(specialKeyP, frequencies.get(key)));
                }
                else
                     pq.add(new Node(key, frequencies.get(key)));
//                System.out.println( key + ": " + frequencies.get(key));
            }
            int unique=frequencies.size();

             File file = new File(filePath);
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
            System.out.println("end huffman");

//            System.out.println("ssssss" + codes.size());
//            printNodes(root);
            //////////////////////output file name//////////////////////
            int lastIndOf = filePath.lastIndexOf("\\") + 1;
            String outputFile = filePath.substring(0, lastIndOf) + "20011880." + n + "" + "." +
                    filePath.substring(lastIndOf) + ".hc";
            /////////////////////write header//////////////////////////////////
            System.out.println("start write freq");

            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
            ByteBuffer buffern = ByteBuffer.allocate(Integer.BYTES);
            buffern.putInt(n);
            byte[] bytes = buffern.array();
            outputStream.write(bytes);
            buffern.clear();
            buffern.putInt(unique);
            bytes = buffern.array();
            outputStream.write(bytes);
            int counter=0;
            int itsOrder=-1;
            for (String key : frequencies.keySet()) {
                if(key.equals(specialKey)){
                    itsOrder=counter;
                    String[] splitBytesStrings=specialKeyP.split(":");
                    byte[] splitBytes=new byte[n];
                    for(int i=0;i<splitBytesStrings.length;i++){
                        splitBytes[i]=Byte.parseByte(splitBytesStrings[i]);
                    }
                    outputStream.write(splitBytes);
                }
//                System.out.println("order "+key);
                else {
                    String[] splitBytesStrings = key.split(":");
                    byte[] splitBytes = new byte[n];
                    for (int i = 0; i < splitBytesStrings.length; i++) {
                        splitBytes[i] = Byte.parseByte(splitBytesStrings[i]);
                    }
                    outputStream.write(splitBytes);

                }
//                outputStream.write(key.getBytes(StandardCharsets.UTF_8));

//                outputStream.write(frequencies.get(key));
//                outputStream.write(':');
                buffern.clear();
                buffern.putInt(frequencies.get(key));
                bytes = buffern.array();
                outputStream.write(bytes);
                outputStream.flush();
                counter++;

//                outputStream.write(';');

            }
            System.out.println("end write freq");

            buffern.clear();
            buffern.putInt(itsOrder);
            bytes = buffern.array();
            outputStream.write(bytes);
            buffern.clear();
            buffern.putInt(howLong);
            bytes = buffern.array();
            outputStream.write(bytes);
//            outputStream.write('#');

            /////////////////////write compressed file//////////////////////////////////
            System.out.println("start write");

            StringBuilder acc = new StringBuilder();
            byte[] bufferxx = new byte[bufferSize];
            InputStream inputStream1 = new FileInputStream(filePath);
            while ((bytesRead = inputStream1.read(bufferxx)) != -1) {

                for (int i = 0; i < bytesRead; i += n) {
                    byte[] arr = Arrays.copyOfRange(bufferxx, i, Math.min(bytesRead, i + n));
                    StringBuilder nBytes = new StringBuilder();
                    for (byte b : arr) {
                        nBytes.append(b);
                        nBytes.append(":");
                    }
//                    System.out.println(nBytes);
//                String chunk=new String(arr, java.nio.charset.StandardCharsets.UTF_8);
                    String chunk = nBytes.toString();
                    if (!codes.containsKey(nBytes.toString())) {
                        System.out.println("something wrong");
                    }
                    String code = codes.get(chunk);
                    acc.append(code);
                if(acc.length()==8){
                    int intValue = Integer.parseInt(acc.toString(), 2); // Parse as an integer
                    byte b = (byte) intValue; // Convert to byte
//                    byte b = Byte.parseByte(acc, 2 );
                    outputStream.write(b);
                    acc = new StringBuilder();
                }
                    while (acc.length() >= 8) {
//                    System.out.println(acc.substring(0,8));
//                    System.out.println(acc);
                        int intValue = Integer.parseInt(acc.substring(0, 8), 2); // Parse as an integer
                        byte b = (byte) intValue; // Convert to byte
//                    byte b = Byte.parseByte(acc.substring(0,8), 2 );
                        outputStream.write(b);
                        acc = new StringBuilder(acc.substring(8));
                    }
                }
                outputStream.flush();
            }
            while (acc.length() > 8) {
                int intValue = Integer.parseInt(acc.substring(0,8), 2); // Parse as an integer
                byte b = (byte) intValue; // Convert to byte
//                byte b = Byte.parseByte(acc.substring(0, 8), 2);
                outputStream.write(b);
                acc = new StringBuilder(acc.substring(8));
            }
            if (acc.length() > 0) {
                int x =  acc.length();
                for (int i = 0; i < 8-x; i++) {
                    acc .append("0");
                }
                int intValue = Integer.parseInt(acc.toString(), 2); // Parse as an integer
                byte b = (byte) intValue; // Convert to byte
//                byte b = Byte.parseByte(acc, 2 );
                outputStream.write(b);
                outputStream.write((byte) x);
            }
            else{
                outputStream.write(0);
                outputStream.write(0);
            }
            outputStream.flush();
            outputStream.close();
            System.out.println("end write");


//            File f = new File(outputFile);
//            System.out.println(f.length());
//            System.out.println(file.length());
            //////////////////////finish writing////////////////////////
//            File f=new File(outputFile);
//            System.out.println("jhj"+f.length());
//            byte[] fileBytesn = Files.readAllBytes(Paths.get(outputFile));
//            System.out.println(fileBytesn.length);
//            String ss= String.valueOf(fileBytesn[3]);
//            System.out.println(ss);
//            int i=Integer.parseInt(ss);
//            System.out.println(i);


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    void prefix(Node root) {
        if (root.left == null || root.right == null) {
//            System.out.println(root.c+" "+root.code);
            codes.put(root.c, root.code);
            return;
        }
        root.left.code += root.code + '0';
        prefix(root.left);
        root.right.code += root.code + '1';
        prefix(root.right);
    }

    void printNodes(Node root) {
        if (root.left == null || root.right == null) {
            System.out.println(root.code);
            return;
        }
//        assert root.left != null;
        printNodes(root.left);
        printNodes(root.right);
    }

}
