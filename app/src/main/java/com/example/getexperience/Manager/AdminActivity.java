package com.example.getexperience.Manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.getexperience.EventBus.ManagerFieldModelClick;
import com.example.getexperience.EventBus.ManagerFoundationClick;
import com.example.getexperience.EventBus.ManagerUnblockedFoundationClick;
import com.example.getexperience.EventBus.PassMassageActionClick;
import com.example.getexperience.Manager.ui.AddFieldFragment;
import com.example.getexperience.Manager.ui.BlockedFoundationDetailsFragment;
import com.example.getexperience.Manager.ui.BlockedFoundationFragment;
import com.example.getexperience.Manager.ui.FieldDetailsFragment;
import com.example.getexperience.Manager.ui.FieldsManagementFragment;
import com.example.getexperience.Manager.ui.FoundationManagementFragment;
import com.example.getexperience.Manager.ui.ManagerFoundationDetailsFragment;
import com.example.getexperience.OuterPages.LoginActivity;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.getexperience.R;
import com.example.getexperience.databinding.ActivityAdminBinding;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityAdminBinding binding;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        toolbar = binding.appBarAdmin.toolbar;
        toolbar.setTitle(R.string.foundation_management);
        setSupportActionBar(toolbar);

        drawer = binding.drawerLayout;
        navigationView = binding.navView;
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        Fragment selectedScreen = FoundationManagementFragment.createFor();
        showFragment(selectedScreen);
        toolbar.setTitle(R.string.foundation_management);
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_foundation_management) {
            Fragment selectedScreen = FoundationManagementFragment.createFor();
            showFragment(selectedScreen);
            toolbar.setTitle(R.string.foundation_management);
        } else if (id == R.id.nav_field_management) {
            Fragment selectedScreen = FieldsManagementFragment.createFor();
            showFragment(selectedScreen);
            toolbar.setTitle(R.string.fields_management);
        } else if (id == R.id.nav_blocked_foundation_management) {

            Fragment selectedScreen = BlockedFoundationFragment.createFor();
            showFragment(selectedScreen);
            toolbar.setTitle(R.string.blocked_foundation_management);

        }
        else if (id == R.id.nav_logout) {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Confirm Logout")
                    .setContentText("Do you really want to log out")
                    .setConfirmText("Yes")
                    .setConfirmClickListener(sDialog -> {
                        sDialog.dismiss();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    }).setCancelText("No")
                    .setCancelClickListener(sweetAlertDialog -> {
                        sweetAlertDialog.dismiss();
                    }).show();

        }

        DrawerLayout drawer = binding.drawerLayout;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment_content_admin, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onPassMessageSelected(PassMassageActionClick event) {

        if (event.getMsg().equals("DisplayFoundationManagement")) {
            toolbar.setTitle(R.string.foundation_management);
            Fragment selectedScreen = FoundationManagementFragment.createFor();
            showFragment(selectedScreen);

        } else if (event.getMsg().equals("DisplayBlockedFoundationManagement")) {

            toolbar.setTitle(R.string.blocked_foundation_management);
            Fragment selectedScreen = BlockedFoundationFragment.createFor();
            showFragment(selectedScreen);

        }


    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onFoundationManagerSelected(ManagerFoundationClick event) {
        if (event.isSuccess()) {
            toolbar.setTitle("Organization Details");
            Fragment selectedScreen = ManagerFoundationDetailsFragment.createFor(event.getFoundationModel());
            showFragment(selectedScreen);
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onBlockedFoundationManagerSelected(ManagerUnblockedFoundationClick event) {
        if (event.isSuccess()) {
            toolbar.setTitle("Blocked Organization Details");
            Fragment selectedScreen = BlockedFoundationDetailsFragment.createFor(event.getFoundationModel());
            showFragment(selectedScreen);
        }
    }

}