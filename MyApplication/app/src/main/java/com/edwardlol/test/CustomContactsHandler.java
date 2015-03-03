package com.edwardlol.test;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by edwardlol on 15/2/28.
 */


public class CustomContactsHandler {
//    public static boolean queryNameByNum(String num, Context context) {
//        Cursor cursor =
//                context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                        new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
//                        ContactsContract.CommonDataKinds.Phone.NUMBER + "='" + num + "'", null, null);
//        if (cursor != null) {
//            if (cursor.getCount() > 1) {
//                cursor.close();
//                return false;
//            } else {
//                if (cursor.moveToFirst()) {
//                    //return cursorOriginal.getString(cursorOriginal.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//                    cursor.close();
//                    return true;
//                } else {
//                    cursor.close();
//                    return false;
//                }
//            }
//        } else {
//            cursor.close();
//            return false;
//        }
//    }
    //根据电话号码查询姓名（在一个电话打过来时，如果此电话在通讯录中，则显示姓名）
    public boolean queryByNumber(String Number, Context context) {
        //uri=  content://com.android.contacts/data/phones/filter/#
        Uri uri = Uri.parse("content://com.android.contacts/data/phones/filter/" + Number);
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Data.DISPLAY_NAME}, null, null, null);
        if (cursor.moveToFirst()) {
            //Log.i("Contacts", "name=" + cursor.getString(0));
            return true;
        } else {
            return false;
        }
    }

    //一步一步添加数据
    public void AddContacts1(){
        //插入raw_contacts表，并获取_id属性
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Context context = MyApplication.getInstance();
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        long contact_id = ContentUris.parseId(resolver.insert(uri, values));
        //插入data表
        uri = Uri.parse("content://com.android.contacts/data");
        //add Name
        values.put("raw_contact_id", contact_id);
        values.put(ContactsContract.Data.MIMETYPE,"vnd.android.cursor.item/name");
        values.put("data2", "user1");
//        values.put("data1", "xzdong");
        resolver.insert(uri, values);
        values.clear();
        //add Phone
        values.put("raw_contact_id", contact_id);
        values.put(ContactsContract.Data.MIMETYPE,"vnd.android.cursor.item/phone_v2");
        values.put("data2", "2");	//手机
        values.put("data1", "11111");
        resolver.insert(uri, values);
        values.clear();
        //add email
        values.put("raw_contact_id", contact_id);
        values.put(ContactsContract.Data.MIMETYPE,"vnd.android.cursor.item/email_v2");
        values.put("data2", "2");	//单位
        values.put("data1", "user1@gmail.com");
        resolver.insert(uri, values);
        Log.i("edwardlol", "contact added");
    }

    public void AddContacts2() {
        //向raw_contacts表中添加数据
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        //获得ContentResolver
        Context context = MyApplication.getInstance();
        ContentResolver resolver = context.getContentResolver();
        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
        ContentProviderOperation op1 = ContentProviderOperation.newInsert(uri).withValue("account_name", null).build();
        operations.add(op1);

        //向data表中添加数据
        uri = Uri.parse("content://com.android.contacts/data");
        //0指的是operations集合下标为0的第一个元素，获得Id
        ContentProviderOperation op2 = ContentProviderOperation.newInsert(uri)
                .withValueBackReference("raw_contact_id", 0)
                .withValue("mimetype", "vnd.android.cursor.item/name")
                .withValue("data2", "user2")
                .build();
        operations.add(op2);

        ContentProviderOperation op3 = ContentProviderOperation.newInsert(uri)
                .withValueBackReference("raw_contact_id", 0)
                .withValue("mimetype", "vnd.android.cursor.item/phone_v2")
                .withValue("data1", "111111")
                .withValue("data2", "2")
                .build();
        operations.add(op3);

        ContentProviderOperation op4 = ContentProviderOperation.newInsert(uri)
                .withValueBackReference("raw_contact_id", 0)
                .withValue("mimetype", "vnd.android.cursor.item/email_v2")
                .withValue("data1", "user2@gmail.com")
                .withValue("data2", "1")
                .build();
        operations.add(op4);

        try {
            resolver.applyBatch("com.android.contacts", operations);
        } catch(Exception e) {
            Log.wtf("MyApp", "Something went wrong with foo!", e);
        }
        Log.i("edwardlol", "contact added");
    }


}
