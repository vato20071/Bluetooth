package com.example.vato.bluetoothcontrol;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

/**
 * Created by vato on 12/10/16.
 */
public class FingerListAdapter extends ArrayAdapter<Finger>{

    private Context context;
    List<Finger> fingers;

    public FingerListAdapter(Context context, int resource, List<Finger> objects) {
        super(context, resource, objects);
        this.context = context;
        this.fingers = objects;
    }

    public class FingerViewHolder {
        TextView fingerName;
        EditText fingerValue;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FingerViewHolder holder;
        final View listView;
        if (convertView == null) {
            holder = new FingerViewHolder();
            listView = inflater.inflate(R.layout.list_item, null);
            holder.fingerName = (TextView) listView.findViewById(R.id.finger_view);
            holder.fingerValue = (EditText) listView.findViewById(R.id.finger_text);
            holder.fingerValue.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    updateItem(fingers.get(position), v);
                    return false;
                }
            });
            listView.setTag(holder);
        } else {
            listView = convertView;
            holder = (FingerViewHolder) listView.getTag();
        }

        String value = String.format(context.getResources().getString(R.string.finger_value),
                fingers.get(position).getId());
        holder.fingerName.setText(value);
        holder.fingerValue.setText("" + fingers.get(position).getValue());
        return listView;
    }

    @Override
    public int getCount() {
        return fingers.size();
    }

    private KeyEvent UnknownKey = new KeyEvent(KeyEvent.ACTION_DOWN,
            KeyEvent.KEYCODE_UNKNOWN);

    public class MyTextWatcher implements TextWatcher {

        private EditText editText;

        public MyTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            editText.dispatchKeyEvent(UnknownKey);
        }
    }

    protected void updateItem(Finger item, View v) {
        try {
            item.setValue(Integer.valueOf(((EditText) v).getText().toString()));
        } catch (Exception e) {

        }
    }
}
