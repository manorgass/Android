package com.korhacker.blacklistnumber;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by KorHacker on 2017-10-20.
 */

public class MyListAdapter extends BaseAdapter {
    ArrayList<MyContactInfo> mArrContacts;
    Boolean isVisibleCheckBox;
    Context mContext;

    public MyListAdapter(Context context, ArrayList<MyContactInfo> datas) {
        mContext = context;
        mArrContacts = datas;
        isVisibleCheckBox = false;
    }

    @Override
    public int getCount() {
        return mArrContacts.size();
    }

    public ArrayList<MyContactInfo> getContactsArray() {
        return mArrContacts;
    }

    public void setCheckBoxVisibility(boolean isVisible) {
        isVisibleCheckBox = isVisible;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater;
            inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.list_item, null);
        }
        final int position = i;
        final MyContactInfo contactInfo = mArrContacts.get(position);

        ((TextView) view.findViewById(R.id.tv_name_list_item)).setText(contactInfo.getName());
        ((TextView) view.findViewById(R.id.tv_number_list_item)).setText(contactInfo.getPhoneNum());

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_isSelected);
        /* flag에 따른 체크박스 노출 여부 설정 */
        if (isVisibleCheckBox)
            checkBox.setVisibility(View.VISIBLE);
        else
            checkBox.setVisibility(View.GONE);

        /* 연락처 객체의 체크 여부값에 따른 체크박스 체크 상태 변경 */
        checkBox.setChecked(contactInfo.isChecked());
        /* 체크박스 상태 변경 시 해당 변경값을 연락처 객체에 저장 */
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                contactInfo.setChecked(b);
                mArrContacts.set(position, contactInfo);
                //MyContactInfo tmp = mArrContacts.get(position);
            }
        });

        return view;
    }
}
