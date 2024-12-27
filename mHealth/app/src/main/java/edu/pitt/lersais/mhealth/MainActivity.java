package edu.pitt.lersais.mhealth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.pitt.lersais.mhealth.adapter.ExpandableListAdapter;
import edu.pitt.lersais.mhealth.adapter.GridListAdapter;
import edu.pitt.lersais.mhealth.model.ExpandedMenu;
import edu.pitt.lersais.mhealth.model.GridItem;
import edu.pitt.lersais.mhealth.util.DownloadImageTask;

import com.google.firebase.FirebaseApp;


/**
 * The MainActivity.
 *
 * @author Haobing Huang and Runhua Xu.
 */
public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private List<ExpandedMenu> mMenuList;
    private HashMap<ExpandedMenu, List<ExpandedMenu>> mMenuListChild;
    private ExpandableListAdapter mExpandableListAdaptor;
    private ExpandableListView expandableListView;

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private DatabaseReference mDatabase;
    private CircleImageView navHeaderImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        navigationView = findViewById(R.id.nav_view);  // Initialize NavigationView
        View navHeaderView = navigationView.getHeaderView(0);  // Get Header View
        navHeaderImageView = navHeaderView.findViewById(R.id.status_profile_photo);  // Set global ImageView
        currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            initializeToolbar();
            initializeDrawerMenu();
            setupGridView();
            loadProfileImageIntoNavHeader();
        }
    }

    private void loadProfileImageIntoNavHeader() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                    .child(user.getUid())
                    .child("profileImage");

            databaseReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().exists()) {
                    String base64Image = task.getResult().getValue(String.class);
                    if (base64Image != null) {
                        Log.d("MainActivity", "Retrieved Base64 image for user " + user.getUid());
                        Bitmap bitmap = base64ToBitmap(base64Image);
                        navHeaderImageView.setImageBitmap(bitmap);
                    } else {
                        Log.e("MainActivity", "Base64 image is null for user " + user.getUid());
                    }
                } else {
                    Log.e("MainActivity", "Failed to retrieve profile image for user " + user.getUid(), task.getException());
                }
            });
        }
    }


    private Bitmap base64ToBitmap(String base64Str) {
        byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private void moveToMedicalRecord() {
        // TODO: implement the function to move to your designed medical record activity view
        Intent intent = new Intent(MainActivity.this, MedicalRecordActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void moveToMedicalPrescription() {
        Intent intent = new Intent(this, MedicalOrderActivity.class);
        startActivity(intent);
    }


    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    private void initializeToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initializeDrawerMenu() {
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        CircleImageView profilePhotoImageView = navHeaderImageView;  // Use global ImageView
        TextView navHeaderUidTextView = navigationView.getHeaderView(0).findViewById(R.id.status_uid);
        TextView navHeaderEmailTextView = navigationView.getHeaderView(0).findViewById(R.id.status_email);

        if (currentUser != null) {
            navHeaderUidTextView.setText(currentUser.getDisplayName());
            navHeaderEmailTextView.setText(currentUser.getEmail());

            loadProfileImageIntoNavHeader();
        }

        prepareExpandableMenuData();

        expandableListView = findViewById(R.id.navigation_expandable_menu);
        expandableListView.setGroupIndicator(null);
        mExpandableListAdaptor = new ExpandableListAdapter(this, mMenuList, mMenuListChild, expandableListView);
        expandableListView.setAdapter(mExpandableListAdaptor);

        expandableListView.setOnGroupClickListener((parent, view, groupPosition, id) -> {
            ExpandedMenu selectModel = (ExpandedMenu) parent.getExpandableListAdapter().getGroup(groupPosition);
            if (selectModel.getMenuName() == R.string.expandable_menu_logout) {
                signOut();
                return true;
            }
            return false;
        });

        expandableListView.setOnGroupClickListener((expandableListView, view, groupPosition, l) -> {
            ExpandedMenu selectModel = (ExpandedMenu) expandableListView.getExpandableListAdapter().getGroup(groupPosition);

            if (selectModel.getMenuName() == R.string.expandable_menu_account) {
                // Handle Profile click
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                return true;
            } else if (selectModel.getMenuName() == R.string.expandable_menu_setting) { // Add this block for Settings
                // Handle Settings click
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                return true;
            } else if (selectModel.getMenuName() == R.string.expandable_menu_logout) {
                // Handle Logout click
                signOut();
                return true;
            }
            return false;
        });



        expandableListView.setOnChildClickListener((parent, view, groupPosition, childPosition, id) -> {
            ExpandedMenu selectedChild = (ExpandedMenu) expandableListView.getExpandableListAdapter().getChild(groupPosition, childPosition);

            if (selectedChild.getMenuName() == R.string.expandable_menu_medical_item1) { // Record Demo
                Intent intent = new Intent(MainActivity.this, MedicalRecordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                return true;
            } else if (selectedChild.getMenuName() == R.string.expandable_menu_medical_item2) { // Medical Order
                Intent intent = new Intent(MainActivity.this, MedicalOrderActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                return true;
            }
            return false;
        });

    }



    private void setupGridView() {
        GridView gridView = findViewById(R.id.content_grid_view);
        ArrayList<GridItem> mData = new ArrayList<>();
        mData.add(new GridItem(R.drawable.icon_medical_48, "Medical Record"));
        mData.add(new GridItem(R.drawable.icon_medical_48, "Medical Prescription"));
        mData.add(new GridItem(R.drawable.icon_medical_48, "Display Medical History")); // New item for Display Medical History


        BaseAdapter adapter = new GridListAdapter<GridItem>(mData, R.layout.grid_view_item) {
            @Override

            public void bindView(ViewHolder holder, GridItem obj) {
                holder.setImageResource(R.id.img_icon, obj.getiId());
                holder.setText(R.id.txt_icon, obj.getiName());
            }
        };

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener((adapterView, view, position, id) -> {
            if (position == 0) {
                moveToMedicalRecord();
            } else if (position == 1) {
                moveToMedicalPrescription();
            } else if (position == 2) {
                // New case for "Display Medical History"
                Intent intent = new Intent(MainActivity.this, DisplayMedicalHistoryActivity.class);
                startActivity(intent);
            }
        });
    }

    private void prepareExpandableMenuData() {
        mMenuList = new ArrayList<>();
        mMenuListChild = new HashMap<>();
        mMenuList.add(new ExpandedMenu(R.string.expandable_menu_account, R.drawable.menu_account_24));
        mMenuList.add(new ExpandedMenu(R.string.expandable_menu_medical, R.drawable.menu_medical_24));
        mMenuList.add(new ExpandedMenu(R.string.expandable_menu_setting, R.drawable.ic_menu_manage));
        mMenuList.add(new ExpandedMenu(R.string.expandable_menu_logout, R.drawable.menu_logout_24));

        List<ExpandedMenu> medicalMenuList = new ArrayList<>();
        medicalMenuList.add(new ExpandedMenu(R.string.expandable_menu_medical_item1, R.drawable.menu_medical_24));
        medicalMenuList.add(new ExpandedMenu(R.string.expandable_menu_medical_item2, R.drawable.menu_medical_24));
        mMenuListChild.put(mMenuList.get(1), medicalMenuList);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) { // Add this condition
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
