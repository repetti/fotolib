package org.repetti.fotolib.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.io.CharSink;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import org.repetti.utils.JsonHelper;
import org.repetti.utils.SecurityHelper;
import org.repetti.utils.StringHelper;
import org.repetti.utils.UtilsException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date: 24/07/15
 *
 * @author repetti
 */
public class Main {
    public static void main(String[] args) throws UtilsException, IOException {
        HashMap<String, Info> collection = new HashMap<String, Info>();
        HashMap<String, Info> collection2 = new HashMap<String, Info>();

//        add(collection, new File("blabla"));
        addFromFile(collection, new File("32gb_card"));
        System.out.println(collection.size());

//        addFromFile(collection2, new File("tmp"));
        add(collection2, new File("/blabla"));
//        add(collection2, new File(""));
//        add(collection2, new File(""));
//        add(collection2, new File(""));
        System.out.println(collection2.size());

        saveToFile(collection2, new File("tmp"), false);

        List<String> tmp = new ArrayList<String>(collection2.keySet());

        for (String t : tmp) {
            Info f = collection.get(t);
            Info f2 = collection2.get(t);
            if (f != null) {
                if (f2.equals(f)) {
                    collection2.remove(t);
                }
            }
        }
        System.out.println(collection2.size());
        print(collection2);

//        saveToFile(collection, new File("32gb_card"), false);

//        File f = new File(".");
//        add(collection, f);
//        addFromFile(collection, new File("1"));
//        print(collection);
//        saveToFile(collection, new File("1"));

    }

    public static void add(HashMap<String, Info> collection, File f) throws UtilsException {
        if (!f.exists()) {
            throw new NullPointerException("file not found: " + f.getAbsolutePath());
        }

        if (f.isDirectory()) {
            File [] files = f.listFiles();
            if (files == null) {
                throw new NullPointerException("Path doesnt exist: " + f.getAbsolutePath());
            }
            for (File t : files) {
                add(collection, t);
            }
        } else {
            Info i = Helper.getInfo(f);
            if (collection.containsKey(i.getHash())) {
                System.out.println("\nCOLLISION");
                System.out.println(collection.get(i.getHash()).toJson());
                System.out.println(i.toJson());
            }
            collection.put(i.getHash(), i);
        }
    }

    public static void saveToFile(Map<String, Info> collection, File to, boolean append) throws IOException {
        if (!append) {
            to.delete();
        }
//        if (to.exists()) {
            CharSink cs = Files.asCharSink(to, Charset.defaultCharset(), FileWriteMode.APPEND);
            for (Map.Entry<String, Info> t : collection.entrySet()) {
                cs.write(JsonHelper.serialize(t.getValue().toJson()));
                cs.write("\n");

            }
//            return;
//        }
//        throw new NullPointerException("to=" + to);
    }

    public static void addFromFile(final Map<String, Info> collection, File from) throws IOException {
        if (from.exists()) {
            Files.readLines(from, Charset.defaultCharset(), new LineProcessor<Object>() {
                @Override
                public boolean processLine(String line) throws IOException {
                    if (line.trim().length() == 0) {
                        return true;
                    }
                    try {
                        JsonNode j = JsonHelper.parse(line);
                        Info i = Info.fromJson(j);
                        collection.put(i.getHash(), i);
                    } catch (UtilsException e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                @Override
                public Object getResult() {
                    return null;
                }
            });
            return;
        }
        throw new NullPointerException("from=" + from);
    }

    public static void print(Map<String, Info> collection) {
        for (Map.Entry<String, Info> t : collection.entrySet()) {
            System.out.println(t.getValue().toJson());
        }
    }
}
