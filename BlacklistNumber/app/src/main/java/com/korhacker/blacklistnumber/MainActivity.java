package com.korhacker.blacklistnumber;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "DEBUG";
    String mStr_contacts;
    String mStr_preNum;
    ProgressDialog progressDialog;
    ArrayList<MyContactInfo> mArr_contactInfo;
    ListView mlv_contactList;
    Button mBtn_onSaveBlackList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStr_preNum = "";
        mArr_contactInfo = new ArrayList<MyContactInfo>();

        mStr_contacts = "";
        mlv_contactList = (ListView) findViewById(R.id.lv_contactList);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("get contacts info"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.setCancelable(false);

        mBtn_onSaveBlackList = (Button) findViewById(R.id.btn_onSaveBlackList);
        mBtn_onSaveBlackList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyListAdapter adapter = (MyListAdapter) mlv_contactList.getAdapter();
                adapter.setCheckBoxVisibility(false);
                mArr_contactInfo = adapter.getContactsArray();
                mBtn_onSaveBlackList.setVisibility(View.GONE);
            }
        });

        /* 주소 가져오기 */
        new getContactList().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_manage_black_list:
                MyListAdapter adapter = (MyListAdapter) mlv_contactList.getAdapter();
                if (adapter == null)
                    return false;
                adapter.setCheckBoxVisibility(true);
                mBtn_onSaveBlackList.setVisibility(View.VISIBLE);
                break;

            case R.id.menu_show_black_list:
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.custom_dialog);
                dialog.setTitle("Black List");

                ListView listView = (ListView) dialog.findViewById(R.id.lv_blackList);
                ArrayList<MyContactInfo> arrBlackContacts = new ArrayList<>();
                for (int i = 0; i < mArr_contactInfo.size(); i++) {
                    MyContactInfo contactInfo = mArr_contactInfo.get(i);
                    if (contactInfo.isChecked())
                        arrBlackContacts.add(contactInfo);
                }
                MyListAdapter listAdapter = new MyListAdapter(MainActivity.this, arrBlackContacts);
                listView.setAdapter(listAdapter);

                ((Button) dialog.findViewById(R.id.btn_ok)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class getContactList extends AsyncTask<URL, Integer, Long> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected Long doInBackground(URL... urls) {
            ContentResolver cr = getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

            if ((cur != null ? cur.getCount() : 0) > 0) {
                while (cur != null && cur.moveToNext()) {
                    String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        Cursor pCur = cr.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        while (pCur.moveToNext()) {
                            String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        /*Log.i(TAG, "Name: " + name);
                        Log.i(TAG, "Phone Number: " + phoneNo);*/
                            int type = pCur.getInt(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                            switch (type) {
                                case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                    if (mStr_preNum.equals(phoneNo))
                                        break;

                                    MyContactInfo contactInfo = new MyContactInfo();
                                    contactInfo.setName(name);
                                    contactInfo.setPhoneNum(phoneNo);
                                    mArr_contactInfo.add(contactInfo);
                                    mStr_contacts += (name + " / " + phoneNo + "\n");
                                    mStr_preNum = phoneNo;
                                    break;
                            }
                        }
                        pCur.close();


                    }
                }
            }
            if (cur != null) {
                cur.close();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            //tv_contacts.setText(mStr_contacts);
            progressDialog.dismiss();
            MyListAdapter listAdapter = new MyListAdapter(MainActivity.this, mArr_contactInfo);
            mlv_contactList.setAdapter(listAdapter);
        }
    }
}