import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ManageAccount extends AppCompatActivity{
}

public void forgotPassword() {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String emailAddress = "user@example.com";
    auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Email sent.");
                    }
                }
            });
}

    public void changeEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updateEmail("user@example.com").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email Updated!.");
                        }
                    }
                });
    }

    public void changePassword() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String newPassword = "SOME-SECURE-PASSWORD";
        user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Password updated!");
                        }
                    }
                });
    }
    public void deleteUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Account Deleted!"); //afaf
                        }
                    }
                });
    }