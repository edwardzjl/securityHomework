package com.edwardlol.contactstest;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.app.Activity;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                // 获取返回的数据
                Uri contactData = data.getData();
                CursorLoader cursorLoader = new CursorLoader(this, contactData,
                        null, null, null, null);
                // 查询联系人信息
                Cursor cursor = cursorLoader.loadInBackground();
                // 如果查询到指定的联系人
                if (cursor.moveToFirst()) {
                    String contactId = cursor.getString(cursor
                            .getColumnIndex(ContactsContract.Contacts._ID));
                    // 获取指定的联系人查询该联系人的信息
                    String name = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                    String phoneNumbere = "此联系人暂时未输入电话号码";
                    Cursor phones = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                    + "=" + contactId, null, null);
                    if (phones.moveToFirst()) {
                        // 取出电话号码
                        phoneNumbere = phones
                                .getString(phones
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    //关闭游标
                    phones.close();
                    //显示联系人与联系电话
                    ed1.setText(name);
                    ed2.setText(phoneNumbere);

                }

            }
        }
    }
    */
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

*/
}