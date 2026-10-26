package com.example.weekmidexam;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText etName, etStudentId;
    private CheckBox cbEnrolled;
    private Button btnAdd, btnEdit, btnDelete, btnView;
    private ListView lvStudents;

    private DBHelper db;

    private final ArrayList<String[]> students = new ArrayList<>();
    private final ArrayList<String> displayList = new ArrayList<>();
    private ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etStudentId = findViewById(R.id.etStudentId);
        cbEnrolled = findViewById(R.id.cbEnrolled);
        btnAdd = findViewById(R.id.btnAdd);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnView = findViewById(R.id.btnView);
        lvStudents = findViewById(R.id.lvStudents);

        db = new DBHelper(this);

        listAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, displayList);
        lvStudents.setAdapter(listAdapter);

        lvStudents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] s = students.get(position);
                etStudentId.setText(s[0]);
                etName.setText(s[1]);
                cbEnrolled.setChecked(s[2].equals("1"));
            }
        });

        btnAdd.setOnClickListener(v -> addStudent());
        btnEdit.setOnClickListener(v -> editStudent());
        btnDelete.setOnClickListener(v -> deleteStudent());
        btnView.setOnClickListener(v -> loadStudents());

        loadStudents();
    }

    private void addStudent() {
        String name = etName.getText().toString().trim();
        String id = etStudentId.getText().toString().trim();
        boolean enrolled = cbEnrolled.isChecked();

        if (name.isEmpty() || id.isEmpty()) {
            Toast.makeText(this, "Please fill in name and student ID", Toast.LENGTH_SHORT).show();
            return;
        }
        if (db.addStudent(id, name, enrolled)) {
            Toast.makeText(this, "Student added", Toast.LENGTH_SHORT).show();
            clearForm();
            loadStudents();
        } else {
            Toast.makeText(this, "Failed: student ID already exists", Toast.LENGTH_SHORT).show();
        }
    }

    private void editStudent() {
        String name = etName.getText().toString().trim();
        String id = etStudentId.getText().toString().trim();
        boolean enrolled = cbEnrolled.isChecked();

        if (name.isEmpty() || id.isEmpty()) {
            Toast.makeText(this, "Select a student or enter the student ID", Toast.LENGTH_SHORT).show();
            return;
        }
        if (db.updateStudent(id, name, enrolled)) {
            Toast.makeText(this, "Student updated", Toast.LENGTH_SHORT).show();
            clearForm();
            loadStudents();
        } else {
            Toast.makeText(this, "Student ID not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteStudent() {
        String id = etStudentId.getText().toString().trim();
        if (id.isEmpty()) {
            Toast.makeText(this, "Select a student or enter the student ID", Toast.LENGTH_SHORT).show();
            return;
        }
        if (db.deleteStudent(id)) {
            Toast.makeText(this, "Student deleted", Toast.LENGTH_SHORT).show();
            clearForm();
            loadStudents();
        } else {
            Toast.makeText(this, "Student ID not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadStudents() {
        students.clear();
        displayList.clear();

        Cursor c = db.getAllStudents();
        while (c.moveToNext()) {
            String id = c.getString(c.getColumnIndexOrThrow(DBHelper.COL_ID));
            String name = c.getString(c.getColumnIndexOrThrow(DBHelper.COL_NAME));
            int enrolled = c.getInt(c.getColumnIndexOrThrow(DBHelper.COL_ENROLLED));
            students.add(new String[]{id, name, String.valueOf(enrolled)});
            displayList.add(name + "\nID: " + id + "  |  Enrolled: " + (enrolled == 1 ? "Yes" : "No"));
        }
        c.close();
        listAdapter.notifyDataSetChanged();
    }

    private void clearForm() {
        etName.setText("");
        etStudentId.setText("");
        cbEnrolled.setChecked(false);
        etName.requestFocus();
    }
}