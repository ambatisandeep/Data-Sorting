package org.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.reverseOrder;


public class DataSorting {
    //final static  String static_file="feed_sample.txt", sample ="sample.txt";

    public static void main(String args[]){
        HashMap<String,HashMap<String,Integer>> three_unique_sequences = new HashMap<String,HashMap<String,Integer>>(); // <3=200,[3,4,5,6],[3,4,5,67,]>
        List<String[]> fileLines = splitPattern(readFile(args[0]));
        three_unique_sequences = getTokens(fileLines);
        uniqueSignatures(three_unique_sequences);
    }



    public static List<Map.Entry<String, Integer>> sortByValue(HashMap<String, Integer> hm)
    {
        List<Map.Entry<String, Integer>> sorted_map =
                hm.entrySet()
                        .stream()
                        .sorted(reverseOrder(Map.Entry.comparingByValue()))
                        .collect(Collectors.toList());
        return sorted_map;
    }


    static public void uniqueSignatures (HashMap<String,HashMap<String,Integer>> list){
        StringBuilder builder = new StringBuilder();
        for(String key:list.keySet()){
            HashMap<String,Integer> valuesList = list.get(key);
            builder.append(key + " Unique Signature - "+valuesList.size() + "\n");
            List<Map.Entry<String, Integer>> topThree = sortByValue(valuesList);
            for(int i=0;i<3 && i<topThree.size();i++){
                builder.append(topThree.get(i) + "\n");
            }



        }
        try {
            File file = new File("output.txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.append(builder);
            }
        }

        // Catch block to handle the exception
        catch (IOException ex) {
            // Print messqage exception occurred as
            // invalid. directory local path is passed
            System.out.print("Invalid Path");
        }
    }

    static HashMap<String,HashMap<String,Integer>> getTokens(List<String[]> fileLines){
        HashMap<String,HashMap<String,Integer>> hashMap = new HashMap<String,HashMap<String,Integer>>();
        for(String[] s: fileLines){
            HashMap<String,Integer> list = new HashMap<String,Integer>();
            String pattern ="";
            if(hashMap.get(s[0])==null)
                hashMap.put(s[0],list);
            else
                list = hashMap.get(s[0]);
            for(String s1:s){
                String[] arr = s1.split("\\=");
                pattern+=arr[0]+",";
            }
            Integer i = list.get(pattern);
            if(i!=null){
                i=++i;
            }else{
                i=1;
            }
            list.put(pattern,i);

        }
        return hashMap;
    }
    static List<String[]> splitPattern(List<String> fileLines){
        List<String[]> list = new ArrayList<String[]>();
        for(String s: fileLines){
            list.add( s.split("\\|"));
        }
        return list;
    }
    static List<String> readFile(String fileName){

        List<String> fileLines = new ArrayList<String>();


        try {
            Files.lines(new File(fileName).toPath())
                    .map(s -> s.trim())
                    .filter(s -> !s.isEmpty())
                    .forEach(s->fileLines.add(s));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileLines;
    }
}
