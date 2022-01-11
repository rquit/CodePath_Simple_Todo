package com.example.codepath_simple_todo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Objects;

public class EditActivity extends AppCompatActivity {

    EditText updateListItem;
    Button updateListItemSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        updateListItem = findViewById(R.id.updateListItem);
        updateListItemSubmit = findViewById(R.id.updateListItemSubmit);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Edit Item");

        updateListItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));

        updateListItemSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create an intent with results
                Intent dataShell = new Intent();
                // pass result
                int editedItemPosition = getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION);
                String editedString = updateListItem.getText().toString();
                dataShell.putExtra(MainActivity.KEY_ITEM_TEXT, editedString);
                dataShell.putExtra(MainActivity.KEY_ITEM_POSITION, editedItemPosition);
                // set the result
                setResult(RESULT_OK, dataShell);
                // finish activity
                finish();
            }
        });
    }
}