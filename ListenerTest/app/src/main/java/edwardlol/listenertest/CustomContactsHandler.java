package edwardlol.listenertest;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;

import utils.MyApplication;

/**
 * Created by edwardlol on 15/3/5.
 */
public class CustomContactsHandler {

    public void CustomContactsHandler() {
        Log.e("Tag", "im fncking created!");
    }
    //根据电话号码查询姓名（在一个电话打过来时，如果此电话在通讯录中，则显示姓名）
    public boolean queryByNumber(String Number, Context context) {
        //uri=  content://com.android.contacts/data/phones/filter/#
        Uri uri = Uri.parse("content://com.android.contacts/data/phones/filter/" + Number);
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Data.DISPLAY_NAME}, null, null, null);
        return cursor.moveToFirst();
    }

    public void AddContacts(Context context, String name, String number, String email) {
        //向raw_contacts表中添加数据
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        //获得ContentResolver
        //Context context = MyApplication.getInstance();
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
                .withValue("data2", name)
                .build();
        operations.add(op2);

        ContentProviderOperation op3 = ContentProviderOperation.newInsert(uri)
                .withValueBackReference("raw_contact_id", 0)
                .withValue("mimetype", "vnd.android.cursor.item/phone_v2")
                .withValue("data1", number)
                .withValue("data2", "2")
                .build();
        operations.add(op3);

        ContentProviderOperation op4 = ContentProviderOperation.newInsert(uri)
                .withValueBackReference("raw_contact_id", 0)
                .withValue("mimetype", "vnd.android.cursor.item/email_v2")
                .withValue("data1", email)
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
