package com.example.codepath_simple_todo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.io.FileUtils;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 1;

    List<String> items;

    Button addItemButton;
    EditText editTextItem;
    RecyclerView recyclerViewItems;
    ItemAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addItemButton = findViewById(R.id.addItemButton);
        editTextItem = findViewById(R.id.editTextItem);
        recyclerViewItems = findViewById(R.id.recyclerViewItems);

        loadItems();

        ItemAdapter.OnLongClickListener onLongClickListener = new ItemAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                // delete the item from the model
                items.remove(position);
                // notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                saveItems();
                Toast.makeText(getApplicationContext(), "Item Removed!", Toast.LENGTH_SHORT).show();
            }
        };

        ItemAdapter.OnClickListener onClickListener = new ItemAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                // create the activity
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                // pass data being edited
                i.putExtra(KEY_ITEM_TEXT, items.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);
                // display
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };

        itemsAdapter = new ItemAdapter(items, onLongClickListener, onClickListener);
        recyclerViewItems.setAdapter(itemsAdapter);
        recyclerViewItems.setLayoutManager(new LinearLayoutManager(this));

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String todoItem = editTextItem.getText().toString();
                // add item to the model
                items.add(todoItem);
                // notify adapter that an item is inserted
                itemsAdapter.notifyItemInserted(items.size()-1);
                editTextItem.setText("");
                saveItems();
                Toast.makeText(getApplicationContext(), "Item Added!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    // handle result of edit
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            String itemText = Objects.requireNonNull(data).getStringExtra(KEY_ITEM_TEXT);
            int toEditPosition = data.getExtras().getInt(KEY_ITEM_POSITION);
            items.set(toEditPosition, itemText);
            itemsAdapter.notifyItemChanged(toEditPosition);
            saveItems();
        } else {
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }

    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    // load items by reading every line of data.txt
    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items to recyclerView", e);
            items = new ArrayList<>();
        }
    }

    // save items by writing them to data file
    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items from recyclerView", e);
        }
    }
}