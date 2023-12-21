package org.example;

import java.io.File;
import java.nio.ByteBuffer;

public class Main {

    public static void main(String[] args) {
//        String sss="107";
//        System.out.println(sss[1]);
//        byte b=Byte.parseByte(sss);
//        StringBuilder bb = new StringBuilder();
//        bb.append((char) b);
//        System.out.println(bb);
//        System.out.println(sss.length());
//        ?
//        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
//        buffer.putInt(5);
//        byte[] bytes = buffer.array();
//        int i=0;
//        while(i<4) {
//            if (bytes[i] != 0) break;
//            i++;
//        }
//
//        System.out.println(i);

//        if(args.length> 3 || args.length<2){
//            System.out.println("Two or three arguments required huffman_<id>.jar c/d absolute_path_to_input_file n");
//            System.out.println("c for compress ,d for decompress");
//            System.out.println("n is number of bytes that will be considered together (required in case of compress");
//            return;
//        }
//        String choice = args[0];
//        String path = args[1];
        String choice ="c";
        String path1 = "C:\\Users\\mariam\\Desktop\\HuffmanAlgorithm\\src\\main\\java\\org\\example\\file.txt";
//        String path="C:\\Users\\mariam\\Desktop\\HuffmanAlgorithm\\gbbct10.seq";
//        String path="C:\\Users\\mariam\\Desktop\\HuffmanAlgorithm\\James_F_Kurose,_Keith_Ross_Computer_Networking_A_Top_Down_Approach.pdf";

//        String path="C:\\Users\\mariam\\Desktop\\HuffmanAlgorithm\\20011880.1.gbbct10.seq.hc";
//        String path="C:\\Users\\mariam\\Desktop\\HuffmanAlgorithm\\gbbct10.seq\\20011880.1.gbbct10.seq.hc";
//        String path="C:\\Users\\mariam\\Desktop\\HuffmanAlgorithm\\20011880.1.James_F_Kurose,_Keith_Ross_Computer_Networking_A_Top_Down_Approach.pdf.hc";
        String path="C:\\Users\\mariam\\Desktop\\HuffmanAlgorithm\\src\\main\\java\\org\\example\\20011880.1.file.txt.hc";
        if(choice.equals("c")){
            Decompress decompress=new Decompress();
            Compress compress=new Compress();
//            int n=Integer.parseInt(args[2]);
            int n=1;
            compress.compressFile(path1, n);
            decompress.decompressFile(path);
        } else if (choice.equals("d")){
            Decompress decompress=new Decompress();
            decompress.decompressFile(path);
        } else {
            System.out.println("Two or three arguments required huffman_<id>.jar c/d absolute_path_to_input_file n");
            System.out.println("c for compress ,d for decompress");
            System.out.println("n is number of bytes that will be considered together (required in case of compress");
            return;
        }

    }
}