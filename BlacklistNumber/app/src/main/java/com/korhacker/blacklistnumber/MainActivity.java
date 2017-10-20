package com.korhacker.blacklistnumber;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "DEBUG";

    NumberPicker npYear, npMonth, npDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((Button) findViewById(R.id.btn_getContact)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getCon, tactList();
                Toast.makeText(MainActivity.this, "유통기한: " + npYear.getValue() + "/" + npMonth.getValue() + "/" + npDay.getValue(), Toast.LENGTH_SHORT).show();
            }
        });

        npYear = (NumberPicker) findViewById(R.id.np_year);
        npYear.setMaxValue(2020);
        npYear.setMinValue(2017);

        npMonth = (NumberPicker) findViewById(R.id.np_month);
        npMonth.setMaxValue(12);
        npMonth.setMinValue(1);

        npDay = (NumberPicker) findViewById(R.id.np_day);
        npDay.setMaxValue(31);
        npDay.setMinValue(1);
    }

    private void getContactList() {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Log.i(TAG, "Name: " + name);
                        Log.i(TAG, "Phone Number: " + phoneNo);
                    }
                    pCur.close();
                }
            }
        }
        if (cur != null) {
            cur.close();
        }
    }
}
