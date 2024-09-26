package in.gov.cgg.alumni.activities;


import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import in.gov.cgg.alumni.Details;
import in.gov.cgg.alumni.GlobalDeclaration;
import in.gov.cgg.alumni.R;

public class OwnProfileActivity extends AppCompatActivity {
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    ImageView profile_image;
    AppBarLayout app_bar;
    NestedScrollView scrollView;
    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;
    private int mMaxScrollSize;
    boolean isShow = false;
    int scrollRange = -1;
    ImageView mProfileImage;
    ImageView backimage;
    TextView tv_name, tvdesg, tvdept, tvmobile, tvaddress, tvemail, tvdop, tv_service, tv_batch;
    private Animator mCurrentAnimator;
    Details bean;
    FloatingActionMenu fab_edit;
    com.github.clans.fab.FloatingActionButton fab_chaneg, fab_mpin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        findAllViews();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab_chaneg = findViewById(R.id.fab_change);
        fab_mpin = findViewById(R.id.fab_mpin);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);


        fab_mpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OwnProfileActivity.this, UpdatempinActivity.class);
                startActivity(intent);
            }
        });

        collapsingToolbarLayout = findViewById(R.id.collapsing_layout);
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);

        app_bar = findViewById(R.id.app_bar);
        app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @SuppressLint("RestrictedApi")
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
                    Animation animation = AnimationUtils.loadAnimation(OwnProfileActivity.this, R.anim.scale_down);
                    //  tv_desgn.startAnimation(animation);
                    tv_name.startAnimation(animation);
                    tv_name.setVisibility(View.GONE);
                    //  profile_image.startAnimation(animation);
                    profile_image.setVisibility(View.GONE);
                    //  fab_edit.setVisibility(View.GONE);


                } else if (isShow) {
                    Animation animation = AnimationUtils.loadAnimation(OwnProfileActivity.this, R.anim.scale_up);
                    tv_name.startAnimation(animation);
                    tv_name.setVisibility(View.VISIBLE);
                    profile_image.startAnimation(animation);
                    profile_image.setVisibility(View.VISIBLE);
                    isShow = false;
                    // fab_edit.setVisibility(View.VISIBLE);

                }
            }
        });


        fab_chaneg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OwnProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        scrollView = (NestedScrollView) findViewById(R.id.nest_scrollview);
        scrollView.setFillViewport(false);

        if (GlobalDeclaration.profileDetails != null) {

            bean = GlobalDeclaration.profileDetails;

            if ((bean.getProfilePicUrl().length() > 0)) {

//                Picasso.with(OwnProfileActivity.this)
//                        .load(bean.getProfilePicUrl())
//                        .error(R.color.colorPrimary).into(profile_image);
                Glide.with(this)
                        .applyDefaultRequestOptions(new RequestOptions()
                                .placeholder(R.drawable.loadingimg_red)
                                .error(R.drawable.user))
                        .load(bean.getProfilePicUrl())
                        .into(profile_image);

            } else {
                profile_image.setImageResource(R.drawable.noimage);
            }

            if ((bean.getFamilyPicUrl().length() > 0)) {

//                Picasso.with(OwnProfileActivity.this)
//                        .load(bean.getFamilyPicUrl())
//                        .error(R.color.colorPrimary).into(backimage);
                Glide.with(this)
                        .applyDefaultRequestOptions(new RequestOptions()
                                .placeholder(R.drawable.loadingimg_red)
                                .error(R.drawable.user))
                        .load(bean.getFamilyPicUrl())
                        .into(backimage);


            } else {
                backimage.setImageResource(R.color.colorPrimary);

            }

            tv_name.setText(bean.getName());
            tvdesg.setText(bean.getDesignation());
            tvdept.setText(bean.getDepartment());
            tvmobile.setText("+" + bean.getCountryCode() + bean.getMobileNo());
            tvemail.setText(bean.getEmail());
            tvaddress.setText(bean.getOfficeAddress());
            tvdop.setText(bean.getDateOfPosting());
            tv_service.setText(bean.getService());
            tv_batch.setText(bean.getBatchno());
        }
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Code to show image in full screen:
                new PhotoFullPopupWindow(OwnProfileActivity.this, R.layout.pop_up_fill_window, view, bean.getProfilePicUrl(), null);

            }
        });
        backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Code to show image in full screen:
                new PhotoFullPopupWindow(OwnProfileActivity.this, R.layout.pop_up_fill_window, view, bean.getFamilyPicUrl(), null);

            }
        });
    }

    private void findAllViews() {
        profile_image = findViewById(R.id.profile_image);
        backimage = findViewById(R.id.backimage);
        tv_name = findViewById(R.id.tv_mname);
        tvdesg = findViewById(R.id.tv_desg);
        tvdept = findViewById(R.id.tv_dept);
        tvmobile = findViewById(R.id.tv_mobile);
        tvemail = findViewById(R.id.tv_emailname);
        tvaddress = findViewById(R.id.tv_address);
        tvdop = findViewById(R.id.tv_dop);
        tv_service = findViewById(R.id.tv_service);
        tv_batch = findViewById(R.id.tv_batch);
        fab_edit = findViewById(R.id.fab_edit);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_profile, menu);
//        for (int i = 0; i < menu.size(); i++) {
//            Drawable drawable = menu.getItem(i).getIcon();
//            if (drawable != null) {
//                drawable.mutate();
//                drawable.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//            }
//        }
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.edit:
//                Intent intent = new Intent(OwnProfileActivity.this, EditProfileActivity.class);
//                startActivity(intent);
//                return true;
//            default:
//                return super.onContextItemSelected(item);
//        }
//    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(OwnProfileActivity.this, DashboardActivity.class));
    }
}

