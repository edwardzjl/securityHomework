package com.edwardlol.autoanswertest;

/**
 * Created by edwardlol on 15/2/28.
 */

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;

import utils.MyApplication;

public class CustomContactsHandler {
    //根据电话号码查询姓名
    public boolean inContacts(String Number, Context context) {
        Uri uri = Uri.parse("content://com.android.contacts/data/phones/filter/" + Number);
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Data.DISPLAY_NAME}, null, null, null);
        if (cursor.moveToFirst()) {
            //Log.i("Contacts", "name = " + cursor.getString(0));
            return true;
        } else {
            return false;
        }
    }

    public void AddContacts() {
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
        //姓名
        //0指的是operations集合下标为0的第一个元素，获得Id
        ContentProviderOperation op2 = ContentProviderOperation.newInsert(uri)
                .withValueBackReference("raw_contact_id", 0)
                .withValue("mimetype", "vnd.android.cursor.item/name")
                .withValue("data2", "user2")
                .build();
        operations.add(op2);
        //号码
        ContentProviderOperation op3 = ContentProviderOperation.newInsert(uri)
                .withValueBackReference("raw_contact_id", 0)
                .withValue("mimetype", "vnd.android.cursor.item/phone_v2")
                .withValue("data1", "111111")
                .withValue("data2", "2")
                .build();
        operations.add(op3);
        //邮件
        ContentProviderOperation op4 = ContentProviderOperation.newInsert(uri)
                .withValueBackReference("raw_contact_id", 0)
                .withValue("mimetype", "vnd.android.cursor.item/email_v2")
                .withValue("data1", "user2@gmail.com")
                .withValue("data2", "1")
                .build();
        operations.add(op4);
        //完成添加
        try {
            resolver.applyBatch("com.android.contacts", operations);
        } catch(Exception e) {
            Log.wtf("edwardlol", "Something went wrong with foo!", e);
        }
        Log.i("edwardlol", "contact added");
    }
}
