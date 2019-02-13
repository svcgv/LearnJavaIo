package com.charjay.parent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 */
public class SuperDemo {

    public static void main(String[] args) {
        SerializePerson();

        User user=DeSerializePerson();

        System.out.println(user);
    }

    private static void SerializePerson(){
        try {
            ObjectOutputStream oo=new ObjectOutputStream(new FileOutputStream(new File("person")));
            User person=new User();
            person.setAge(18);
            oo.writeObject(person);

            System.out.println("序列化成功");

            oo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static User DeSerializePerson(){
        ObjectInputStream ois= null;
        try {
            ois = new ObjectInputStream(new FileInputStream(new File("person")));
            User user=(User)ois.readObject();
            return user;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
