package org.example;

import java.io.File;

public class Main {

    public static void main(String[] args) {
//        if(args.length> 3 || args.length<2){
//            System.out.println("Two or three arguments required huffman_<id>.jar c/d absolute_path_to_input_file n");
//            System.out.println("c for compress ,d for decompress");
//            System.out.println("n is number of bytes that will be considered together (required in case of compress");
//            return;
//        }
//        String choice = args[0];
//        String path = args[1];
        String choice ="c";
        String path = "C:\\Users\\mariam\\Desktop\\HuffmanAlgorithm\\src\\main\\java\\org\\example\\file.txt";
        if(choice.equals("c")){
            Compress compress=new Compress();
//            int n=Integer.parseInt(args[2]);
            int n=2;
            compress.compressFile(path, n);
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