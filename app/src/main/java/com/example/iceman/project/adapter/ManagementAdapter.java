package com.example.iceman.project.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.iceman.project.R;
import com.example.iceman.project.activity.ListTransactionsActivity;
import com.example.iceman.project.activity.MainActivity;
import com.example.iceman.project.activity.ManagementActivity;
import com.example.iceman.project.database.SQLiteDatabase;
import com.example.iceman.project.dialog.SimpleDialog;
import com.example.iceman.project.model.ItemCurrentBalance;
import com.example.iceman.project.utils.PreferenceManager;

import java.util.ArrayList;

/**
 * Created by iceman on 18/10/2016.
 */

public class ManagementAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<ItemCurrentBalance> mData;
    LayoutInflater mLayoutInflater;
    SQLiteDatabase mDatabase;
    PreferenceManager prefManager;

    public ManagementAdapter(Context mContext, ArrayList<ItemCurrentBalance> mData) {
        this.mContext = mContext;
        this.mData = mData;
        mDatabase = SQLiteDatabase.getInstance(mContext);
        mLayoutInflater = LayoutInflater.from(mContext);
        prefManager = new PreferenceManager(mContext);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_manage, null);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_type);
            viewHolder.tvMoney = (TextView) convertView.findViewById(R.id.tv_money_manage);
            viewHolder.btnDelete = (Button) convertView.findViewById(R.id.btn_delete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ItemCurrentBalance item = mData.get(position);
        viewHolder.tvName.setText(item.getName());
        viewHolder.tvMoney.setText(item.getMoney() + "");
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog(item);

            }
        });


        return convertView;
    }

    private static class ViewHolder {
        TextView tvName;
        TextView tvMoney;
        Button btnDelete;
    }

    public void confirmDialog(ItemCurrentBalance item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Cảnh báo");
        builder.setMessage("Bạn có chắc chắn muốn xóa " + item.getName() + " ?");
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDatabase.deleteRecord(SQLiteDatabase.TBL_CURRENT_BALANCE,
                        SQLiteDatabase.TBL_CB_COLUMN_ID, new String[]{item.getId() + ""});
                Intent intent = new Intent(MainActivity.ACTION_ADD_CB_SUCCESS);
                mContext.sendBroadcast(intent);
                mData.remove(item);
                notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }
}
