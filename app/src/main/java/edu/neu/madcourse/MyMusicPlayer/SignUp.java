package edu.neu.madcourse.MyMusicPlayer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.neu.madcourse.MyMusicPlayer.dao.MessageDao;

//import edu.neu.madcourse.MyMusicPlayer.dao.MessageDao;

public class SignUp extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "SignUp";
    private Button btnSubmit;
    private EditText edtName, edtEmail, edtPassword, edtRePassword;
    private TextView tvName, tvEmail, tvPassword, tvRePassword, tvMatch;
    private ConstraintLayout parent;
//    private MessageDao messageDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        parent = findViewById(R.id.SignUpParent);
        btnSubmit = findViewById(R.id.btnSubmit);
        edtName = findViewById(R.id.edtNameSignUp);
        edtEmail = findViewById(R.id.edtEmailSignUp);
        edtPassword = findViewById(R.id.edtPasswordSignUp);
        edtRePassword = findViewById(R.id.edtRePasswordSignUp);
        tvName = findViewById(R.id.notificationName);
        tvEmail = findViewById(R.id.notificationEmail);
        tvPassword = findViewById(R.id.notificationPassword);
        tvRePassword = findViewById(R.id.notificationRePassword);
        tvMatch = findViewById(R.id.notificationPasswordMatch);
        btnSubmit.setOnClickListener(this::onClick);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit:{
                tvName.setVisibility(View.INVISIBLE);
                tvEmail.setVisibility(View.INVISIBLE);
                tvPassword.setVisibility(View.INVISIBLE);
                tvRePassword.setVisibility(View.INVISIBLE);
                tvMatch.setVisibility(View.INVISIBLE);
                boolean canSubmit = true;
                if (edtName.getText().toString().length() == 0) {
                    tvName.setVisibility(View.VISIBLE);
//                    Log.d(TAG, "NAME");
                    canSubmit = false;
                }
                if (edtEmail.getText().toString().length() == 0) {
                    tvEmail.setVisibility(View.VISIBLE);
//                    Log.d(TAG, "Email");
                    canSubmit = false;
                }
                if (edtPassword.getText().toString().length() == 0) {
                    tvPassword.setVisibility(View.VISIBLE);
//                    Log.d(TAG, "password");
                    canSubmit = false;
                }
                if (edtRePassword.getText().toString().length() == 0) {
                    tvRePassword.setVisibility(View.VISIBLE);
                    canSubmit = false;
                }
                String pass = edtPassword.getText().toString(), rePass = edtRePassword.getText().toString();
                if (pass.length() > 0 && rePass.length() > 0 && !pass.equals(rePass)) {
                    tvMatch.setVisibility(View.VISIBLE);
                    canSubmit = false;
                }

                if (canSubmit) {
                    User newUser = new User(edtName.getText().toString(), edtEmail.getText().toString(), edtPassword.getText().toString());
                    DatabaseReference testReference =
                            FirebaseDatabase.getInstance()
                                    .getReference(User.class.getSimpleName());
                    Query query = testReference.orderByChild("userName").equalTo(edtName.getText().toString());

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getChildrenCount() > 0) {
                                Snackbar.make(SignUp.this, parent, "The userName is exist, please sign in using password", Snackbar.LENGTH_SHORT).show();
                            }else{
                                MessageDao.writeToDatabase(User.class.getSimpleName(), newUser, SignUp.this);
                                Intent intent = new Intent(SignUp.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                break;
            }
                default:
                    break;

        }
    }

}