package edu.neu.madcourse.MyMusicPlayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import edu.neu.madcourse.MyMusicPlayer.dao.FirebaseMessageDao;
import edu.neu.madcourse.MyMusicPlayer.dao.MessageDao;

//import edu.neu.madcourse.MyMusicPlayer.dao.FirebaseMessageDao;
//import edu.neu.madcourse.MyMusicPlayer.dao.MessageDao;

/**
 * Log in and Sign in Page
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnSignIn, btnSignUp;
    private MessageDao messageDao;
    private EditText edtName, edtPassword;
    private ConstraintLayout parent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        edtName = findViewById(R.id.edtName);
        edtPassword = findViewById(R.id.edtPassword);
        parent = findViewById(R.id.mainView);
        btnSignIn.setOnClickListener(this::onClick);
        btnSignUp.setOnClickListener(this::onClick);
        messageDao = new FirebaseMessageDao();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignUp:  //switch to sign up page
                Intent intentSignUp = new Intent(MainActivity.this, SignUp.class);
                startActivity(intentSignUp);
                break;
//            case R.id.btnSignIn:
//                Intent intentHome = new Intent(MainActivity.this, SignUp.class);
//                startActivity(intentHome);
//                break;
            default:  //check the input information
//                Intent intentHome = new Intent(MainActivity.this, MyHomePage.class);
//                startActivity(intentHome);

                //get the name of database
                DatabaseReference testReference =
                        FirebaseDatabase.getInstance()
                                .getReference(User.class.getSimpleName());
                //find the data with userName equal to input field
                Query query = testReference.orderByChild("userName").equalTo(edtName.getText().toString());

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //if has the matched username, check email
                        if(dataSnapshot.getChildrenCount() > 0) {
                            for (DataSnapshot user: dataSnapshot.getChildren()) {
                                User u = user.getValue(User.class);
                                if (u.getPassword().equals(edtPassword.getText().toString())) {
                                    Intent intentHome = new Intent(MainActivity.this, MyHomePage.class);
                                    startActivity(intentHome);
                                }
                                else {
                                    Snackbar.make(MainActivity.this, parent, "Password wrong", Snackbar.LENGTH_SHORT).show();
                                }
                            }

                        }else{
                            Snackbar.make(MainActivity.this, parent, "User doesn't exist", Snackbar.LENGTH_SHORT).show();
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        }
    }

}