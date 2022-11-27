package com.dipesh.pdf_viewer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView iv_pdf;
    public static ArrayList<File> filelist = new ArrayList<>();
    PDFAdapter obj_adapter;
    public static int REQUEST_PERMISSION = 1;
    boolean boolean_permission;
    File dir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv_pdf = (ListView)findViewById(R.id.listview_pdf);

        dir = new File(Environment.getExternalStorageDirectory().toString());

        permission_fn();

        iv_pdf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(),viewPDF.class);
                intent.putExtra("position",i);
                startActivity(intent);
            }
        });
    }

    private void permission_fn()
    {
        if((ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
        {
            if((ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)))
            {

            }
            else
            {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_PERMISSION);
            }
        }
        else
        {
            boolean_permission = true;
            getFile(dir);
            obj_adapter = new PDFAdapter(getApplicationContext(),filelist);
            iv_pdf.setAdapter(obj_adapter);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_PERMISSION)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                boolean_permission = true;
                getFile(dir);
                obj_adapter = new PDFAdapter(getApplicationContext(),filelist);
                iv_pdf.setAdapter(obj_adapter);
            }
            else
            {
                Toast.makeText(this,"Please Allow Permissions",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public ArrayList<File> getFile(File dir)
    {
        File[] listFile = dir.listFiles();

        if(listFile!=null && listFile.length>0)
        {
            for(int i=0;i<listFile.length;i++)
            {
                if(listFile[i].isDirectory())
                {
                    getFile(listFile[i]);
                }
                else
                {
                    boolean booleanpdf = false;
                    if(listFile[i].getName().endsWith(".pdf"))
                    {
                        for(int j=0;j<filelist.size();j++)
                        {
                            if(filelist.get(j).getName().equals(listFile[i].getName()))
                            {
                                booleanpdf = true;
                            }
                            else
                            {

                            }
                        }
                        if(booleanpdf)
                        {
                            booleanpdf = false;
                        }
                        else
                        {
                            filelist.add(listFile[i]);
                        }
                    }
                }
            }
        }
      return filelist;
    }

    //method for back button
    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Are You Sure?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
