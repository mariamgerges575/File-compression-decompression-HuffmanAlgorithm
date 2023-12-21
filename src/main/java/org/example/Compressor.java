package org.example;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Compressor {
        HashMap<String, Integer> frequencies = new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>();
        HashMap<String, String> codes = new HashMap<>();

        public void compressFile(String filePath, int n) {
                File file = new File(filePath);
                try {
                    byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
                    int fileLenInBytes = (int) file.length();
                    for (int i = 0; i < fileLenInBytes; i += n) {
                        byte[] arr = Arrays.copyOfRange(fileBytes, i, Math.min(fileLenInBytes, i + n));
                        StringBuilder nBytes = new StringBuilder();
                        for (byte b : arr) {
                            nBytes.append((char) b);
                        }
                        if (!frequencies.containsKey(nBytes.toString())) {
                            frequencies.put(nBytes.toString(), 1);
                        } else
                            frequencies.put(nBytes.toString(), frequencies.get(nBytes.toString()) + 1);
                    }
                    for (String key : frequencies.keySet()) {
                        pq.add(new Node(key, frequencies.get(key)));
                        System.out.println("Key: " + key + ", Value: " + frequencies.get(key));
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
                    System.out.println("ssssss" + codes.size());
                    printNodes(root);
                    //////////////////////output file name//////////////////////
                    int lastIndOf = filePath.lastIndexOf("\\") + 1;
                    String outputFile = filePath.substring(0, lastIndOf) + "20011880." + n + "" + "." +
                            filePath.substring(lastIndOf) + ".hc";
                    /////////////////////write header//////////////////////////////////

                    BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
                    ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
                    buffer.putInt(n);
                    byte[] bytes = buffer.array();
                    outputStream.write(bytes);
                    for (String key : frequencies.keySet()) {
                        outputStream.write(key.getBytes(StandardCharsets.UTF_8));
//                outputStream.write(':');
                        buffer.clear();
                        buffer.putInt(frequencies.get(key));
                        byte[] bytesK = buffer.array();
                        int i = 0;
                        while (i < 4) {
                            if (bytes[i] != 0) break;
                            i++;
                        }
                        outputStream.write(Arrays.copyOfRange(bytesK, i, 4));
                        outputStream.write(';');

                    }
                    outputStream.write('#');
                    /////////////////////write compressed file//////////////////////////////////

                    String acc = "";
                    for (int i = 0; i < fileLenInBytes; i += n) {
                        byte[] arr = Arrays.copyOfRange(fileBytes, i, Math.min(fileLenInBytes, i + n));
                        StringBuilder nBytes = new StringBuilder();
                        for (byte b : arr) {
                            nBytes.append((char) b);
                        }
//                String chunk=new String(arr, java.nio.charset.StandardCharsets.UTF_8);
                        String chunk = nBytes.toString();
                        if (!codes.containsKey(nBytes.toString())) {
                            System.out.println("a7a");
                        }
                        String code = codes.get(chunk);
                        acc += code;
//                if(acc.length()==8){
//                    int intValue = Integer.parseInt(acc, 2); // Parse as an integer
//                    byte b = (byte) intValue; // Convert to byte
////                    byte b = Byte.parseByte(acc, 2 );
//                    outputStream.write(b);
//                    acc = "";
//                }
                        while (acc.length() >= 8) {
//                    System.out.println(acc.substring(0,8));
//                    System.out.println(acc);
                            int intValue = Integer.parseInt(acc.substring(0, 8), 2); // Parse as an integer
                            byte b = (byte) intValue; // Convert to byte
//                    byte b = Byte.parseByte(acc.substring(0,8), 2 );
                            outputStream.write(b);
                            acc = acc.substring(8);
                        }
                    }
                    while (acc.length() > 8) {
                        int intValue = Integer.parseInt(acc.substring(0,8), 2); // Parse as an integer
                        byte b = (byte) intValue; // Convert to byte
//                byte b = Byte.parseByte(acc.substring(0, 8), 2);
                        outputStream.write(b);
                        acc = acc.substring(8);
                    }
                    if (acc.length() > 0) {
                        int x = 8 - acc.length();
                        for (int i = 0; i < x; i++) {
                            acc += "0";
                        }
                        int intValue = Integer.parseInt(acc, 2); // Parse as an integer
                        byte b = (byte) intValue; // Convert to byte
//                byte b = Byte.parseByte(acc, 2 );
                        outputStream.write(b);
                        outputStream.write('$');
                        outputStream.write((char) acc.length());
                    }
                    outputStream.flush();
                    outputStream.close();

                    File f = new File(outputFile);
                    System.out.println(f.length());
                    System.out.println(file.length());
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



