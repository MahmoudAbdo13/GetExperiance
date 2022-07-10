package com.example.getexperience.Foundation;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.getexperience.Prevalent;
import com.example.getexperience.R;
import com.example.getexperience.databinding.ActivityFoundationBinding;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

public class OrganizationActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityFoundationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFoundationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarFoundation.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        View headerView = navigationView.getHeaderView(0);
        TextView foundationName = headerView.findViewById(R.id.home_foundation_name_title);
        ImageView foundationImage = headerView.findViewById(R.id.home_foundation_image);
        foundationName.setText(Prevalent.currentOnlineOrganization.getName());

        Glide.with(this).load(Prevalent.currentOnlineOrganization.getImageUrl()).into(foundationImage);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_opportunities_management, R.id.nav_opportunity_requests, R.id.nav_courses_requests,
                R.id.nav_courses_management, R.id.nav_upload_certificate, R.id.nav_logout_foundation)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_foundation);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_foundation);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

/*    @Override

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_opportunities_management:
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                break;
            case R.id.nav_opportunity_requests:
                startActivity(new Intent(HomeActivity.this, InvitationActivity.class));
                break;
            case R.id.nav_courses_requests:
                startActivity(new Intent(HomeActivity.this, TransactionsActivity.class));
                break;
            case R.id.nav_courses_management:
                startActivity(new Intent(HomeActivity.this, TermsActivity.class));
                break;
            case R.id.nav_upload_certificate:
                Fragment selectedScreen = BlockedFoundationFragment.createFor();
                showFragment(selectedScreen);
                toolbar.setTitle(R.string.blocked_foundation_management);
                break;
            */
/*case R.id.nav_chat:
                startActivity(new Intent(HomeActivity.this, ChatActivity.class));
                break;*//*

            case R.id.nav_logout_foundation:
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
                        }).show();;
                break;
        }
        DrawerLayout drawer = binding.drawerLayout;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
*/
}