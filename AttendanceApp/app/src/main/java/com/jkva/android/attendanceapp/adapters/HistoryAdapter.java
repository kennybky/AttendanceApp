package com.jkva.android.attendanceapp.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jkva.android.attendanceapp.R;
import com.jkva.android.attendanceapp.databaseConnectivity.Contract;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ItemHolder> {

    private Cursor cursor;

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_student_data, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.bind(holder, position);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
    public void swapCursor(Cursor newCursor){
        if (cursor != null) cursor.close();
        cursor = newCursor;
        if (newCursor != null) {
            this.notifyDataSetChanged();
        }
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        TextView studentName;
        TextView className;
        public ItemHolder(View itemView) {
            super(itemView);
            studentName = (TextView) itemView.findViewById(R.id.studentName);
            className = (TextView) itemView.findViewById(R.id.className);
        }

        public void bind(ItemHolder holder, int position) {
            cursor.moveToPosition(position);
            String cName=cursor.getString(cursor.getColumnIndex(Contract.TABLE_ATTENDANCE.COLUMN_NAME_CLASS_NAME));
            String sName=cursor.getString(cursor.getColumnIndex(Contract.TABLE_ATTENDANCE.COLUMN_NAME_STUDENT_NAME));
            String d=cursor.getString(cursor.getColumnIndex(Contract.TABLE_ATTENDANCE.COLUMN_NAME_DATE));
            Log.d("MyAdapter",cName);
            Log.d("MyAdapter",sName);
            Log.d("MyAdapter",d+" ");
            className.setText(cName);
            studentName.setText(sName);
        }
    }
}
