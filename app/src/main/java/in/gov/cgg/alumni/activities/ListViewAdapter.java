package in.gov.cgg.alumni.activities;


import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.gov.cgg.alumni.Details;
import in.gov.cgg.alumni.GlobalDeclaration;
import in.gov.cgg.alumni.R;

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.MyViewHolder> {

    Context context;
    private List<Details> modelDataList;
    ArrayList<Details> data_dashbord;
    BottomSheetDialog dialog;
    Details details;
    Animator mCurrentAnimator;
    String office;
    NestedScrollView scrollView;


    public ListViewAdapter(Context context, List<Details> ModelDatas) {
        this.context = context;
        this.modelDataList = ModelDatas;
        // data_dashbord = sroSubDataLists;
        this.data_dashbord = new ArrayList<Details>();
        this.data_dashbord.addAll(modelDataList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.list_item, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

        details = data_dashbord.get(i);
        myViewHolder.tv_name.setText(data_dashbord.get(i).getName());
        myViewHolder.tv_desg.setText(data_dashbord.get(i).getDesignation());
        myViewHolder.mobile.setText(data_dashbord.get(i).getMobileNo());
        // myViewHolder.tv_dept.setText(data_dashbord.get(i).getDepartment());
        myViewHolder.tv_city.setText(data_dashbord.get(i).getDepartment()+" "+data_dashbord.get(i).getCity());



        if (!data_dashbord.get(i).getProfilePicUrl().isEmpty()) {

//            Bitmap bitmapImage = BitmapFactory.decodeFile(data_dashbord.get(i).getProfilePicUrl());
//            int nh = (int) ( bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()) );
//            Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);
//            myViewHolder.circleImageView.setImageBitmap(scaled);


            Glide.with(context)
                    .applyDefaultRequestOptions(new RequestOptions()
                            .placeholder(R.drawable.loadingimg_red)
                            .error(R.drawable.user))
                    .load(data_dashbord.get(i).getProfilePicUrl())
                    .into(myViewHolder.circleImageView);

        } else {
            myViewHolder.circleImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.noimage));
        }

        myViewHolder.root_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showDetails(i);
                showDetail(i);
            }
        });
    }

    private void showDetail(final int i) {

        final View view = LayoutInflater.from(context).inflate(R.layout.activity_testprofile, null);

        final BottomSheetDialog dialog = new BottomSheetDialog(context);

        dialog.setContentView(view);

        TextView tv_call, tv_email, tv_map, tv_text, name, desg, dept, mobile, email,
                tv_city,address, dop, tv_service, tv_share,tv_batch;

        final ImageView profile = view.findViewById(R.id.profile_image);
        ImageView backimage = view.findViewById(R.id.backimage);
        ImageView back_arrow = view.findViewById(R.id.back_arrow);
        name = view.findViewById(R.id.tv_mname);
        desg = view.findViewById(R.id.tv_desg);
        dept = view.findViewById(R.id.tv_dept);
        mobile = view.findViewById(R.id.tv_mobile);
        email = view.findViewById(R.id.tv_emailname);
        address = view.findViewById(R.id.tv_address);
        dop = view.findViewById(R.id.tv_dop);
        tv_call = view.findViewById(R.id.tv_call);
        tv_text = view.findViewById(R.id.tv_text);
        tv_map = view.findViewById(R.id.tv_map);
        tv_email = view.findViewById(R.id.tv_email);
        tv_service = view.findViewById(R.id.tv_service);
        tv_share = view.findViewById(R.id.tv_share);
        tv_batch = view.findViewById(R.id.tv_batch);
        tv_city = view.findViewById(R.id.tv_city);
        scrollView = view.findViewById(R.id.scroll);

        tv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //print();
                shareImage();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Code to show image in full screen:
                new PhotoFullPopupWindow(context, R.layout.pop_up_fill_window, view, data_dashbord.get(i).getProfilePicUrl(), null);

            }
        });

        backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Code to show image in full screen:
                new PhotoFullPopupWindow(context, R.layout.pop_up_fill_window, view, data_dashbord.get(i).getFamilyPicUrl(), null);

            }
        });
        if (data_dashbord != null) {

            if (data_dashbord.get(i).getProfilePicUrl().length()>0) {
//                Picasso.with(context)
//                        .load(data_dashbord.get(i).getProfilePicUrl())
//                        .error(R.drawable.noimage).into(profile);
                Glide.with(context)
                        .applyDefaultRequestOptions(new RequestOptions()
                                .placeholder(R.drawable.loadingimg_red)
                                .error(R.drawable.user))
                        .load(data_dashbord.get(i).getProfilePicUrl())
                        .into(profile);

            } else {
                profile.setImageDrawable(context.getResources().getDrawable(R.drawable.user));

            }
            if (data_dashbord.get(i).getFamilyPicUrl().length()>0) {

//                Picasso.with(context)
//                        .load(data_dashbord.get(i).getFamilyPicUrl())
//                        .error(R.color.colorPrimary).into(backimage);
                Glide.with(context)
                        .applyDefaultRequestOptions(new RequestOptions()
                                .placeholder(R.drawable.loadingimg_red)
                                .error(R.drawable.user))
                        .load(data_dashbord.get(i).getFamilyPicUrl())
                        .into(backimage);


            } else {
                backimage.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));

            }

            if(!data_dashbord.get(i).getEmail().isEmpty())
            {
                tv_email.setVisibility(View.VISIBLE);
            }

            if(!data_dashbord.get(i).getOfficeAddress().isEmpty())
            {
                tv_map.setVisibility(View.VISIBLE);
            }

            name.setText(data_dashbord.get(i).getName());
            desg.setText(data_dashbord.get(i).getDesignation());
            dept.setText(data_dashbord.get(i).getDepartment());
            mobile.setText("+"+data_dashbord.get(i).getCountryCode()+data_dashbord.get(i).getMobileNo());
            email.setText(data_dashbord.get(i).getEmail());
            tv_service.setText(data_dashbord.get(i).getService());
            address.setText(data_dashbord.get(i).getOfficeAddress());
            dop.setText(data_dashbord.get(i).getDateOfPosting());
            tv_batch.setText(data_dashbord.get(i).getBatchno());
            tv_city.setText(data_dashbord.get(i).getCity());
            office = data_dashbord.get(i).getOfficeAddress();
        }
        dialog.show();

        tv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data_dashbord.get(i).getMobileNo().trim() != null && !data_dashbord.get(i).getMobileNo().trim().equals("")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL,
                            Uri.fromParts("tel", data_dashbord.get(i).getMobileNo(), null));
                    context.startActivity(intent);
                } else {
                    // Toast.makeText(context,"empty",Toast.LENGTH_SHORT).show();
                }
            }
        });

        tv_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data_dashbord.get(i).getMobileNo() != null && !data_dashbord.get(i).getMobileNo().trim().equals("")) {

                    context.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.fromParts("sms", data_dashbord.get(i).getMobileNo().trim(), null)));
                } else {

                }
            }
        });

        tv_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data_dashbord.get(i).getMobileNo() != null && !data_dashbord.get(i).getMobileNo().trim().equals("")) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + data_dashbord.get(i).getEmail()));
                        context.startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(context, "Sorry...You don't have any mail app", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                } else {

                }
            }
        });
        tv_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i=new Intent(context,MapsActivity.class);
//                i.putExtra("mid",i);
//                i.putExtra("loc",office);
//                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                context.startActivity(i);
                context.startActivity(new Intent(context, MapsActivity.class).
                        putExtra("mid", i).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                GlobalDeclaration.details=data_dashbord.get(i);

            }
        });

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
    }


    private void shareImage() {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Loading...");
        dialog.show();

        Bitmap bitmap1 = getBitmapFromView(scrollView, scrollView.getChildAt(0).getHeight(),
                scrollView.getChildAt(0).getWidth());
        try {
            File cachePath = new File(context.getCacheDir(), "images");
            cachePath.mkdirs(); // don't forget to make the directory
            FileOutputStream stream = new FileOutputStream(cachePath + "/image.png");
            // overwrites this image every time
            bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.close();
            dialog.dismiss();

        } catch (IOException e) {
            e.printStackTrace();
        }
        File imagePath = new File(context.getCacheDir(), "images");
        File newFile = new File(imagePath, "image.png");
        Uri contentUri = FileProvider.getUriForFile(context.getApplicationContext(),
                ".fileprovider",
                newFile);
        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, context.getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.setType("image/png");
            context.startActivity(Intent.createChooser(shareIntent, "Share via"));
        }
    }


    @Override
    public int getItemCount() {
        return (null != data_dashbord ? data_dashbord.size() : 0);
    }

    public void filter(String newText) {
        Log.e("Searchletter", newText);
        newText = newText.toLowerCase(Locale.getDefault());
        data_dashbord.clear();
        if (newText.length() == 0) {
            data_dashbord.addAll(modelDataList);
        } else {
            for (Details wp : modelDataList) {

                if (wp.getName().toLowerCase(Locale.getDefault()).contains(newText.toLowerCase())

                        || wp.getDesignation().toLowerCase(Locale.getDefault()).contains(newText.toLowerCase())
                        || wp.getDepartment().toLowerCase(Locale.getDefault()).contains(newText.toLowerCase())
                        || wp.getDesignation().toLowerCase(Locale.getDefault()).contains(newText.toLowerCase())
                        || wp.getCity().toLowerCase(Locale.getDefault()).contains(newText.toLowerCase())
                        || wp.getMobileNo().contains(newText.toLowerCase())
                        ) {
                    data_dashbord.add(wp);
                }
            }

        }

        notifyDataSetChanged();
    }

    //create bitmap from the view
    private Bitmap getBitmapFromView(View view, int height, int width) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return bitmap;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name, tv_desg, tv_dept, tv_city,mobile;
        ImageView circleImageView;
        RelativeLayout root_view;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_desg = itemView.findViewById(R.id.tv_desg);
            // tv_dept = itemView.findViewById(R.id.tv_dept);
            tv_city = itemView.findViewById(R.id.tv_city);
            circleImageView = itemView.findViewById(R.id.profile);
            root_view = itemView.findViewById(R.id.root_view);
            mobile = itemView.findViewById(R.id.mobile);
        }
    }


}
