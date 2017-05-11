package com.example.vasuchand.lbb_collagemaker;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Config;
import android.util.Log;
import android.view.View;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import  android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;




public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS = 007;
    private static Model data;
    public static List<Model> imagelist= new ArrayList<>();
    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    ContentResolver contentResolver;
    Context context = MainActivity.this;
    ImageView layout1image1,layout1image2,layout1image3;
    ImageView layout2image1,layout2image2,layout2image3;
    static ImageView image;
    LinearLayout layout1;
    HashMap<String,List<Integer>> log= new HashMap<String,List<Integer>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        /*
          Defining layouts
         */
        layout1image1 = (ImageView)findViewById(R.id.layout1image1);
        layout1image2 = (ImageView)findViewById(R.id.layout1image2);
        layout1image3 = (ImageView)findViewById(R.id.layout1image3);

        layout2image1 = (ImageView)findViewById(R.id.layout2image1);
        layout2image2 = (ImageView)findViewById(R.id.layout2image2);
        layout2image3 = (ImageView)findViewById(R.id.layout2image3);

        image = (ImageView)findViewById(R.id.image);

        layout1 = (LinearLayout)findViewById(R.id.collagelayout1);

        /*
          Recyclerview
         */

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),4);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);


        /*

          Permission to view gallery

         */



        if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

                getpermission();


        }else {
            Log.e("MainActivity","Permission Taken into the account ");
            Loadimages();
            //System.out.println(getAllShownImagesPath(this));
        }

        /*
           adapting gallery images in view

         */
        RecyclerviewListener();
        adapter = new Model_adapter(MainActivity.this, imagelist);
        recyclerView.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);


        /*
          layout clicked
         */

        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFrameLayout(layout1);
            }
        });

    }



    public void  Loadimages() {
        Uri uri;
        Cursor cursor;
        int column_index_data,column_index_id,column_width,column_height;


        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME
                ,MediaStore.Images.Media._ID, "width", "height"};

        contentResolver = getContentResolver();
        cursor = contentResolver.query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_height = cursor.getColumnIndexOrThrow("height");
        column_width = cursor.getColumnIndexOrThrow("width");



        column_index_id = cursor.getColumnIndex(
                MediaStore.Images.Media._ID );


        if ( cursor != null && cursor.getCount() > 0 ) {

            String imageId="";
            String imagepath="";
            String height ="";
            String width= "";

            Log.i("DeviceImageManager", " query count=" + cursor.getCount());

            int counter =0;
            while (cursor.moveToNext()) {

                imagepath = cursor.getString(column_index_data);
                imageId = cursor.getString(column_index_id);
                width = cursor.getString(column_width);
                height = cursor.getString(column_height);
                System.out.println("height "+height + " "+ "width " +width);
                if(width!=null || height!=null)
                    data = new Model(imageId,imagepath,Integer.parseInt(height),Integer.parseInt(width),counter);
                counter++;
                imagelist.add(data);
            }
        }

    }


    public void RecyclerviewListener()
    {
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        data = imagelist.get(position);
                        int len=log.size();// how many images are selected

                        ArrayList<Integer> list1= new ArrayList<Integer>();

                        list1.add(null);
                        list1.add(null);




                       /*
                         first layout
                        */

                        System.out.println("********          *****************");
                        System.out.println("MapStart : " + log);

                        // check if selected image is already into layouts or  not ?
                        if(log.containsKey(data.getImageid()))
                        {
                            int value = log.get(data.getImageid()).get(0);

                            // value is for layout number ,on which layout image is  1,2,3 ?
                            if(value==1)
                            {

                                Map<String, List<Integer>> map = log;


                                // data model key from tracking of other layouts and update them when switching
                                Model data1 =null;
                                Model data2 =null;
                                String key1="";
                                String key2 ="";
                                int position1 = 0;
                                int position2 = 0;
                                //


                                for (Map.Entry<String, List<Integer>> entry : map.entrySet())
                                {

                                    int a = entry.getValue().get(0);
                                    if(a==2)
                                    {
                                        data1 = imagelist.get(entry.getValue().get(1));
                                        key1 = entry.getKey();
                                        position1 = entry.getValue().get(1);
                                    }
                                    else if(a==3)
                                    {
                                        data2 = imagelist.get(entry.getValue().get(1));
                                        key2 = entry.getKey();
                                        position2 = entry.getValue().get(1);
                                    }

                                }

                                if(data1!=null && map.size()>1) {

                                    Glide.with(context)
                                            .load(data1.getImageurl())
                                            .into(layout1image1);




                                    list1.set(0,1);
                                    list1.set(1,position1);

                                    if(log.containsKey(key1))
                                        log.put(key1,list1);



                                    // check wether only two images in pane and upper one gets selected in this case third imageview
                                    //should remain defualt

                                    LinearLayout.LayoutParams param1 = Getweightzero();
                                    layout1image2.setLayoutParams(param1);


                                    /*

                                        Second Collage layout

                                     */

                                    layout2image1.setLayoutParams(param1);




                                    if(data2!=null && map.size()>2)
                                    {
                                        Glide.with(context)
                                                .load(data2.getImageurl())
                                                .into(layout1image2);


                                        Glide.with(context)
                                                .load(data2.getImageurl())
                                                .into(layout2image1);

                                        Glide.with(context)
                                                .load(data1.getImageurl())
                                                .into(layout2image3);





                                        list1.set(0,2);
                                        list1.set(1,position2);
                                        if(log.containsKey(key2))
                                            log.put(key2,list1);
                                    }
                                    else if(map.size()==2)
                                    {
                                        Glide.with(context)
                                                .load(data1.getImageurl())
                                                .into(layout1image2);


                                        Glide.with(context)
                                                .load(data1.getImageurl())
                                                .into(layout2image1);
                                        Glide.with(context)
                                                .load(data1.getImageurl())
                                                .into(layout2image3);
                                    }
                                    else
                                    {
                                        Glide.with(context)
                                                .load(R.drawable.defualt)
                                                .into(layout1image2);


                                        layout2image1.setLayoutParams(param1);
                                        Glide.with(context)
                                                .load(R.drawable.defualt)
                                                .into(layout1image1);


                                    }
                                }
                                else {


                                    Glide.with(context)
                                            .load(R.drawable.defualt)
                                            .into(layout1image1);
                                    Glide.with(context)
                                            .load(R.drawable.defualt)
                                            .into(layout1image2);

                                    Glide.with(context)
                                            .load(R.drawable.defualt)
                                            .into(layout2image1);

                                    Glide.with(context)
                                            .load(R.drawable.defualt)
                                            .into(layout2image2);

                                    Glide.with(context)
                                            .load(R.drawable.defualt)
                                            .into(layout2image3);
                                }



                                log.remove(data.getImageid());



                            }
                            else if(value==2)
                            {

                                Map<String, List<Integer>> map = log;

                                Model data1 =null;
                                Model data2 = null;
                                String key1 ="";
                                String key2 ="";
                                int position1 = 0;
                                int position2 =0;

                                for (Map.Entry<String, List<Integer>> entry : map.entrySet())
                                {
                                    int a = entry.getValue().get(0);
                                    if(a==1)
                                    {
                                        data1 = imagelist.get(entry.getValue().get(1));
                                        key1 = entry.getKey();
                                        position1 = entry.getValue().get(1);
                                    }
                                    else if(a==3)
                                    {
                                        data2 = imagelist.get(entry.getValue().get(1));
                                        key2 = entry.getKey();
                                        position2 = entry.getValue().get(1);
                                    }

                                }

                                LinearLayout.LayoutParams param1 = Getweightzero();
                                layout1image2.setLayoutParams(param1);

                                /*

                                   Second layout

                                 */
                                layout2image1.setLayoutParams(param1);

                                if(map.size()==2 && data1!=null)
                                {


                                    list1.set(0,1);
                                    list1.set(1,position1);
                                    log.put(key1,list1);

                                    Glide.with(context)
                                            .load(data1.getImageurl())
                                            .into(layout1image2);


                                    /*
                                      second collage
                                     */
                                    Glide.with(context)
                                            .load(data1.getImageurl())
                                            .into(layout2image3);
                                    Glide.with(context)
                                            .load(data1.getImageurl())
                                            .into(layout2image1);


                                }
                                else if(map.size()==3 && data2!=null)
                                {

                                    list1.set(0,2);
                                    list1.set(1,position2);
                                    log.put(key2,list1);
                                    Glide.with(context)
                                            .load(data2.getImageurl())
                                            .into(layout1image2);

                                    Glide.with(context)
                                            .load(data1.getImageurl())
                                            .into(layout2image3);

                                    Glide.with(context)
                                            .load(data2.getImageurl())
                                            .into(layout2image1);

                                }


                                log.remove(data.getImageid());


                            }// now three pane onwards query
                            else
                            {


                                Model data2 = null;

                                List<Model> datalist = getdata();
                                data2 = datalist.get(2);

                                LinearLayout.LayoutParams param1 = Getweightzero();
                                layout1image2.setLayoutParams(param1);


                                /*
                                 Second layout
                                 */



                                layout2image1.setLayoutParams(param1);

                                if(log.size()>2)
                                {
                                    Glide.with(context)
                                            .load(data2.getImageurl())
                                            .into(layout2image1);
                                }

                                data2 = datalist.get(1);

                                Glide.with(context)
                                        .load(data2.getImageurl())
                                        .into(layout2image3);


                                log.remove(data.getImageid());
                            }
                        }
                        /*
                            Adding Images to layout

                         */

                        else if(len==0)
                        {




                            list1.set(0,1);
                            list1.set(1,position);

                            Glide.with(context)
                                    .load(data.getImageurl())
                                    .into(layout1image1);
                            log.put(data.getImageid(),list1);


                            //First time click on image set both images same;
                            LinearLayout.LayoutParams param = Getweightzero();
                            layout1image2.setLayoutParams(param);

                            Glide.with(context)
                                    .load(data.getImageurl())
                                    .into(layout1image2);

                            /*

                               Second Collage Images Adding

                             */


                            layout2image1.setLayoutParams(param);

                            Glide.with(context)
                                    .load(data.getImageurl())
                                    .into(layout2image1);

                            Glide.with(context)
                                    .load(data.getImageurl())
                                    .into(layout2image3);


                        }

                        else if(len==1)
                        {
                            System.out.println("hey 2 is called");


                            list1.set(0,2);
                            list1.set(1,position);

                            LinearLayout.LayoutParams param = Getweightzero();
                            layout1image2.setLayoutParams(param);

                            Glide.with(context)
                                    .load(data.getImageurl())
                                    .into(layout1image2);

                            log.put(data.getImageid(),list1);


                            /*

                              Second Collage

                             */



                            layout2image1.setLayoutParams(param);

                            Glide.with(context)
                                    .load(data.getImageurl())
                                    .into(layout2image1);



                        }

                        else if(len==2)
                        {
                            System.out.println("hey 3 is called");
                            list1.set(0,3);
                            list1.set(1,position);

                            LinearLayout.LayoutParams param = Getweightone();
                            layout1image2.setLayoutParams(param);
                            Glide.with(context)
                                    .load(data.getImageurl())
                                    .into(layout1image3);


                            /*

                              Second Collage

                             */




                            List<Model> datalist = getdata();
                            Model data2 = datalist.get(2);
                            Model data3 = datalist.get(1);

                            LinearLayout.LayoutParams param2 = Getweightone();
                            layout2image1.setLayoutParams(param2);

                            Glide.with(context)
                                    .load(data.getImageurl())
                                    .into(layout2image3);


                            if(data2!=null)
                                Glide.with(context)
                                        .load(data2.getImageurl())
                                        .into(layout2image2);

                            if(data3!=null)
                                Glide.with(context)
                                        .load(data3.getImageurl())
                                        .into(layout2image1);


                            log.put(data.getImageid(),list1);

                        }
                        else if(len>2)
                        {
                            Toast.makeText(context, "Can't Select More than 3 Images !", Toast.LENGTH_SHORT).show();
                        }


                        System.out.println("*************************");
                        System.out.println("MapEnd : " + log);



                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );


    }


    public void saveFrameLayout(LinearLayout linearLayout) {



        /*
          canvas
         */

        Bitmap bitmap = Bitmap.createBitmap(linearLayout.getWidth(), linearLayout.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = linearLayout.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {

        }
        linearLayout.draw(canvas);

        image.setImageBitmap(bitmap);


    }



    public List<Model> getdata()
    {
        Map<String, List<Integer>> map = log;

        List<Model>  datalist = new ArrayList<Model>();

        Model data = null;

        datalist.add(data);
        datalist.add(data);
        datalist.add(data);
        datalist.add(data);


        for (Map.Entry<String, List<Integer>> entry : map.entrySet())
        {
            int a = entry.getValue().get(0);

            System.out.println(" tada "+ a );
            if(a==1)
            {
                data = imagelist.get(entry.getValue().get(1));
                datalist.set(1,data);
            }
            if(a==2)
            {
                data = imagelist.get(entry.getValue().get(1));
                datalist.set(2,data);
            }
            if(a==3)
            {
                data = imagelist.get(entry.getValue().get(1));
                datalist.set(3,data);
            }

        }



        return datalist;
    }

    public LinearLayout.LayoutParams Getweightzero()
    {
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                0.0f
        );
        return  param;
    }

    public LinearLayout.LayoutParams Getweightone()
    {
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                0.5f
        );
        return  param;
    }



    public void getpermission()
    {
        if ((ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE))) {



            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Permission is to read your files")
                    .setTitle("LBB -Permission");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE} , REQUEST_PERMISSIONS);

                }
            });

        }else
        {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE} , REQUEST_PERMISSIONS);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                        //System.out.println(permissions[i] + " " +grantResults[i]);

                    } else {
                        Toast.makeText(MainActivity.this, "The app was not allowed to read or write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }
}
