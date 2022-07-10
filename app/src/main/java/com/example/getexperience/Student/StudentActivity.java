package com.example.getexperience.Student;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.getexperience.EventBus.ManagerFieldModelClick;
import com.example.getexperience.EventBus.ManagerFoundationClick;
import com.example.getexperience.EventBus.PassMassageActionClick;
import com.example.getexperience.EventBus.StudentCourseClick;
import com.example.getexperience.EventBus.StudentMyCourseClick;
import com.example.getexperience.EventBus.StudentMyOpportunityClick;
import com.example.getexperience.EventBus.StudentOpportunityClick;
import com.example.getexperience.Manager.ui.AddFieldFragment;
import com.example.getexperience.Manager.ui.FieldDetailsFragment;
import com.example.getexperience.Manager.ui.FieldsManagementFragment;
import com.example.getexperience.Manager.ui.FoundationManagementFragment;
import com.example.getexperience.Manager.ui.ManagerFoundationDetailsFragment;
import com.example.getexperience.OuterPages.LoginActivity;
import com.example.getexperience.Prevalent;
import com.example.getexperience.R;

import com.example.getexperience.databinding.ActivityStudentBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class StudentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityStudentBinding binding;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private boolean mToolBarNavigationListenerIsRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setSupportActionBar(binding.appBarSutdent.toolbar);

        navigationView = binding.navView;
        View headerView = navigationView.getHeaderView(0);
        TextView name = headerView.findViewById(R.id.student_name);
        name.setText(Prevalent.currentOnlineStudent.getName());
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        toolbar = binding.appBarSutdent.toolbar;
        toolbar.setTitle(R.string.new_opportunities);
        setSupportActionBar(toolbar);

        drawer = binding.drawerLayout;
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        Fragment selectedScreen = StudentNewOpportunitiesFragment.createFor();
        showFragment(selectedScreen);
        toolbar.setTitle(R.string.new_opportunities);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_sutdent);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    protected void onPause() {
        super.onPause();
        finish();
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
        if (id == R.id.nav_student_new_opportunities) {
            Fragment selectedScreen = StudentNewOpportunitiesFragment.createFor();
            showFragment(selectedScreen);
            toolbar.setTitle(R.string.new_opportunities);
        }
        else if (id == R.id.nav_student_new_courses) {
            Fragment selectedScreen = StudentNewCoursesFragment.createFor();
            showFragment(selectedScreen);
            toolbar.setTitle(R.string.new_courses);
        }
        else if (id == R.id.nav_student_request_state) {
            Fragment selectedScreen = StudentRequestStateFragment.createFor();
            showFragment(selectedScreen);
            toolbar.setTitle(R.string.requests_state);
        }
        else if (id == R.id.nav_student_courses) {
            Fragment selectedScreen = MyCoursesFragment.createFor();
            showFragment(selectedScreen);
            toolbar.setTitle(R.string.my_courses);
        }
        else if (id == R.id.nav_student_opportunities) {
            Fragment selectedScreen = MyOpportunitiesFragment.createFor();
            showFragment(selectedScreen);
            toolbar.setTitle(R.string.my_opportunities);
        }
        else if (id == R.id.nav_download_certificate) {
            Fragment selectedScreen = DownloadCertificateFragment.createFor();
            showFragment(selectedScreen);
            toolbar.setTitle(R.string.download_certificate);
        } else if (id == R.id.nav_logout_student) {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Confirm Logout")
                    .setContentText("Do you really want to log out")
                    .setConfirmText("Yes")
                    .setConfirmClickListener(sDialog -> {
                        startActivity(new Intent(StudentActivity.this, LoginActivity.class));
                        sDialog.dismiss();
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

    private void SignOut(){
        //FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(StudentActivity.this, LoginActivity.class));
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment_content_sutdent, fragment)
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

        if (event.getMsg().equals("Back")) {
            toolbar.setTitle(R.string.new_opportunities);
            Fragment selectedScreen = StudentNewOpportunitiesFragment.createFor();
            showFragment(selectedScreen);

        }else if (event.getMsg().equals("New Courses")) {

            toolbar.setTitle("New Courses");
            Fragment selectedScreen = StudentNewCoursesFragment.createFor();
            showFragment(selectedScreen);

        }else if (event.getMsg().equals("DisplayOpportunityRequest")) {
            toolbar.setTitle("Opportunity Requests");
            Fragment selectedScreen = StudentOpportunityRequestsFragment.createFor();
            showFragment(selectedScreen);

        }else if (event.getMsg().equals("Display Course Request")) {
            toolbar.setTitle("Course Requests");
            Fragment selectedScreen = StudentCourseRequestsFragment.createFor();
            showFragment(selectedScreen);

        }else if (event.getMsg().equals("CourseCertificates")) {
            toolbar.setTitle("Course Certificates");
            Fragment selectedScreen = DownloadCourseCertificatesFragment.createFor();
            showFragment(selectedScreen);

        }else if (event.getMsg().equals("OpportunityCertificates")) {
            toolbar.setTitle("Opportunity Certificates");
            Fragment selectedScreen = DownloadOpportunityCertificatesFragment.createFor();
            showFragment(selectedScreen);
        }

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onStudentNewOpportunitySelected(StudentOpportunityClick event) {
        if (event.isSuccess()) {
            toolbar.setTitle("New Opportunity Details");
            Fragment selectedScreen = StudentNewOpportunityDetailsFragment.createFor(event.getNewOpportunityModel());
            showFragment(selectedScreen);
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onStudentNewOpportunitySelected(StudentMyOpportunityClick event) {
        if (event.isSuccess()) {

            toolbar.setTitle("My Opportunity Details");
            Fragment selectedScreen = MyOpportunityDetailsFragment.createFor(event.getMyOpportunityModel());
            showFragment(selectedScreen);
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onStudentNewOpportunitySelected(StudentCourseClick event) {
        if (event.isSuccess()) {
            toolbar.setTitle("New Course Details");
            Fragment selectedScreen = StudentNewCoursesDetailsFragment.createFor(event.getNewCourseModel());
            showFragment(selectedScreen);

        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onStudentMyCourseSelected(StudentMyCourseClick event) {
        if (event.isSuccess()) {
            toolbar.setTitle("My Course Details");
            Fragment selectedScreen = MyCourseDetailsFragment.createFor(event.getMyCourseRequestModel());
            showFragment(selectedScreen);
        }
    }
}